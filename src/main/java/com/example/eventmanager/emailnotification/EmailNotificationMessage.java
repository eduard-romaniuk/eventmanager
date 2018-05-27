package com.example.eventmanager.emailnotification;

import com.example.eventmanager.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class EmailNotificationMessage {
    private User user;
    private List<Map<String, Object>> notifications;

    private final DateTimeFormatter formatterForDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private final DateTimeFormatter formatterForUsers = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm");

    private EmailNotificationMessage() {
    }

    public static final class Builder {
        private EmailNotificationMessage emailNotificationMessage;

        public Builder(User user, List<Map<String, Object>> notifications) {
            emailNotificationMessage = new EmailNotificationMessage();
            this.emailNotificationMessage.user = user;
            this.emailNotificationMessage.notifications = notifications;
        }

        public Builder user(User user) {
            this.emailNotificationMessage.user = user;
            return this;
        }

        public Builder notifications(List<Map<String, Object>> notifications) {
            this.emailNotificationMessage.notifications = notifications;
            return this;
        }

        public EmailNotificationMessage build() {
            return emailNotificationMessage;
        }
    }

    public User getUser() {
        return user;
    }

    public List<Map<String, Object>> getNotifications() {
        return notifications;
    }

    private String generateEventMessage(Map<String, Object> event) {
        String eventId = event.get("id").toString();
        String eventName = event.get("name").toString();

        String eventImage;
        if (event.get("image") == null) {
            eventImage = "https://www.crucial.com.au/blog/wp-content/uploads/2014/12/events_medium.jpg";
        } else {
            eventImage = event.get("image").toString();
        }

        LocalDateTime eventStartDate = LocalDateTime.parse(event.get("timeline_start").toString(), formatterForDB);
        LocalDateTime eventEndDate = LocalDateTime.parse(event.get("timeline_finish").toString(), formatterForDB);

        StringBuilder eventInfo = new StringBuilder();

        eventInfo.append("<div style=\"border:1px solid black; margin: 5px; width: 500px\">")
                .append("<img src=\"").append(eventImage).append("\" style=\"height: 100%; width: 100%; object-fit: cover;\"")
                .append("alt=\"Event image\" />")
                .append("<h2 style=\"text-align: center\">").append(eventName).append("</h2>")
                .append("<p style=\"margin: 0px 5px\">Start - ")
                .append(eventStartDate.format(formatterForUsers))
                .append("</p>")
                .append("<p style=\"margin: 0px 5px\">End - ")
                .append(eventEndDate.format(formatterForUsers))
                .append("</p>");

        if (Boolean.TRUE.equals(event.get("count_down_on"))) {
            long countDown = DAYS.between(LocalDate.now(), eventStartDate);
            eventInfo.append("<div style=\"text-align: center\"><h3>Left ").append(countDown).append(" days</h3></div>");
        }

        eventInfo.append("<div style=\"text-align: center\">")
                .append("<button style=\"background-color: #4CAF50;" +
                        "margin: 5px; padding: 5px; text-align: center; font-size: 16px;\">")
                .append("<a href=\"https://web-event-manager.firebaseapp.com/event/")
                .append(eventId)
                .append("\" style=\"color: black;\"> Go to event </a></button></div>");

        eventInfo.append("</div>");

        return eventInfo.toString();
    }

    private String generateNotificationMessage(List<Map<String, Object>> notifications) {
        StringBuilder messageText = new StringBuilder();
        StringBuilder messageTextEventWithoutCountdown = new StringBuilder();
        StringBuilder messageTextEventWithCountdown = new StringBuilder();

        Boolean hasEventWithoutCountdown = false;
        Boolean hasEventWithCountdown = false;

        for (Map<String, Object> notification : notifications) {
            if (Boolean.TRUE.equals(notification.get("count_down_on"))) {
                hasEventWithCountdown = true;
                messageTextEventWithCountdown.append(generateEventMessage(notification));
            } else {
                hasEventWithoutCountdown = true;
                messageTextEventWithoutCountdown.append(generateEventMessage(notification));
            }
        }

        if (hasEventWithoutCountdown) {
            messageText.append("<h2>Your upcoming events:</h2><br>").append(messageTextEventWithoutCountdown);
        }

        if (hasEventWithCountdown) {
            messageText.append("<h2>Countdown to your selected events:</h2><br>").append(messageTextEventWithCountdown);
        }

        return messageText.toString();
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();

        message.append("<h1>Hello, ").append(user.getLogin()).append("! </h1>");
        message.append(generateNotificationMessage(notifications));
        message.append("Have fun! <br>Your Event manager team");

        return message.toString();
    }
}
