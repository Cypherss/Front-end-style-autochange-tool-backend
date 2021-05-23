package com.example.core.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    final String MATCHREPLACE_HEADER = "http://matchreplace/replaceAndMatch";
    @Autowired
    UtilService utilService;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public Boolean match(@RequestParam("sourceId") String sourceId,@RequestParam("targetId") String targetId){
        try {
            if(restTemplate.getForObject(MATCHREPLACE_HEADER+"/match?fileId1={1}&fileId2={2}",Boolean.class,sourceId,targetId)){
                return true;
            }
            return false;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    public String replace(){
        try {
            return restTemplate.getForObject(MATCHREPLACE_HEADER+"/replace",String.class);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    @RequestMapping(value = "/html", method = RequestMethod.POST)
    public JSONObject generateHtml(@RequestParam("fileId") String fileId){
        String[] temp = fileId.split("\\.");
        String type = temp[temp.length-1];
        String json = restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,type);
        JSONObject res = new JSONObject();
        JSONObject target = JSON.parseObject(json);
        String htmlCode = utilService.generateHTML(target,false,false,new HashSet<>());
        htmlCode = "<!DOCTYPE html><head><meta charset=\"utf-8\"></head>" + htmlCode + "</html>";
        String fileName = fileId.split("-")[0];
        MultipartFile tempFile = utilService.fileTransfer(fileName,htmlCode,"html");
        String htmlKey = utilService.fileSave(tempFile,"html");
        res.put("html",restTemplate.getForObject(STORAGE_HEADER+"/url?htmlKey={1}",String.class,htmlKey));
        res.put("idDom",utilService.getIdDomTree(target));
        return res;
    }



    @RequestMapping(value = "/adjust", method = RequestMethod.GET)
    public String adjustHtml(@RequestParam("fileId") String fileId,@RequestParam("id") String id,@RequestParam("attribute") String attribute,@RequestParam("time")String time){
        return utilService.adjustStyle(fileId,id,attribute,time);
    }

}
