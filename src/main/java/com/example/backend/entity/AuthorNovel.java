package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("author_novel")
public class AuthorNovel {
    @TableField("authorId")
    String authorId;

    @TableField("novelId")
    String novelId;
}
