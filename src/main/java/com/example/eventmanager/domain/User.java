package com.example.eventmanager.domain;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean sex;
    private String name;
    private String surName;
    private String token = "";
    private Boolean verified = false;
}
