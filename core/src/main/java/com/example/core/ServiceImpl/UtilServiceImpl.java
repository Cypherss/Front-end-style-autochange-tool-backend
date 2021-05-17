package com.example.core.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        String htmlBody = htmlBuilder(jsonObject, false, false);
        return "<!DOCTYPE html><head><meta charset=\"utf-8\"></head>" + htmlBody + "</html>";
    }

    public String htmlBuilder(JSONObject jsonObject,boolean heightAuto, boolean widthAuto){
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

        //optimize
        boolean subHeightAuto,subWidthAuto;
        if (jsonObject.getJSONObject("info").getInteger("scrollHeight") >
                jsonObject.getJSONObject("info").getInteger("offsetHeight")){
            subHeightAuto = true;
        }else{
            subHeightAuto = false;
        }
        if (jsonObject.getJSONObject("info").getInteger("scrollWidth") >
                jsonObject.getJSONObject("info").getInteger("offsetWidth")){
            subWidthAuto = true;
        }else{
            subWidthAuto = false;
        }

        if(jsonObject.containsKey("children")){
            JSONArray subJsonObjects = jsonObject.getJSONArray("children");
            subHtmlCode += Arrays.stream(subJsonObjects.toArray()).map((Object element) -> {
                return htmlBuilder((JSONObject) element, subHeightAuto, subWidthAuto);
            }).collect(Collectors.joining());
        }
        String tag = jsonObject.getJSONObject("info").getString("tag").toLowerCase();
        String usedCss = jsonObject.getJSONObject("info").getString("usedCss");
        String style = "";
        //预处理
        if(usedCss!=null&&usedCss.contains("{")){
            usedCss = usedCss.replace("\"","\'");
            usedCss = usedCss.replaceAll("/\\*!.*\\*/","");
            usedCss = usedCss.replaceAll("\n","");
            Pattern pattern = Pattern.compile(".*?\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(usedCss);
            List<String> cssItems = new LinkedList<>();
            while (matcher.find()){
                cssItems.add(matcher.group(1));
            }

            if(cssItems.size()>0){
                style= cssItems.stream()
                        .filter(x -> x.length() > 0)
                        .filter(x -> !x.contains("{"))
                        .collect(Collectors.joining());
            }

            style = optimize(style, heightAuto, widthAuto);
        }
        /**
        Map<String,Object> css = jsonObject.getJSONObject("info").getJSONObject("css").getInnerMap();
        List<String> styleItems = new LinkedList<>();
        for(String key:css.keySet()){
            String val = (String)css.get(key);
            if(val.contains("\"")){
                val = val.replace("\"","\'");
            }
            styleItems.add(key+": "+val);
        }

        StringJoiner styleJoiner = new StringJoiner(";");
        styleItems.forEach(item -> styleJoiner.add(item));
        String style = styleJoiner.toString();
        **/
        String htmlCode = "<" + tag + " style = \"" + style + "\"" + ">";
        //String htmlCode = "<" + tag + ">";
        if(jsonObject.containsKey("content")){
            htmlCode += jsonObject.getString("content");
        }
        htmlCode += subHtmlCode;
        htmlCode += "</" + tag + ">";
        return htmlCode;
    }

    public String optimize(String style, boolean heightAuto, boolean widthAuto){
        if(!heightAuto && !widthAuto){
            return style;
        }
        String[] styles = style.split(";");
        if(heightAuto){
            style = Arrays.stream(styles)
                    .filter( x -> !x.trim().startsWith("height"))
                    .collect(Collectors.joining(";"));
            style += "height:auto;";
        }
        if(widthAuto) {
            style = Arrays.stream(styles)
                    .filter(x -> !x.trim().startsWith("width"))
                    .collect(Collectors.joining(";"));
            style += "width:auto;";
        }
        return style;
    }
}
