package com.example.backend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.AuthorNovel;
import com.example.backend.entity.Novel;
import com.example.backend.mapper.NovelMapper;
import com.example.backend.service.AuthorNovelService;
import com.example.backend.service.impl.AuthorNovelServiceImpl;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.RestBean;


// 添加保存 小说的请求参数
@Data
class SaveNovelQuery {
  private Integer id;
  private String cover;
  private String name;
  private String originalName;
  private String desc;
  private Integer volume;
  private Integer page;
  private String releaseTime;
  private Integer countryId;
  private Integer languageId;
  private List<Integer> authorId;
}

// 列表的请求参数
@Data
class NovelListQuery {
  private Integer page;
  private Integer pageSize;
  private String name;
  private String originalName;
  private String authorName;
}

@RestController
public class NovelController {
  @Autowired
  private NovelMapper novelMapper;

  @Resource
  private AuthorNovelService authorNovelService;

  //    @RequestMapping
  @RequestMapping(value = "/api/novel/novelList", method = RequestMethod.POST)
  public RestBean<List<Object>> novelList (@RequestBody NovelListQuery query) throws IOException {
    System.out.println("开始请求");

    QueryWrapper wrapper = new QueryWrapper<>();
    wrapper.select("DISTINCT id");
    Page<Object> page = new Page<>(query.getPage(), query.getPageSize());

    IPage<Object> list = novelMapper.novelList(page, query);

    return RestBean.success(list.getRecords(), query.getPage(), (int) list.getTotal(), query.getPageSize());
  }

  @RequestMapping(value = "/api/novel/novelDetail", method = RequestMethod.GET)
  public RestBean<Object> novelDetail (@Param("id") Integer id) {
    if (id == null) return RestBean.error(-1, "小说ID不能为空");

    Object data = novelMapper.novelDetail(id);

    return RestBean.success(data, "获取成功");
  }

  @Transactional
  @PostMapping("/api/novel/save")
  public RestBean<Null> save (@RequestBody SaveNovelQuery query) {
    if (query.getName() == null) return RestBean.error(-1, "小说名称不能为空");
    if (query.getOriginalName() == null) return RestBean.error(-1, "小说原名不能为空");
    if (query.getVolume() == null) return RestBean.error(-1, "卷数不能为空");
    if (query.getAuthorId().size() == 0) return RestBean.error(-1, "作者不能为空");

//      判断当前小说 和卷数是否已经存在，如果不存在则添加
    QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
    System.out.println(query);

    queryWrapper.lambda()
      .eq(Novel::getOriginalName, query.getOriginalName())
      .eq(Novel::getVolume, query.getVolume());

    Novel find = novelMapper.selectOne(queryWrapper);

//      如果数据存在并且原名卷数相同
    if (find != null && query.getOriginalName() == find.getOriginalName() && query.getVolume() == find.getVolume()) {
      // 如果有 id 并且 原名不相同，并且数据库也存在
      return RestBean.error(0, "该卷已存在");
    }

    Novel novel = new Novel();
    if (query.getId() != null) novel.setId(query.getId());
    novel.setCover(query.getCover());
    novel.setName(query.getName());
    novel.setOriginalName(query.getOriginalName());
    if (query.getPage() != null) novel.setPage(query.getPage());
    if (query.getCountryId() != null) novel.setCountryId(query.getCountryId());
    if (query.getLanguageId() != null) novel.setLanguageId(query.getLanguageId());
    novel.setDescription(query.getDesc());
    novel.setVolume(query.getVolume());
    novel.setReleaseTime(query.getReleaseTime());
    novel.setUpdateTime(new Date());

    // 添加小说和作者的关联数据
    List<AuthorNovel> list = new ArrayList<>();
    for (Integer id : query.getAuthorId()) {

      AuthorNovel authorNovel = new AuthorNovel();
      authorNovel.setNovelId(novel.getId());
      authorNovel.setAuthorId(id);

      list.add(authorNovel);
    }

    if (query.getId() == null) {
      // 创建
      novelMapper.insert(novel);
      authorNovelService.saveBatch(list);
    } else {
      // 修改
      QueryWrapper updateQueryWrapper = new QueryWrapper();
      updateQueryWrapper.eq("novel_id", novel.getId());

      novelMapper.updateById(novel);
      authorNovelService.remove(updateQueryWrapper);
      authorNovelService.saveBatch(list);
    }

    return RestBean.success(null, "保存成功");
  }

  @Transactional
  @DeleteMapping("/api/novel/remove")
  public RestBean<Null> remove (@RequestParam Integer id) {
    if(id == null) return RestBean.error(-1, "参数错误");

    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("novel_id", id);

    authorNovelService.remove(queryWrapper);
    novelMapper.deleteById(id);

    return RestBean.success(null, "删除成功");
  }
}
