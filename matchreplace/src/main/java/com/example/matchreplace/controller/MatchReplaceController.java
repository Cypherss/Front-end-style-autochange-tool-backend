package com.example.matchreplace.controller;

import com.example.matchreplace.Global.GlobalVariable;
import com.example.matchreplace.MatchReplaceApplication;
import com.example.matchreplace.bl.match.Matcher;
import com.example.matchreplace.bl.replace.Replacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
* @RestController = @Controller + @ResponseBody, 用于将返回值解析成JSON，不能返回页面
* @Controller 则会使用试图解析器，可以返回页面进行跳转
* */
@RestController
@RequestMapping("/replaceAndMatch")
public class MatchReplaceController {
    /*
    * @PathVariable :
    * @RequestMapping(value="/{user}", method=RequestMethod.GET)
    public User getUser(@PathVariable Long user) {
        // ...
    }
    *
    * @RequestParam :
    * http://localhost:8080/springmvc/hello/101?param1=10&param2=20
    * public String getDetails(
    @RequestParam(value="param1", required=true) String param1,
        @RequestParam(value="param2", required=false) String param2){
        // ...
    }
    * */

    @Autowired
    Matcher matcher;

    @Autowired
    Replacer replacer;

    @RequestMapping(value = "/match", method = RequestMethod.GET)
    public Boolean match(@RequestParam("fileId1") String fileId1, @RequestParam("fileId2") String fileId2){
        System.out.println("start");
        matcher.matchControl(fileId1, fileId2);
        return true;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.GET)
    public String replace(){
        System.out.println(GlobalVariable.matchedBody);
        return replacer.replaceAndSave(GlobalVariable.matchedBody);
    }
}
