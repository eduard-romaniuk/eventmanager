package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class Note {
    @JsonView(NoteView.ShortView.class)
    private Long id;
    @JsonView(NoteView.ShortView.class)
    private String name;
    @JsonView(NoteView.ShortView.class)
    private String description;
    @JsonView(NoteView.FullView.class)
    private String place;
    @JsonView(NoteView.FullView.class)
    private User creator;
    @JsonView(NoteView.FullView.class)
    private Integer period;
    @JsonView(NoteView.FullView.class)
    private String image;
    @JsonView(NoteView.FullView.class)
    private boolean isPrivate;
    @JsonView(NoteView.FullView.class)
    private Folder folder;
    @JsonView(NoteView.FullView.class)
    private boolean isSent;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
