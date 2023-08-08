package com.example.backend.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Another;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.AnotherMapper;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
public class AnotherController {
    @Autowired
    private AnotherMapper anotherMapper;


    @PostMapping("/api/another/anotherList")
    public RestBean<List<Another>> anotherList(@RequestParam  Map<String, Object> query) throws IOException {
        System.out.println("开始请求");
        QueryWrapper<Another> queryWrapper = new QueryWrapper<>();

//        queryWrapper.eq("id", 1);
        Page<Another> list = anotherMapper.selectPage(Page.of(1, 10), queryWrapper);
//        List<Another> list = anotherMapper.selectList(queryWrapper);
//            list.getTotal();
        System.out.println(list.getRecords());
        long total = list.getTotal();

        return RestBean.success(list.getRecords(),1, (int) list.getTotal(), 2);
    }

    @PostMapping("/api/another/save")
    public RestBean<Null> save(@RequestBody  Map<String, Object> query) {
//        Another another = new Another();
//        another.setName((String) query.get("name"));
//        another.setName((String) query.get("originalName"));
//        int result = anotherMapper.insert(another);
//        System.out.println(result);
        return RestBean.success(null, "保存成功");
    }
}
