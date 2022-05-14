package com.example.videoApplication.controllers;
import com.example.videoApplication.model.User;
import com.example.videoApplication.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class Controller {
    @Autowired
    AppService appService;

    @Autowired
    User user;

    @GetMapping("/hello")
    public String hello(@RequestParam("userId") String userId){
        return "Hello World "+userId;
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity createUser(@RequestBody User user) {
        appService.saveOrUpdateUserInfo(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/uploadVideo")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity handleUploadForm(@RequestParam("videoFile") MultipartFile multipart) {
        try {
            appService.saveVideo(multipart);
        } catch (Exception ex) {
            System.out.println("Error uploading file: " + ex.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
