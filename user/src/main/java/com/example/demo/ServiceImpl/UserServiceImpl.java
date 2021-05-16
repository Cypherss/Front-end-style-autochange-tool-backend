package com.example.demo.ServiceImpl;

import com.example.demo.DAO.UserMapper;
import com.example.demo.PO.Group;
import com.example.demo.PO.Record;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;
import com.example.demo.Service.UserService;
import com.example.demo.DTO.RecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:14
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    public int register(String name, String password){
        User user = new User(name, password);
        if (userMapper.addUser(user) == 1){
            return user.getId();
        }
        return -1;
    }
    public User login(String name, String password){
        User user = userMapper.getUser(new User(name, password));
        if(user != null){
            return user;
        }
        return null;
    }
    public List<SourceFile> getUserSources(int userId){
        return userMapper.getSourceFilesByUserId(userId);
    }

    public boolean addUserSource(String fileId, int userId, String sourceName, String uploadTime, String type){
        boolean res = (userMapper.addFile(new SourceFile(fileId, sourceName,uploadTime,type,userId)) == 1);
        if (res){
            userMapper.addUserFile(userId,fileId);
            return true;
        }
        return false;
    }
    public boolean shareSource(int groupId, String sourceName){
        return userMapper.addGroupFile(groupId, sourceName) == 1;
    }
    public int addGroup(String name, String description){
        Group group = new Group(name, description);
        if (userMapper.addGroup(group) == 1){
            return group.getId();
        }
        return -1;
    }
    public boolean addGroupMember(int groupId,int userId){
        return userMapper.addGroupMember(groupId, userId) == 1;
    }
    public List<Group> getUserGroups(int userId){
        return userMapper.getGroupsByUserId(userId);
    }
    public List<User> getGroupUsers(int groupId){
        return userMapper.getUsersByGroupId(groupId);
    }
    public List<SourceFile> getGroupSources(int groupId) {
        return userMapper.getSourceFilesByGroupId(groupId);
    }
    public boolean addRecord(int userId,String sourceId,String targetId, String time){
        return userMapper.addRecord(new Record(userId,sourceId,targetId,time))==1;
    }

    public List<RecordDTO> getUserHistory(int userId){
        List<Record> records = userMapper.getUserHistory(userId);
        List<RecordDTO> ans = new ArrayList<>();
        for(Record record:records){
            ans.add(record.getDTO());
        }
        return ans;
    }
}
  