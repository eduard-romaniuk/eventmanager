package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class NotificationService {

    private final UserService userService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(NotificationService.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    //TODO Move to external file
    //Pattern - second, minute, hour, day of month, month, day(s) of week
    private final String TIME_TO_SEND_NOTIFICATIONS = "0 0 10 * * ?"; // every day at 10 a.m.
    //private final String TIME_TO_SEND_NOTIFICATIONS = "0/30 * * * * *"; //every 30 seconds

    @Autowired
    public NotificationService(UserService userService,
                               NotificationSettingsService notificationSettingsService,
                               EmailService emailService) {
        this.userService = userService;
        this.notificationSettingsService = notificationSettingsService;
        this.emailService = emailService;
    }

    @Scheduled(cron = TIME_TO_SEND_NOTIFICATIONS)
    public void sendNotifications() {
        logger.info("Start sending notifications");

        List<User> activeUsers = userService.findAllActive();
        logger.info("activeUsers - " + activeUsers.toString());

        for (User user : activeUsers) {
            sendNotificationToUser(user);
        }

        logger.info("End sending notifications");
    }

    private void sendNotificationToUser(User user){
        logger.info("Start sending Notification to user with id {}", user.getId());

        List<Map<String,Object>> notificationsToSend =
                notificationSettingsService.findAllEventsToNotificateByUserId(user.getId(), LocalDate.now());

        if(!notificationsToSend.isEmpty()){
            sendEventNotification(user, notificationsToSend);
        }

        logger.info("End sending Notification to user with id {}", user.getId());
    }

    private void sendEventNotification(User user, List<Map<String,Object>> notifications) {
        String subject = "Notification about your events";
        String messageText = "Hello, " + user.getLogin() + "! \n\n";

        messageText += generateNotificationMessage(notifications);
        messageText += "Have fun! \nYour Event manager team";

        emailService.sendTextMail(user.getEmail(), subject, messageText);
    }

    private String generateEventMessage(Map<String,Object> event) {
        String eventName = event.get("name").toString();
        LocalDateTime eventStartDate = LocalDateTime.parse(event.get("timeline_start").toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        LocalDateTime eventEndDate = LocalDateTime.parse(event.get("timeline_finish").toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));

        String eventInfo = "";

        if(Boolean.TRUE.equals(event.get("count_down_on"))){
            long countDown = DAYS.between(LocalDate.now(), eventStartDate);
            eventInfo += "Left " + countDown + " days to ";
        }

        eventInfo += "\'" + eventName + "\'\n" +
                "Start at " + eventStartDate.format(formatter) + "\n" +
                "End at " + eventEndDate.format(formatter) + "\n\n";

        return eventInfo;
    }

    private String generateNotificationMessage(List<Map<String,Object>> notifications){
        String messageText = "";
        String messageTextEventWithoutCountdown = "";
        String messageTextEventWithCountdown = "";

        Boolean hasEventWithoutCountdown = false;
        Boolean hasEventWithCountdown = false;

        for (Map<String,Object> notification : notifications) {
            if (Boolean.TRUE.equals(notification.get("count_down_on"))) {
                hasEventWithCountdown = true;
                messageTextEventWithCountdown += generateEventMessage(notification);
            } else {
                hasEventWithoutCountdown = true;
                messageTextEventWithoutCountdown += generateEventMessage(notification);
            }
        }

        if(hasEventWithoutCountdown){
            messageText += "Your upcoming events:\n";
            messageText += messageTextEventWithoutCountdown;
        }

        if(hasEventWithCountdown){
            messageText += "Countdown to your selected events:\n";
            messageText += messageTextEventWithCountdown;
        }

        return messageText;
    }
}
