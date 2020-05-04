package com.sec.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SecController {

    @Autowired
    private DataInputFileRow fileHandler;
    private SecurityAppResponse responseObject;

    @PostMapping("/upload")
    public SecurityAppResponse processUpload(@RequestParam("fileKey") MultipartFile file){
        fileHandler.process(file);
        fileHandler.parse();
        responseObject = new SecurityAppResponse();
        responseObject.setFileNameCountMap(fileHandler.getTopFileNames());
        responseObject.setUserNameCountMap(fileHandler.getTopUserNames());
        responseObject.setDayActivityMap(fileHandler.getDateActivitCountMap());
       return responseObject;//fileHandler.getTopFileNames().toString();
    }

    @GetMapping("/details")
    public String getDetails(){
        fileHandler.parse();
        return fileHandler.getTopFileNames().toString();
    }

    @GetMapping("/topfiles")
    public String getTopFiles(){
        fileHandler.parse();
        return fileHandler.getTopFileNames().toString();
    }

    @GetMapping("/topusernames")
    public String getTopUserName(){
       fileHandler.parse();
        return fileHandler.getTopFileNames().toString();
    }

    @GetMapping("/test")
    public Map<String, String> sayHello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        return map;
    }
}
