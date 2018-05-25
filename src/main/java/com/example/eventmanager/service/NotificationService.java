package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@PropertySource("classpath:schedule.properties")
@Service
public class NotificationService {

    private final UserService userService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(NotificationService.class);

    private final DateTimeFormatter formatterForDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private final DateTimeFormatter formatterForUsers = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    @Autowired
    public NotificationService(UserService userService,
                               NotificationSettingsService notificationSettingsService,
                               EmailService emailService) {
        this.userService = userService;
        this.notificationSettingsService = notificationSettingsService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "${schedule.cron.sendNotifications}")
    public void sendNotifications() {
        logger.info("Start sending notifications");

        List<User> activeUsers = userService.findAllActive();
        logger.info("activeUsers - " + activeUsers.toString());

        for (User user : activeUsers) {
            sendNotificationToUser(user);
        }

        logger.info("End sending notifications");
    }

    @Scheduled(cron = "${schedule.cron.updateDb}")
    public void updateNotificationsInDB() {
        logger.info("Start updating notifications in DB");
        notificationSettingsService.shiftNotificationStartDateForAllNotifications(LocalDate.now());
        logger.info("End updating notifications in DB");
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
        LocalDateTime eventStartDate = LocalDateTime.parse(event.get("timeline_start").toString(), formatterForDB);
        LocalDateTime eventEndDate = LocalDateTime.parse(event.get("timeline_finish").toString(), formatterForDB);

        String eventInfo = "";

        if(Boolean.TRUE.equals(event.get("count_down_on"))){
            long countDown = DAYS.between(LocalDate.now(), eventStartDate);
            eventInfo += "Left " + countDown + " days to ";
        }

        eventInfo += "\'" + eventName + "\'\n" +
                "Start at " + eventStartDate.format(formatterForUsers) + "\n" +
                "End at " + eventEndDate.format(formatterForUsers) + "\n\n";

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
