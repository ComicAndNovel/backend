package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Country;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.CountryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {
    @Autowired
    private CountryMapper countryMapper;

    @GetMapping("/api/country/countryList")
    public RestBean<List<Country>> countryList() {
        QueryWrapper<Country> queryWrapper = new QueryWrapper<>();
        
       List<Country> list = countryMapper.selectList(queryWrapper);
        System.out.println(list);
       return RestBean.success(list, "获取成功");
    }
}
