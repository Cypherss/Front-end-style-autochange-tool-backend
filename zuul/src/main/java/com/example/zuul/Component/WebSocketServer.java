package com.example.zuul.Component;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-05 16:34
 */

@ServerEndpoint(value = "/replace/schedule")
@Component
public class WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }

    final String CORE_HEADER = "http://core";
    final String USER_HEADER = "http://user";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("有新连接加入：{}", session.getId());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        LOGGER.info("有一连接关闭：{}", session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("服务端收到客户端[{}]的消息:{}", session.getId(), message);
        try {
            if (message.startsWith("match")){
                String[] fileIds = message.replace("match:","").split("@");
                if(fileIds.length!=2){
                    this.sendMessage("fail",session);
                    return;
                }
                System.out.println(restTemplate);
                boolean res = restTemplate.postForObject(CORE_HEADER+"/replace/match?sourceId={1}&targetId={2}",null,Boolean.class,fileIds[0],fileIds[1]);
                if(res){
                    this.sendMessage("matchSuccess",session);
                }else {
                    this.sendMessage("fail",session);
                }
                return;
            }
            if (message.startsWith("replace")){
                String resFileId = restTemplate.postForObject(CORE_HEADER+"/replace/replace",null,String.class);
                this.sendMessage("replaceSuccess:"+resFileId,session);
                return;
            }
            if(message.startsWith("rebuild")){
                String[] items = message.replace("rebuild:", "").split("@");
                if(items.length!=3){
                    this.sendMessage("fail",session);
                    return;
                }
                JSONObject res = restTemplate.postForObject(CORE_HEADER+"/replace/html?fileId={1}",null,JSONObject.class,items[1]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                boolean insertFlag = restTemplate.postForObject(USER_HEADER+"/user/record?userId={1}&sourceId={2}&targetId={3}&time={4}",null,Boolean.class,items[2],items[0],items[1],""+sdf.format(new Date()));
                if (insertFlag){
                    this.sendMessage("rebuildSuccess:"+JSONObject.toJSONString(res),session);
                }else{
                    this.sendMessage("fail",session);
                }
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            this.sendMessage("fail",session);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("发生错误");
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            LOGGER.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            LOGGER.error("服务端发送消息给客户端失败：{}", e);
        }
    }
}
