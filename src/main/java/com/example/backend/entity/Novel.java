package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

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

    @TableField("`desc`")
    String desc;

    @TableField("volume")
    double volume;

    @TableField("page")
    int page;

    @TableField("releaseTime")
    String releaseTime;

    @TableField("countryId")
    int countryId;

    @TableField("languageId")
    int languageId;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;

    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}
