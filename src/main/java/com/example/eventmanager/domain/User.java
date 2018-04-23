package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.ToString;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;

@ToString
public class User {

    @JsonView(EventView.FullView.class)
    private Long id;
    @JsonView(EventView.FullView.class)
    private String username;
    private String password;
    private String passwordConfirm;
    @JsonView(EventView.FullView.class)
    private String name;
    @JsonView(EventView.FullView.class)
    private String surName;
    private ZoneId timeZone;
    private LocalDate birth;
    private String phone;
    private boolean gender;
    private boolean verified;
    private long imageId;
    private LinkedList<Long> friends;
    private LinkedList<Long> wishLists;
    private LinkedList<Long> events;
    private long settingsId;

    public User copy() {

        return new User(id, username, password, passwordConfirm, name, surName, timeZone, birth, phone, gender, verified, imageId, (LinkedList<Long>)friends.clone(), (LinkedList<Long>)wishLists.clone(), (LinkedList<Long>)events.clone(), settingsId);
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

    public User(Long id, String username, String password, String passwordConfirm, String name, String surName, ZoneId timeZone, LocalDate birth, String phone, boolean gender, boolean verified, long imageId, LinkedList<Long> friends, LinkedList<Long> wishLists, LinkedList<Long> events, long settingsId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.surName = surName;
        this.timeZone = timeZone;
        this.birth = birth;
        this.phone = phone;
        this.gender = gender;
        this.verified = verified;
        this.imageId = imageId;
        this.friends = friends;
        this.wishLists = wishLists;
        this.events = events;
        this.settingsId = settingsId;
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

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
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

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public LinkedList<Long> getFriends() {
        return friends;
    }

    public void setFriends(LinkedList<Long> friends) {
        this.friends = friends;
    }

    public LinkedList<Long> getWishLists() {
        return wishLists;
    }

    public void setWishLists(LinkedList<Long> wishLists) {
        this.wishLists = wishLists;
    }

    public LinkedList<Long> getEvents() {
        return events;
    }

    public void setEvents(LinkedList<Long> events) {
        this.events = events;
    }

    public long getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(long settingsId) {
        this.settingsId = settingsId;
    }
}
