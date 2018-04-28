package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Event {
    @JsonView(EventView.ShortView.class)
    private Long id;
    @JsonView(EventView.ShortView.class)
    private String name;

    @JsonView(EventView.FullView.class)
    private String description;
    @JsonView(EventView.FullView.class)
    private String place;
    @JsonView(EventView.FullView.class)
    private LocalDateTime timeLineStart;
    @JsonView(EventView.FullView.class)
    private LocalDateTime timeLineFinish;

    private User creator;
    // private Folder folder;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
