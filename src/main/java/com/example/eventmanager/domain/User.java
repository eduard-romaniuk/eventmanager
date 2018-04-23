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
    private String login;
    @JsonView(UserView.FullView.class)
    private String password;
    private String passwordConfirm;
    @JsonView(UserView.ShortView.class)
    private String name;
    @JsonView(UserView.ShortView.class)
    private String surName;
    @JsonView(UserView.ShortView.class)
    private String email;
    @JsonView(UserView.ShortView.class)
    private LocalDate birth;
    @JsonView(UserView.ShortView.class)
    private String phone;
    @JsonView(UserView.ShortView.class)
    private boolean sex;
    @JsonView(UserView.ShortView.class)
    private boolean active;
    @JsonView(UserView.ShortView.class)
    private Image image;
    @JsonView(UserView.ShortView.class)
    private LocalDate regDate;
    @JsonView(UserView.FullView.class)
    private Set<User> friends;
    @JsonView(UserView.FullView.class)
    private WishList wishList;
    @JsonView(UserView.FullView.class)
    private Set<Event> events;
    @JsonView(UserView.FullView.class)
    private Settings settings;
    @JsonView(UserView.FullView.class)
    private String confLink;

    public User copy() {
        return new User(id, login, password, passwordConfirm, name, surName,
                email, birth, phone, sex, active, image,
                regDate, friends, wishList, events, settings, confLink);
    }

    public User() {
    }

    public User(Long id, String password) {
        this.id = id;
        this.password = password;
    }
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(Long id, String login, String password, String name) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public User(String login, String password, String passwordConfirm) {
        this.login = login;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public User(Long id, String login, String password, String passwordConfirm,
                String name, String surName, String email, LocalDate birth,
                String phone, boolean sex, boolean active, Image image,
                LocalDate regDate, Set<User> friends, WishList wishList,
                Set<Event> events, Settings settings, String confLink) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
        this.active = active;
        this.image = image;
        this.regDate = regDate;
        this.friends = friends;
        this.wishList = wishList;
        this.events = events;
        this.settings = settings;
        this.confLink = confLink;
    }

    public User(Long id, String login, String password, String name,
                String surName, String email, LocalDate birth, String phone,
                boolean sex, boolean active, LocalDate regDate, String confLink){
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
        this.active=active;
        this.regDate=regDate;
        this.confLink=confLink;
    }

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

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
