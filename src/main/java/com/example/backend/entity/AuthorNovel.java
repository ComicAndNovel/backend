package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("author_novel")
public class AuthorNovel {
    @TableField("authorId")
    Integer authorId;

    @TableField("novelId")
    Integer novelId;
}
