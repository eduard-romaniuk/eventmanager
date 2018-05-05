package com.example.eventmanager.domain;


import java.time.LocalDate;

public class PersonalPlanSetting {

    private User user;
    private boolean sendPlan;
    private LocalDate fromDate;
    private Integer planPeriod;
    private Integer notificationPeriod;

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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getPlanPeriod() {
        return planPeriod;
    }

    public void setPlanPeriod(Integer planPeriod) {
        this.planPeriod = planPeriod;
    }

    public Integer getNotificationPeriod() {
        return notificationPeriod;
    }

    public void setNotificationPeriod(Integer notificationPeriod) {
        this.notificationPeriod = notificationPeriod;
    }
}
