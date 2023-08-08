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

    @JsonIgnore
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    Date createTime;

    @JsonIgnore
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
