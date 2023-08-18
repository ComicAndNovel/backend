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

    @TableField("original_name")
    String originalName;

    @TableField("description")
    String description;

    @TableField("volume")
    double volume;

    @TableField("page")
    int page;

    @TableField("release_time")
    String releaseTime;

    @TableField("country_id")
    int countryId;

    @TableField("language_id")
    int languageId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;

    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;
}
