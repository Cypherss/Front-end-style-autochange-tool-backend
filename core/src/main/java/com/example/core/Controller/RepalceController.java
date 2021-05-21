package com.example.core.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 14:24
 */
@RestController
@RequestMapping("/replace")
public class RepalceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepalceController.class);

    final String STORAGE_HEADER = "http://objectstorage/minio";
    @Autowired
    UtilService utilService;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String match(@RequestParam("sourceId") String sourceId,@RequestParam("targetId") String targetId){
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return sourceId;
    }

    @RequestMapping(value = "/optimize", method = RequestMethod.POST)
    public String match(@RequestParam("targetId") String targetId){
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return targetId;
    }

    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public JSONObject generateHtml(@RequestParam("fileId") String fileId){
        String[] temp = fileId.split("\\.");
        String type = temp[temp.length-1];
        String json = restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,type);
        JSONObject res = new JSONObject();
        JSONObject target = JSON.parseObject(json);
        res.put("html",utilService.generateHTML(target,false,false,new HashSet<>()));
        res.put("idDom",utilService.getIdDomTree(target));
        return res;
    }

    @RequestMapping(value = "/adjust", method = RequestMethod.GET)
    public String adjustHtml(@RequestParam("html") String html,@RequestParam("id") String id,@RequestParam("attribute") String attribute){
        return utilService.adjustStyle(html,id,attribute);
    }

}
