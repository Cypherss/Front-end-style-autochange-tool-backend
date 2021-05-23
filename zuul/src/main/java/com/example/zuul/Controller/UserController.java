package com.example.zuul.Controller;

import com.example.zuul.DTO.*;
import com.example.zuul.VO.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    final String HEADER = "http://user/user";
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseVO register(@RequestParam(value = "name") String name, @RequestParam(value = "password")String password){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/register?name={1}&password={2}",null,Integer.class,name,password));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseVO login(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/login?name={1}&password={2}",User.class,name,password));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/person/source", method = RequestMethod.GET)
    public ResponseVO getSourcesByUserId(@RequestParam(value = "userId")int userId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/person/source?userId={1}", SourceFile[].class,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/person/sortedsource", method = RequestMethod.GET)
    public ResponseVO getMostUsedSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/person/sortedsource?userId={1}",SourceFileWithCount[].class,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/person/sortedgroupsource", method = RequestMethod.GET)
    public ResponseVO getMostUsedGroupSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/person/sortedgroupsource?userId={1}",SourceFileWithCount[].class,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/user/group/source", method = RequestMethod.GET)
    public ResponseVO getGroupSourcesByUserId(@RequestParam(value = "userId")int userId){
        try{
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/user/group/source?userId={1}", SourceFile[].class,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/group/source", method = RequestMethod.GET)
    public ResponseVO getSourcesByGroupId(@RequestParam(value = "groupId")int groupId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/group/source?groupId={1}",SourceFile[].class,groupId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/sourceadd", method = RequestMethod.POST)
    public ResponseVO addUserSource(@RequestParam(value = "fileId")String fileId, @RequestParam(value = "userId") int userId, @RequestParam(value = "sourceName") String sourceName, @RequestParam(value = "uploadTime") String uploadTime, @RequestParam(value = "type") String type){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/sourceadd?fileId={1}&userId={2}&sourceName={3}&uploadTime={4}&type={5}",null,Boolean.class,fileId,userId,sourceName,uploadTime,type));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/sourceshare", method = RequestMethod.POST)
    public ResponseVO shareSource(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "sourceId") String sourceId){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/sourceshare?groupId={1}&sourceId={2}",null,Boolean.class,groupId,sourceId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseVO addGroup(@RequestParam(value = "name")String name,@RequestParam(value = "description") String description,@RequestParam("userId")int userId){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/group?name={1}&description={2}&userId={3}",null,Integer.class,name,description,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/groupmember", method = RequestMethod.POST)
    public ResponseVO addGroupMember(@RequestParam(value = "groupId")int groupId,@RequestParam(value = "userId") int userId){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/groupmember?groupId={1}&userId={2}",null,Boolean.class,groupId,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/usergroups", method = RequestMethod.GET)
    public ResponseVO getUserGroup(@RequestParam(value = "userId")int userId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/usergroups?userId={1}", Group[].class,userId));
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/groupusers", method = RequestMethod.GET)
    public ResponseVO getGroupUser(@RequestParam(value = "groupId")int groupId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/groupusers?groupId={1}", User[].class,groupId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public ResponseVO addRecord(@RequestParam("userId")int userId,@RequestParam("sourceId")String sourceId,@RequestParam("targetId")String targetId,@RequestParam("time")String time){
        try {
            return ResponseVO.buildSuccess(restTemplate.postForObject(HEADER+"/record?userId={1}&sourceId={2}&targetId={3}&time={4}",null,Boolean.class,userId,sourceId,targetId,time));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseVO.buildFailure("error");
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseVO getHistory(@RequestParam("userId")int userId){
        try {
            return ResponseVO.buildSuccess(restTemplate.getForObject(HEADER+"/history?userId={1}", RecordDTO[].class,userId));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseVO.buildFailure("error");
        }
    }
}
