package com.example.eventmanager.domain;

import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
public class Event {

    private Long id;
    private User creator;
    //private Folder folder;
    private String name;
    private long descriptionId;
    private String plase;
    private LocalDateTime timeLineStart;
    private LocalDateTime timeLineFinish;
    private Integer period;
   // private WishList wishList;
    private Long imageId;
    private boolean isSent;
    private boolean isPrivate;
    private List<User> participants;
  //  private List<Chat> chats;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(long descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getPlase() {
        return plase;
    }

    public void setPlase(String plase) {
        this.plase = plase;
    }

    public LocalDateTime getTimeLineStart() {
        return timeLineStart;
    }

    public void setTimeLineStart(LocalDateTime timeLineStart) {
        this.timeLineStart = timeLineStart;
    }

    public LocalDateTime getTimeLineFinish() {
        return timeLineFinish;
    }

    public void setTimeLineFinish(LocalDateTime timeLineFinish) {
        this.timeLineFinish = timeLineFinish;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}

