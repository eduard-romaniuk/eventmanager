package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.stereotype.Component;

@Component
public class Participant {
    @JsonView(ParticipantView.ShortView.class)
    private Long id;
    @JsonView(ParticipantView.FullView.class)
    private User userId;
    @JsonView(ParticipantView.FullView.class)
    private Event eventId;
    //private Priority priorityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }
}
