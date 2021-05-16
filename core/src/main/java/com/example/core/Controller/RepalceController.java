package com.example.core.Controller;


import com.example.core.Service.UtilService;
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
 * @date 2021-04-25 14:24
 */
@RestController
@RequestMapping("/replace")
public class RepalceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepalceController.class);

    final String STORAGE_HEADER = "http://objectstorage/minio";
    @Autowired
    UtilService utilService;
    @Autowired
    RestTemplate restTemplate;

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

    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public String generateHtml(@RequestParam("fileId") String fileId){
        String[] temp = fileId.split("\\.");
        String type = temp[temp.length-1];
        String json = restTemplate.getForObject(STORAGE_HEADER+"/get?objectName={1}&type={2}",String.class,fileId,type);
        return utilService.generateHTML(json);
    }
}
