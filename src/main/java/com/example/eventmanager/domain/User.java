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
    @JsonView(EventView.FullView.class)
    private List<Event> events;
    // private Settings settings;
    @JsonView(EventView.FullView.class)
    private String token = "";
    @JsonView(EventView.FullView.class)
    private Boolean verified = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
