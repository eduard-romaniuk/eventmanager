package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final UserService userService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(NotificationService.class);

    //TODO Move to external file
    //Pattern - second, minute, hour, day of month, month, day(s) of week
    //private final String TIME_TO_SEND_NOTIFICATIONS = "0 0 10 * * ?"; // every day at 10 a.m.
    private final String TIME_TO_SEND_NOTIFICATIONS = "0/30 * * * * *"; //every 30 minutes

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
        for(User user : activeUsers){
            List<Event> eventsToNotificate =
                    notificationSettingsService.findEventsToNotificateByUserId(user.getId(), LocalDate.now());
            this.emailService.sendEventNotification(user, eventsToNotificate);
        }

        logger.info("End sending notifications");
    }
}
