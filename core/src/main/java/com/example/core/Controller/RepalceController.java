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
        MultipartFile tempFile = fileTransfer(fileName,htmlCode,"html");
        String htmlKey = fileSave(tempFile,"html");
        res.put("html",restTemplate.getForObject(STORAGE_HEADER+"/url?htmlKey={1}",String.class,htmlKey));
        res.put("idDom",utilService.getIdDomTree(target));
        return res;
    }

    public MultipartFile fileTransfer(String fileName,String content,String type){
        try {
            File temp = File.createTempFile(fileName+"-", "." + type);
            FileUtils.writeStringToFile(temp, content);
            FileItem fileItem = createFileItem(temp);
            MultipartFile tempMultipartFile = new CommonsMultipartFile(fileItem);
            temp.deleteOnExit();
            return tempMultipartFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    public String fileSave(MultipartFile file,String type){
        try {
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            MediaType paramType = MediaType.parseMediaType("multipart/form-data");
            headers.setContentType(paramType);

            //转换文件
            ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            //设置请求体，注意是LinkedMultiValueMap
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", byteArrayResource);
            form.add("type",type);

            //用HttpEntity封装整个请求报文
            HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

            String fileId = restTemplate.postForObject(STORAGE_HEADER+"/upload", files, String.class);
            return fileId;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "/adjust", method = RequestMethod.GET)
    public String adjustHtml(@RequestParam("html") String html,@RequestParam("id") String id,@RequestParam("attribute") String attribute){
        return utilService.adjustStyle(html,id,attribute);
    }

}
