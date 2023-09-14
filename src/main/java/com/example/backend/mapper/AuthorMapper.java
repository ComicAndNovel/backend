package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backend.entity.Author;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface AuthorMapper extends BaseMapper<Author> {
    // 所有的 curd 都已经完成了
    IPage<Object> authorList(Object query, IPage<Object> page);
    Object authorDetail(Integer id);
}
