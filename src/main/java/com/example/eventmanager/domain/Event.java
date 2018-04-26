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
}
