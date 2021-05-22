package com.example.demo.Controller;

import com.example.demo.DTO.SourceFileWithCount;
import com.example.demo.PO.Group;
import com.example.demo.PO.SourceFile;
import com.example.demo.PO.User;
import com.example.demo.Service.UserService;
import com.example.demo.DTO.RecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 17:05
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public int register(@RequestParam(value = "name") String name, @RequestParam(value = "password")String password){
        try{
            return userService.register(name,password);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return -1;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public User login(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password){
        try{
            return userService.login(name, password);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/person/source", method = RequestMethod.GET)
    public List<SourceFile> getSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return userService.getUserSources(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/person/sortedsource", method = RequestMethod.GET)
    public List<SourceFileWithCount> getMostUsedSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return userService.getMostUsedUserSources(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/person/sortedgroupsource", method = RequestMethod.GET)
    public List<SourceFileWithCount> getMostUsedGroupSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return userService.getMostUsedUserGroupSources(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/user/group/source", method = RequestMethod.GET)
    public List<SourceFile> getGroupSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return userService.getGroupSourceFileByUserId(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/group/source", method = RequestMethod.GET)
    public List<SourceFile> getSourcesByGroupId(@RequestParam(value = "groupId")int groupId){
        try{
            return userService.getGroupSources(groupId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/sourceadd", method = RequestMethod.POST)
    public boolean addUserSource(@RequestParam(value = "fileId")String fileId,@RequestParam(value = "userId") int userId,@RequestParam(value = "sourceName") String sourceName,@RequestParam(value = "uploadTime") String uploadTime,@RequestParam(value = "type") String type){
        try{
            return userService.addUserSource(fileId, userId,sourceName,uploadTime,type);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = "/sourceshare", method = RequestMethod.POST)
    public boolean shareSource(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "sourceId") String sourceId){
        try{
            return userService.shareSource(groupId, sourceId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public Group addGroup(@RequestParam(value = "name")String name,@RequestParam(value = "description") String description,@RequestParam("userId")int userId){
        try{
            Group group = userService.addGroup(name, description);
            userService.addGroupMember(group.getId(), userId);
            return group;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/groupmember", method = RequestMethod.POST)
    public boolean addGroupMember(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "userId") int userId){
        try{
            return userService.addGroupMember(groupId, userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = "/usergroups", method = RequestMethod.GET)
    public List<Group> getUserGroup(@RequestParam(value = "userId")int userId){

        try{
            return userService.getUserGroups(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/groupusers", method = RequestMethod.GET)
    public List<User> getGroupUser(@RequestParam(value = "groupId")int groupId){
        try{
            return userService.getGroupUsers(groupId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public boolean addRecord(@RequestParam("userId")int userId,@RequestParam("sourceId")String sourceId,@RequestParam("targetId")String targetId,@RequestParam("time")String time){
        try {
            return userService.addRecord(userId,sourceId,targetId,time);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<RecordDTO> getHistory(@RequestParam("userId")int userId){
        try {
            return userService.getUserHistory(userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return null;
        }
    }

}
  