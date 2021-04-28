package com.example.zuul.Controller;

import com.example.zuul.VO.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-28 16:09
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreController.class);
    @Autowired
    RestTemplate restTemplate;

    final String HEADER = "http://user";
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseVO register(@RequestParam(value = "name") String name, @RequestParam(value = "password")String password){
        try {
            //return ResponseVO.buildSuccess(restTemplate.postForEntity(HEADER + "/user/register",));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseVO login(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/person/source", method = RequestMethod.GET)
    public ResponseVO getSourcesByUserId(@RequestParam(value = "userId")int userId){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/group/source", method = RequestMethod.GET)
    public ResponseVO getSourcesByGroupId(@RequestParam(value = "groupId")int groupId){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/sourceadd", method = RequestMethod.POST)
    public ResponseVO addUserSource(@RequestParam(value = "fileId")String fileId, @RequestParam(value = "userId") int userId, @RequestParam(value = "sourceName") String sourceName, @RequestParam(value = "uploadTime") Timestamp uploadTime, @RequestParam(value = "type") String type){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/sourceshare", method = RequestMethod.POST)
    public ResponseVO shareSource(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "sourceName") String sourceName){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseVO addGroup(@RequestParam(value = "name")String name,@RequestParam(value = "description") String description){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/groupmember", method = RequestMethod.POST)
    public ResponseVO addGroupMember(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "userId") int userId){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/usergroups", method = RequestMethod.GET)
    public ResponseVO getUserGroup(@RequestParam(value = "userId")int userId){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/groupusers", method = RequestMethod.GET)
    public ResponseVO getGroupUser(@RequestParam(value = "groupId")int groupId){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }
}
