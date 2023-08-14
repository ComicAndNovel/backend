package com.example.backend.response;

import lombok.Data;

@Data
class Country {
    private Integer id;
    private String name;
}

@Data
public class AuthorResponse extends Author {
    private Country country;
}
