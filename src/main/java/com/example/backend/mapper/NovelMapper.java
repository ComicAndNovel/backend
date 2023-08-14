package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.example.backend.controller.NovelListQuery;
//import com.example.backend.controller.SaveNovelQuery;
//import com.example.backend.controller.NovelListQuery;
import com.example.backend.entity.Novel;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface NovelMapper extends MPJBaseMapper<Novel> {
    // 所有的 curd 都已经完成了
    IPage<Object> novelList(IPage<Object> page, Object query);
}
