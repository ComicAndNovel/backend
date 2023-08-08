package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.*;

@Data
@TableName("another")
public class Another {
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
}
