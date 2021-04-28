package com.example.core.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 14:24
 */
@RestController
@RequestMapping("/replace")
public class RepalceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepalceController.class);

    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String match(@RequestParam("fileId") String fileId){
        return "success";
    }

    @RequestMapping(value = "/optimize", method = RequestMethod.POST)
    public String optimize(@RequestParam("fileId") String fileId){
        return "success";
    }

    @RequestMapping(value = "/assessment", method = RequestMethod.POST)
    public String assessment(@RequestParam("fileId") String fileId){
        return "success";
    }
}
