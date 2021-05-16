package com.example.core.Service;

import com.example.core.ServiceImpl.UtilServiceImpl;
import org.junit.jupiter.api.Test;

import org.apache.commons.io.FileUtils;


import java.io.File;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-16 13:30
 */
public class UtilsServiceTest {
    @Test
    public void generateHtmlTest(){
        File file = new File("src/test/resources/content.json");
        try{
            String content = FileUtils.readFileToString(file);
            UtilService utilService = new UtilServiceImpl();
            System.out.println(utilService.generateHTML(content));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
