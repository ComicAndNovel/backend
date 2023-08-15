package com.example.backend.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.AuthorMapper;
import lombok.Data;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Data
class authorListQuery {
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
public class AuthorController {
    @Autowired
    private AuthorMapper authorMapper;

    @PostMapping("/api/author/authorList")
    public RestBean<List<Object>> authorList(@RequestBody authorListQuery query) throws IOException {

      Page<Object> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<Object> list = authorMapper.authorList(page, query);

        return RestBean.success(list.getRecords(), query.getPage(), (int) list.getTotal(), query.getPageSize());


    }

    @PostMapping("/api/author/save")
    public RestBean<Null> save(@RequestBody  Map<String, Object> query) {
        return RestBean.success(null, "保存成功");
    }
}
