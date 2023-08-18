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
    
    @TableField("original_name")
    String originalName;

    @TableField("country_id")
    int countryId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;

    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}
