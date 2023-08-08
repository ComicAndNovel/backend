package com.example.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.backend.entity.Language;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LanauageMapper extends BaseMapper<Language> {
    // 所有的 curd 都已经完成了
}