package com.example.core.Service;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 13:56
 */
public interface UtilService {
    public String generateHTML(JSONObject jsonObject,boolean heightAuto, boolean widthAuto, Set<String> ids);
    public JSONObject getIdDomTree(JSONObject jsonObject);
}
