package com.example.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificationSettings {
    @JsonView(NotificationSettingsView.View.class)
    private Long participantId;
    @JsonView(NotificationSettingsView.View.class)
    private Boolean countDownOn;
    @JsonView(NotificationSettingsView.View.class)
    private int period;
    @JsonView(NotificationSettingsView.View.class)
    private LocalDate startDate;
    @JsonView(NotificationSettingsView.View.class)
    private Boolean emailNotificationOn;
    @JsonView(NotificationSettingsView.View.class)
    private Boolean bellNotificationOn;

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Boolean getCountDownOn() {
        return countDownOn;
    }

    public void setCountDownOn(Boolean countDownOn) {
        this.countDownOn = countDownOn;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean getEmailNotificationOn() {
        return emailNotificationOn;
    }

    public void setEmailNotificationOn(Boolean emailNotificationOn) {
        this.emailNotificationOn = emailNotificationOn;
    }

    public Boolean getBellNotificationOn() {
        return bellNotificationOn;
    }

    public void setBellNotificationOn(Boolean bellNotificationOn) {
        this.bellNotificationOn = bellNotificationOn;
    }
}
