package com.example.objectstorage.Controller;

import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-17 15:32
 */

@RestController
@RequestMapping("/minio")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @Value("${minio.endpoint}")
    private String ENDPOINT;
    @Value("${minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String SECRET_KEY;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("type")String type) {
        try {
            //创建一个MinIO的Java客户端
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESS_KEY, SECRET_KEY);
            boolean isExist = minioClient.bucketExists(type);
            if (isExist) {
                LOGGER.info("存储桶已经存在！");
            } else {
                //创建存储桶并设置只读权限
                minioClient.makeBucket(type);
                minioClient.setBucketPolicy(type, "*.*", PolicyType.READ_ONLY);
            }
            String filename = file.getOriginalFilename();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 设置存储对象名称
            String uuid = UUID.randomUUID().toString();
            String objectName = uuid + sdf.format(new Date()) + "-" + filename;
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(type, objectName, file.getInputStream(), file.getContentType());
            return objectName;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("上传发生错误: {}！", e.getMessage());
        }
        return "error";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public boolean delete(@RequestParam("objectName") String objectName,@RequestParam("type") String type) {
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESS_KEY, SECRET_KEY);
            minioClient.removeObject(type, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(@RequestParam("objectName") String objectName,@RequestParam("type") String type) {
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESS_KEY, SECRET_KEY);
            InputStream inputStream = minioClient.getObject(type, objectName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

}
