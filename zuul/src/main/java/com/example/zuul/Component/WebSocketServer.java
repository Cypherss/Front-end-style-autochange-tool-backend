package com.example.zuul.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-05 16:34
 */

@ServerEndpoint(value = "/replace/schedule")
@Component
public class WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    @Autowired
    RestTemplate restTemplate;

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
            //match and replace
            //todo
            this.sendMessage("50%",session);

            //optimize
            //todo
            this.sendMessage("75%",session);

            this.sendMessage("end",session);

        }catch (Exception e){
            LOGGER.error(e.getMessage());
            this.sendMessage("end",session);
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
