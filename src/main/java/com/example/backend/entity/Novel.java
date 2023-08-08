package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.*;
import lombok.Data;

@Data
@TableName("novel")
public class Novel {
    @TableId(type = IdType.AUTO)
    int id;

    @TableField("cover")
    String cover;

    @TableField("name")
    String name;
    
    @TableField("originalName")
    String originalName;

    @TableField("'desc'")
    String desc;

    @TableField("volume")
    int volume;

    @TableField("page")
    int page;

    @TableField("releaseTime")
    String releaseTime;


    @TableField(value = "createTime", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
