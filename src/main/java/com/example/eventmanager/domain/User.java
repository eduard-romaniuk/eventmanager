package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@ToString
public class User {
    @JsonView(UserView.ShortView.class)
    private Long id;
    @JsonView(UserView.ShortView.class)
    private String username;
    @JsonView(UserView.ShortView.class)
    private String password;
    private String passwordConfirm;
    private String name;
    private String surName;
    private String email;
    private LocalDate birth;
    private String phone;
    private boolean sex;
    private boolean isActive;
    private Image image;
    private LocalDate regDate;
    private Set<User> friends;
    private WishList wishList;
    private Set<Event> events;
    private Settings settings;
    private String confLink;

    public User copy() {

        return new User(id, username, password, passwordConfirm, name, surName, email, birth, phone, sex, isActive, image, regDate, friends, wishList, events, settings, confLink);
    }



    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public User(Long id, String username, String password, String passwordConfirm, String name, String surName, String email, LocalDate birth, String phone, boolean sex, boolean isActive, Image image, LocalDate regDate, Set<User> friends, WishList wishList, Set<Event> events, Settings settings, String confLink) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
        this.isActive = isActive;
        this.image = image;
        this.regDate = regDate;
        this.friends = friends;
        this.wishList = wishList;
        this.events = events;
        this.settings = settings;
        this.confLink = confLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getConfLink() {
        return confLink;
    }

    public void setConfLink(String confLink) {
        this.confLink = confLink;
    }
}
