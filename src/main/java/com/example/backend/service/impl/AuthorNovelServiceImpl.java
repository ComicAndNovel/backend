package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.AuthorNovel;
import com.example.backend.mapper.AuthorNovelMapper;
import com.example.backend.service.AuthorNovelService;
import org.springframework.stereotype.Service;

@Service
public class AuthorNovelServiceImpl extends ServiceImpl<AuthorNovelMapper, AuthorNovel> implements AuthorNovelService {

}
