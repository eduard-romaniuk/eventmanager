package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;


@Data
public class Member {       //member for folder
    @JsonView(MemberView.ShortView.class)
    private Long id;
    @JsonView(MemberView.ShortView.class)
    private String login;
    @JsonView(MemberView.ShortView.class)
    private String name;
    @JsonView(MemberView.ShortView.class)
    private String surName;
    @JsonView(MemberView.FullView.class)
    private String image;
    @JsonView(MemberView.FullView.class)
    private String email;
    @JsonView(MemberView.FullView.class)
    private LocalDate birth;
    @JsonView(MemberView.FullView.class)
    private String phone;
    @JsonView(MemberView.FullView.class)
    private Boolean sex;
    @JsonView(MemberView.ShortView.class)
    private Boolean isMember;

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

    public Boolean isMember() {
        return isMember;
    }

    public void setIsMember(Boolean member) {
        isMember = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member user = (Member) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
