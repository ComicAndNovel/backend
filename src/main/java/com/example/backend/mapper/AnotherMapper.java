package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Another;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;


@Mapper
public interface AnotherMapper extends BaseMapper<Another> {
    // 所有的 curd 都已经完成了
}
