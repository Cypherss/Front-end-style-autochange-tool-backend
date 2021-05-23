package com.example.core.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.core.ServiceImpl.UtilServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import org.apache.commons.io.FileUtils;


import java.io.File;
import java.util.HashSet;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 13:30
 */
public class UtilsServiceTest {
    @Test
    public void generateHtmlTest(){
        File file = new File("C:\\Users\\Administrator\\Desktop\\c78c0b90-bb94-11eb-9127-ab49135ca54d.json");
        try{
            String content = FileUtils.readFileToString(file);
            JSONObject jsonObject = JSON.parseObject(content);
            UtilService utilService = new UtilServiceImpl();
            String htmlCode = utilService.generateHTML(jsonObject,false,false,new HashSet<>());
            htmlCode = "<!DOCTYPE html><head><meta charset=\"utf-8\"></head>" + htmlCode+ "</html>";
            System.out.println(htmlCode);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void addHtmlTest(){
        File file = new File("src/test/resources/test.html");
        try {
            String content = FileUtils.readFileToString(file);
            Document document = Jsoup.parse(content);
            Element target = document.getElementById("divc");
            String originStyle = target.attr("style");
            target.removeAttr("style");
            target.attr("style",originStyle+"width:200px;");
            System.out.println(document.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void generateDomTest(){
        File file = new File("src/test/resources/content3.json");
        try{
            String content = FileUtils.readFileToString(file);
            UtilServiceImpl utilService = new UtilServiceImpl();
            JSONObject jsonObject = JSON.parseObject(content);
            System.out.println(JSONObject.toJSONString(jsonObject));
            utilService.generateHTML(jsonObject,false,false,new HashSet<>());
            System.out.println(JSONObject.toJSONString(jsonObject));
            JSONObject root = utilService.getIdDomTree(jsonObject);
            System.out.println(JSON.toJSONString(root));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
