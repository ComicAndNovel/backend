package com.example.backend.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Another;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.AnotherMapper;
import lombok.Data;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Data
class anotherListQuery {
    private Integer page;
    private Integer pageSize;
    private String name;
    private String originalName;

    public void setPage(Integer page) {
        this.page = page != null ? page : 1;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize != null ? pageSize : 10;
    }
}

@RestController
public class AnotherController {
    @Autowired
    private AnotherMapper anotherMapper;

    @PostMapping("/api/another/anotherList")
    public RestBean<List<Another>> anotherList(@RequestBody anotherListQuery query) throws IOException {
        System.out.println("开始请求");
        QueryWrapper<Another> queryWrapper = new QueryWrapper<>();

//        queryWrapper.eq("id", 1);
        Page<Another> list = anotherMapper.selectPage(Page.of(query.getPage(), query.getPageSize()), queryWrapper);
//        List<Another> list = anotherMapper.selectList(queryWrapper);
//            list.getTotal();
        System.out.println(list.getRecords());


        return RestBean.success(list.getRecords(),query.getPage(), list.getTotal(), query.getPageSize());
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
