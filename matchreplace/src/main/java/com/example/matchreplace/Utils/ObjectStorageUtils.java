package com.example.matchreplace.Utils;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    public String saveFile(String fileId, String content){
        try {
            System.out.println(fileId);
            MultipartFile tempFile = fileTransfer(fileId, content);
            System.out.println(tempFile.getOriginalFilename());
            System.out.println(tempFile.getClass());
            System.out.println(tempFile.getBytes());
            String newfileId = fileSave(tempFile,"json");
            return newfileId;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
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

    public String getFileContetnt(String fileId){
        return restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,"json");
    }

    public MultipartFile fileTransfer(String fileId, String content){
        try {
            String prefix = fileId.split("-")[0];
            File temp = File.createTempFile(prefix+"-", "." + "json");
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
}
