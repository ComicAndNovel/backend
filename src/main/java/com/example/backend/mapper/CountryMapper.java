package com.example.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Country;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface CountryMapper extends BaseMapper<Country> {
    // 所有的 curd 都已经完成了
}
