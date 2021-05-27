package com.example.demo.DAO;

import com.example.demo.PO.Group;
import com.example.demo.PO.Record;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:33
 */

@Mapper
@Service
public interface UserMapper {
    public int addUser(User user);
    public User getUser(User user);
    public int addFile(SourceFile sourceFile);
    public int addUserFile(@Param(value = "userId") int userId, @Param(value = "fileId") String fileId);
    public int addGroup(Group group);
    public int addGroupMember(@Param(value = "groupId") int groupId,@Param(value = "userId") int userId);
    public int addGroupFile(@Param(value = "groupId") int groupId,@Param(value = "fileId") String fileId);
    public List<Group> getGroupsByUserId(int userId);
    public List<User> getUsersByGroupId(int groupId);
    public List<SourceFile> getSourceFilesByUserId(int userId);
    public List<SourceFile> getGroupSourceFilesByUserId(int userId);
    public List<SourceFile> getSourceFilesByGroupId(int groupId);
    public int addRecord(Record record);
    public List<Record> getUserHistory(int userId);
    public String getSourceName(String sourceId);
    public int updateRecordTarget(@Param(value = "targetId") String targetId, @Param(value = "time") String time);
}
