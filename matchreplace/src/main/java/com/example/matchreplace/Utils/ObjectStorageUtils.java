package com.example.matchreplace.Utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-21 23:49
 */
@Component
public class ObjectStorageUtils {

    @Autowired
    RestTemplate restTemplate;

    final String STORAGE_HEADER = "http://objectstorage/minio";
    public String saveFile(String content){
        try {
                return restTemplate.postForObject(STORAGE_HEADER+"/strupload?content={1}&type={2}",null,String.class,content,"json");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getFileContetnt(String fileId){
        return restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,"json");
    }
}
