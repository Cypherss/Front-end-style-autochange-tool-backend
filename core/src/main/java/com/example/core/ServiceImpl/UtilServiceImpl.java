package com.example.core.ServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
//    public String generateHTML(String json){
//        JSONObject jsonObject = JSON.parseObject(json);
//        String htmlBody = htmlBuilder(jsonObject, false, false, new HashSet<String>());
//        return "<!DOCTYPE html><head><meta charset=\"utf-8\"></head>" + htmlBody + "</html>";
//    }

    /**
     * 调整页面css
     * @param html
     * @param id
     * @param attribute
     * @return
     */
    public String adjustStyle(String html,String id,String attribute){
        Document document = Jsoup.parse(html);
        Element target = document.getElementById(id);
        String originStyle = "";
        originStyle = target.attr("style");
        target.removeAttr("style");
        target.attr("style",originStyle+attribute);
        return document.toString();
    }


    /**
     * 生成ID DOM树
     * @param jsonObject
     * @return
     */
    public JSONObject getIdDomTree(JSONObject jsonObject){
        if(jsonObject==null){
            return null;
        }
        if(!jsonObject.containsKey("info")){
            return null;
        }
        JSONObject root = new JSONObject();
        root.put("tag",jsonObject.getJSONObject("info").getString("tag"));
        root.put("id", jsonObject.getJSONObject("info").getString("id"));
        root.put("isLeaf", true);
        if(jsonObject.containsKey("children")){
            List<JSONObject> children = new ArrayList<>();
            JSONArray subJsonObjs = jsonObject.getJSONArray("children");
            for(int i=0;i<subJsonObjs.size();i++){
                JSONObject sub = subJsonObjs.getJSONObject(i);
                JSONObject subRoot = getIdDomTree(sub);
                if (subRoot != null){
                    children.add(subRoot);
                }
            }
            if (children.size()>0){
                root.put("isLeaf", false);
                root.put("children",children);
            }
        }
        return root;
    }

    /**
     * 根据json组件树重建html
     * @param jsonObject
     * @param heightAuto
     * @param widthAuto
     * @param ids
     * @return
     */
    public String generateHTML(JSONObject jsonObject,boolean heightAuto, boolean widthAuto, Set<String> ids){
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
                return generateHTML((JSONObject) element, subHeightAuto, subWidthAuto,ids);
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
        String id = UUID.randomUUID().toString();
        while(ids.contains(id)){
            id = UUID.randomUUID().toString();
        }
        ids.add(id);
        jsonObject.getJSONObject("info").put("id", id);
        String htmlCode = "<" + tag + " id = \"" + id + "\" style = \"" + style + "\"" + ">";
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
