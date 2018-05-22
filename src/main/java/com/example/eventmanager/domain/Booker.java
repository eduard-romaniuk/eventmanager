package com.example.eventmanager.domain;

import com.example.eventmanager.domain.transfer.view.ItemView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class Booker {

    @JsonView({ItemView.HideView.class})
    private Long itemId;

    @JsonView({ItemView.FullView.class})
    private Long eventId;

    @JsonView({ItemView.FullView.class})
    private Long userId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{ user_id: " + userId + ", event_id " + eventId + "}";
    }
}
