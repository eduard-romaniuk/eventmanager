package com.example.eventmanager.domain;

/**
 * Created by Shvets on 04.05.2018.
 */

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
public class Folder {
    @JsonView(FolderView.ShortView.class)
    private Long id;
    @JsonView(FolderView.ShortView.class)
    private String name;
    @JsonView(FolderView.FullView.class)
    private List<Note> notes;
    @JsonView(FolderView.FullView.class)
    private User creator;
    @JsonView(FolderView.FullView.class)
    private List<Member> members;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
