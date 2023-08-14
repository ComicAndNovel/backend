package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("language")
public class Language {
    @TableId(type = IdType.AUTO)
    int id;

    @TableField("name")
    String name;

    @TableField("code")
    String code;
    
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
