package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComicController {
    @GetMapping("/api/comicList")
    public String comicList() {
        return "hello world";
    }
}
