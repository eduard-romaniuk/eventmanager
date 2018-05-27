package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.emailnotification.EmailNotificationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:schedule.properties")
@Service
public class NotificationService {

    private final UserService userService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(NotificationService.class);

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
        String messageText = new EmailNotificationMessage.Builder(user, notifications).build().toString();
        emailService.sendMailInHTML(user.getEmail(), subject, messageText);
    }

}
