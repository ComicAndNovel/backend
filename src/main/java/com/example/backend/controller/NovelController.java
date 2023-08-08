package com.example.backend.controller;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Novel;
import com.example.backend.mapper.NovelMapper;
import lombok.Data;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.RestBean;


// 添加保存 小说的请求参数
@Data
class SaveNovelQuery {
    private String cover;
    private String name;
    private String originalName;
    private String desc;
    private Integer volume;
    private Integer page;
    private String releaseTime;
}

@RestController
public class NovelController {
  @Autowired
  private NovelMapper novelMapper;

    @PostMapping("/api/novel/novelList")
    public RestBean<List<Novel>> novelList() throws IOException {
        System.out.println("开始请求");
        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();


        Page<Novel> list = novelMapper.selectPage(Page.of(1, 10), queryWrapper);
//        List<Another> list = anotherMapper.selectList(queryWrapper);
//            list.getTotal();
        System.out.println(list.getRecords());
        long total = list.getTotal();
        System.out.println(list);

        return RestBean.success(list.getRecords(),1, (int) list.getTotal(), 2);
    }

  @PostMapping("/api/novel/save")
   public RestBean<Null> save(@RequestBody SaveNovelQuery query) {
      if (query.getName() == null) return  RestBean.error(-1, "小说名称不能为空");
      if (query.getOriginalName() == null) return RestBean.error(-1, "小说原名不能为空");
      if (query.getVolume() == null) return  RestBean.error(-1, "卷数不能为空");

//      判断当前小说 和卷数是否已经存在，如果不存在则添加
      QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
      System.out.println(query);

      queryWrapper.lambda()
          .eq(Novel::getOriginalName, query.getOriginalName())
          .eq(Novel::getVolume, query.getVolume());

      Novel find = novelMapper.selectOne(queryWrapper);
      System.out.println("是否存在" + find);
      if (find == null) {
          Novel novel = new Novel();
          System.out.println("+++++");
          novel.setCover(query.getCover());
          novel.setName(query.getName());
          novel.setOriginalName(query.getOriginalName());
          novel.setPage(query.getPage());
          novel.setDesc(query.getDesc());
          novel.setVolume(query.getVolume());
          novel.setReleaseTime(query.getReleaseTime());

          novelMapper.insert(novel);
//          System.out.println("插入成功" + f);
          return RestBean.success(null, "保存成功");
      } else {
          return RestBean.error(0, "该卷已存在");
      }
  }
}
