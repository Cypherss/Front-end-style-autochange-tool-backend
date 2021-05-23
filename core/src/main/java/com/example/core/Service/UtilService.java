package com.example.core.Service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 13:56
 */
public interface UtilService {
    public String generateHTML(JSONObject jsonObject,boolean heightAuto, boolean widthAuto, Set<String> ids);
    public JSONObject getIdDomTree(JSONObject jsonObject);
    public MultipartFile fileTransfer(String fileName, String content, String type);
    public String fileSave(MultipartFile file,String type);
    public String adjustStyle(String fileId,String id,String attribute,String time);
}
