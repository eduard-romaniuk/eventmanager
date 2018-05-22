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
    private User creator;
    @JsonView(NoteView.FullView.class)
    private String image;
    @JsonView(NoteView.FullView.class)
    private Folder folder;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }
}
