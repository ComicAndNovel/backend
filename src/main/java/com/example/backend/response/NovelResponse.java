package com.example.backend.response;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
class Author {
    private Integer id;
    private String name;
    private String originalName;
}

@Data
class Language {
    private Integer id;
    private String name;
    private String code;
}

@Data
class Novel {
    private Integer id;
    private String cover;
    private String name;
    private String originalName;
    private String  description;
    private  Double volume;
    private Integer page;
    private String releaseTime;
    private List<Author> author;
    private Language language;
    private Country country;
}

