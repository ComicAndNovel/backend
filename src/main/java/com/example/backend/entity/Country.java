package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
@TableName("country")
public class Country {
    @TableId(type = IdType.AUTO)
    int id;

    @TableField("name")
    String name;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
