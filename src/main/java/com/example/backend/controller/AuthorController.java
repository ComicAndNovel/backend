package com.example.backend.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Author;
import com.example.backend.entity.Novel;
import com.example.backend.entity.RestBean;
import com.example.backend.mapper.AuthorMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
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
}

@Data
class SaveAuthorQuery {
    private Integer id;
    private String name;
    private String originalName;
    private Integer countryId;
}

@RestController
public class AuthorController {
    @Autowired
    private AuthorMapper authorMapper;

    @PostMapping("/api/author/authorList")
    public RestBean<List<Object>> authorList(@RequestBody authorListQuery query) throws IOException {

      Page<Object> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<Object> list = authorMapper.authorList(query, page);

        return RestBean.success(list.getRecords(), query.getPage(), list.getTotal(), query.getPageSize());
    }

    @RequestMapping(value = "/api/author/authorDetail", method = RequestMethod.GET)
    public RestBean<Object> novelDetail (@Param("id") Integer id) {
        if (id == null) return RestBean.error(-1, "作者ID不能为空");

        Object data = authorMapper.authorDetail(id);

        return RestBean.success(data, "获取成功");
    }

    @PostMapping("/api/author/save")
    public RestBean<Null> save(@RequestBody  SaveAuthorQuery query) {
        if (query.getName() == null) return RestBean.error(-1, "作者名称不能为空");
        if (query.getOriginalName() == null) return RestBean.error(-1, "作者原名不能为空");

        QueryWrapper<Author> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
          .eq(Author::getOriginalName, query.getOriginalName());

        Author find = authorMapper.selectOne(queryWrapper);

        //  如果数据存在并且原名卷数相同
        if (find != null && query.getOriginalName() == find.getOriginalName()) {
            // 如果有 id 并且 原名不相同，并且数据库也存在
            return RestBean.error(0, "该作者已存在");
        }
        Author author = new Author();
        author.setName(query.getName());
        author.setOriginalName(query.getOriginalName());
        if (query.getCountryId() != null) author.setCountryId(query.getCountryId());

        if (query.getId() == null) {
            authorMapper.insert(author);
        } else {
            author.setId(query.getId());
            authorMapper.updateById(author);
        }
        return RestBean.success(null, "保存成功");
    }
    @DeleteMapping("/api/author/remove")
    public RestBean<Null> remove (@RequestParam Integer id) {
        if(id == null) return RestBean.error(-1, "参数错误");

        authorMapper.deleteById(id);

        return RestBean.success(null, "删除成功");
    }
}
