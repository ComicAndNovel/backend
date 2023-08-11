package com.example.backend.controller;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Novel;
import com.example.backend.mapper.NovelMapper;
import lombok.Data;
import lombok.Setter;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
// 列表的请求参数
@Data
class NovelListQuery {
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
public class NovelController {
  @Autowired
  private NovelMapper novelMapper;

//    @RequestMapping
    @RequestMapping(value = "/api/novel/novelList", method = RequestMethod.POST)
    public RestBean<List<Novel>> novelList(@RequestBody NovelListQuery query) throws IOException {
        System.out.println("开始请求");
        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");

        Page<Novel> list = novelMapper.selectPage(Page.of(query.getPage(), query.getPageSize()), queryWrapper);
//        List<Another> list = anotherMapper.selectList(queryWrapper);
//            list.getTotal();
        System.out.println(list.getRecords());
        System.out.println(list);

        return RestBean.success(list.getRecords(), query.getPage(), (int) list.getTotal(), query.getPageSize());
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
          novel.setCover(query.getCover());
          novel.setName(query.getName());
          novel.setOriginalName(query.getOriginalName());
          if (query.getPage() != null) {
              novel.setPage(query.getPage());
          }
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
