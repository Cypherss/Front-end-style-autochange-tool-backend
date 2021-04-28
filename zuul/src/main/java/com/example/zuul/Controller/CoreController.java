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
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author zcy
 * @version 1.0
 * @date 2021-04-25 14:52
 */
@RestController
@RequestMapping("/core")
public class CoreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreController.class);
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public ResponseVO sourceUpload(@RequestParam("userId")int userId, @RequestParam("file")MultipartFile file){
        try {
            Map<String, Object> res = restTemplate.postForObject("http://core/source/upload",null, Map.class);
            return ResponseVO.buildSuccess(res);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/target", method = RequestMethod.POST)
    public ResponseVO targetReplace(@RequestParam("userId")int userId, @RequestParam("source")String source, @RequestParam("file") MultipartFile file){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }

    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public ResponseVO getHtml(){
        try {
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return ResponseVO.buildFailure("error");
    }
}
