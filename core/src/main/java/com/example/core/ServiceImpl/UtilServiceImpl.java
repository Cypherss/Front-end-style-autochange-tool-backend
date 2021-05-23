package com.example.core.ServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.core.Service.UtilService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    @Autowired
    RestTemplate restTemplate;

    final String STORAGE_HEADER = "http://objectstorage/minio";
    final String USER_HEADER = "http://user/user";
    /**
     * 调整页面css
     * @param fileId
     * @param id
     * @param attribute
     * @return
     */
    public String adjustStyle(String fileId,String id,String attribute,String time){
        String html = restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,"html");
        Document document = Jsoup.parse(html);
        Element target = document.getElementById(id);
        target.removeAttr("style");
        target.attr("style",attribute);
        MultipartFile tempFile = fileTransfer(fileId.split("-")[0],document.toString(),"html");
        String newFileID = fileSave(tempFile,"html");
        restTemplate.postForObject(USER_HEADER+"/record/update?targetId={1}&time={2}",null,Boolean.class,newFileID,time);
        return restTemplate.getForObject(STORAGE_HEADER+"/url?htmlKey={1}",String.class,newFileID);
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
            //System.out.println("out:"+jsonObject.getString("children"));
            //System.out.println(jsonObject.getString("children")==null);
            if(jsonObject.getString("children")!=null){
                JSONArray subJsonObjects = jsonObject.getJSONArray("children");
                subHtmlCode += Arrays.stream(subJsonObjects.toArray()).map((Object element) -> {
                    if(element instanceof String){
                        return "";
                    }
                    return generateHTML((JSONObject) element, subHeightAuto, subWidthAuto,ids);
                }).collect(Collectors.joining());
            }
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

//        Map<String,Object> css = jsonObject.getJSONObject("info").getJSONObject("css").getInnerMap();
//        List<String> styleItems = new LinkedList<>();
//        for(String key:css.keySet()){
//            String val = (String)css.get(key);
//            if(val.contains("\"")){
//                val = val.replace("\"","\'");
//            }
//            styleItems.add(key+": "+val);
//        }
//
//        StringJoiner styleJoiner = new StringJoiner(";");
//        styleItems.forEach(item -> styleJoiner.add(item));
//        String style = styleJoiner.toString();
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

    public MultipartFile fileTransfer(String fileName, String content, String type){
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
}
