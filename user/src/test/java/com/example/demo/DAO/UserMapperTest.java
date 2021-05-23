package com.example.demo.DAO;

import com.example.demo.PO.Group;
import com.example.demo.PO.Record;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-26 17:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void sourceNameTest(){
        System.out.println(userMapper.getSourceName("3471e330-ba49-11eb-9639-935c69467859.json"));
    }

    @Test
    void addUser(){
        User user = new User("test","123");
        userMapper.addUser(user);
        System.out.println(user.getId());
    }

    @Test
    void getUser() {
        User user = new User("test","123");
        userMapper.addUser(user);
        Assert.assertTrue(userMapper.getUser(user)!=null);
    }

    @Test
    void addFile() {
        SourceFile sourceFile = new SourceFile("testFile","test","2020","xml",0);
        Assert.assertEquals(1,userMapper.addFile(sourceFile));
    }

    @Test
    void addUserFile() {
        SourceFile sourceFile = new SourceFile("testFile","test","2020","xml",0);
        userMapper.addFile(sourceFile);
        Assert.assertEquals(1,userMapper.addUserFile(0,"testFile"));
    }

    @Test
    void addGroup() {
        Assert.assertEquals(1,userMapper.addGroup(new Group("g1","g1")));
    }

    @Test
    void addGroupMember() {
        Group group = new Group("g1","g1");
        userMapper.addGroup(group);
        Assert.assertEquals(1,userMapper.addGroupMember(group.getId(),0));
    }

    @Test
    void addGroupFile() {
        SourceFile sourceFile = new SourceFile("testFile","test","2020","xml",0);
        userMapper.addFile(sourceFile);
        Group group = new Group("g1","g1");
        userMapper.addGroup(group);
        Assert.assertEquals(1,userMapper.addGroupFile(group.getId(),sourceFile.getId()));
    }

    @Test
    void getGroupsByUserId() {
        Group group = new Group("g1","g1");
        userMapper.addGroup(group);
        userMapper.addGroupMember(group.getId(),0);
        Assert.assertEquals(1,userMapper.getGroupsByUserId(0).size());
    }

    @Test
    void getUsersByGroupId() {
        Group group = new Group("g1","g1");
        userMapper.addGroup(group);
        userMapper.addGroupMember(group.getId(),1);
        Assert.assertEquals(1,userMapper.getUsersByGroupId(group.getId()).size());
    }

    @Test
    void getSourceFilesByUserId() {
        SourceFile sourceFile = new SourceFile("testFile","test","2020","xml",0);
        userMapper.addFile(sourceFile);
        userMapper.addUserFile(1,"testFile");
        SourceFile sourceFile2 = new SourceFile("testFile2","tes2t","2020","xml",0);
        userMapper.addFile(sourceFile2);
        userMapper.addUserFile(1,"testFile2");
        Assert.assertEquals(2,userMapper.getSourceFilesByUserId(1).size());
    }

    @Test
    void getSourceFilesByGroupId() {
        SourceFile sourceFile = new SourceFile("testFile","test","2020","xml",0);
        userMapper.addFile(sourceFile);
        Group group = new Group("g1","g1");
        userMapper.addGroup(group);
        userMapper.addGroupFile(group.getId(),sourceFile.getId());
        Assert.assertEquals(1,userMapper.getSourceFilesByGroupId(group.getId()).size());
    }

    @Test
    void recordTest(){
        Record record = new Record(1,"hello.json","hi.json","20201011");
        Assert.assertEquals(1,userMapper.addRecord(record));
        List<Record> records = userMapper.getUserHistory(1);
        Assert.assertEquals(1,records.size());
        System.out.println(records.get(0));

    }
}
