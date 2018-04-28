package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class User {
    @JsonView(UserView.ShortView.class)
    private Long id;
    @JsonView(UserView.ShortView.class)
    private String login;
    @JsonView(UserView.ShortView.class)
    private String name;
    @JsonView(UserView.ShortView.class)
    private String surName;
    @JsonView(UserView.ShortView.class)
    private String image;

    @JsonView(UserView.FullView.class)
    private String email;
    @JsonView(UserView.FullView.class)
    private LocalDate birth;
    @JsonView(UserView.FullView.class)
    private String phone;
    @JsonView(UserView.FullView.class)
    private Boolean sex;
    @JsonView(UserView.FullView.class)
    private LocalDate regDate;
    @JsonView(UserView.FullView.class)
    private List<User> friends;

    //TODO Delete password from FullView
    @JsonView(UserView.FullView.class)
    private String password;
    //private WishList wishList;
    private List<Event> events;
    // private Settings settings;
    private String token = "";
    private Boolean verified = false;
}
