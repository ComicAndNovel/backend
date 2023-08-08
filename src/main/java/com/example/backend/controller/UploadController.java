package com.example.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class UploadController {
    @PostMapping("/api/upload")
    public String upload (@RequestParam("file") MultipartFile file) throws IOException {
//        File f = new File(file);
        System.out.println("文件名：" + file.getOriginalFilename());
        System.out.println("文件大小：" + file.getSize());
        System.out.println("文件内容：" + file.getBytes());
        System.out.println(file);
        return "hello world";
    }
}
