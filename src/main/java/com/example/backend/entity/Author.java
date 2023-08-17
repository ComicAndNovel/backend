package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.*;

@Data
@TableName("author")
public class Author {
    @TableId(type = IdType.AUTO)
    int id;

    @TableField("name")
    String name;
    
    @TableField("originalName")
    String originalName;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;

    @TableField("countryId")
    int countryId;

    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}
