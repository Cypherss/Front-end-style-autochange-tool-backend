package com.example.core.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 13:56
 */
@Service
public class UtilServiceImpl implements UtilService {
    /**
     * 根据json组件树重建html
     * @param json
     * @return
     */
    public String generateHTML(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        String htmlBody = rebuildHTML(jsonObject);
        return "<!DOCTYPE html><head><meta charset=\"utf-8\"></head>" + htmlBody + "</html>";
    }

    public String rebuildHTML(JSONObject jsonObject){
        if(jsonObject==null){
            return "";
        }
        if(!jsonObject.containsKey("info")){
            return "";
        }
        if(jsonObject.containsKey("type") && jsonObject.getString("type").equals("svg")){
            return "";
        }
        String subHtmlCode = "";
        if(jsonObject.containsKey("children")){
            JSONArray subJsonObjects = jsonObject.getJSONArray("children");
            subHtmlCode += Arrays.stream(subJsonObjects.toArray()).map((Object element) -> {
                return rebuildHTML((JSONObject) element);
            }).collect(Collectors.joining());
        }
        String tag = jsonObject.getJSONObject("info").getString("tag").toLowerCase();
        Map<String,Object> css = jsonObject.getJSONObject("info").getJSONObject("css").getInnerMap();
        List<String> styleItems = new LinkedList<>();
        for(String key:css.keySet()){
            styleItems.add(key+": "+css.get(key));
        }
        StringJoiner styleJoiner = new StringJoiner(";");
        styleItems.forEach(item -> styleJoiner.add(item));
        String style = styleJoiner.toString();
        String htmlCode = "<" + tag + " style = \"" + style + "\"" + ">";
        if(jsonObject.containsKey("content")){
            htmlCode += jsonObject.getString("content");
        }
        htmlCode += subHtmlCode;
        htmlCode += "</" + tag + ">";
        return htmlCode;
    }
}
