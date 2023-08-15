package com.example.backend.controller;

import com.example.backend.entity.Language;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.LanguageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LanguageController {
    @Autowired
    private LanguageMapper languageMaper;

    @GetMapping("/api/language/languageList")
    public RestBean<List<Language>> languageList() {
        List<Language> list = languageMaper.selectList(null);

        return RestBean.success(list, "获取成功");
    }
}
