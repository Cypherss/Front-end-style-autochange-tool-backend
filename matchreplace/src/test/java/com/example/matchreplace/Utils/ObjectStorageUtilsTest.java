package com.example.matchreplace.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-05-22 00:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ObjectStorageUtilsTest {
    @Autowired
    ObjectStorageUtils objectStorageUtils;

    @Test
    public void saveTest(){
        String testStr = "test str";
        String fileId = objectStorageUtils.saveFile(testStr);
        System.out.println(objectStorageUtils.getFileContetnt(fileId));
    }
}
