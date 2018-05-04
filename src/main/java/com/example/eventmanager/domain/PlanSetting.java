package com.example.eventmanager.domain;


import java.time.LocalDate;

public class PlanSetting {

    private User user;
    private boolean sendPlan;
    private LocalDate from;
    private Integer period;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSendPlan() {
        return sendPlan;
    }

    public void setSendPlan(boolean sendPlan) {
        this.sendPlan = sendPlan;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
