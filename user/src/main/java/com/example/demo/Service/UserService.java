package com.example.demo.Service;

import com.example.demo.PO.Group;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;

import java.util.List;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:05
 */
public interface UserService {
    public int register(String name, String password);

    public User login(String name, String password);

    public List<SourceFile> getUserSources(int userId);

    public boolean addUserSource(String fileId, int userId, String sourceName, String uploadTime, String type);

    public boolean shareSource(int groupId, String sourceName);

    public int addGroup(String name, String description);

    public boolean addGroupMember(int groupId, int userId);

    public List<Group> getUserGroups(int userId);

    public List<User> getGroupUsers(int groupId);

    public List<SourceFile> getGroupSources(int groupId);

}