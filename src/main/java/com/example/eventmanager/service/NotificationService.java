package com.example.eventmanager.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("notification.properties")
@Service
public class NotificationService {

    private final Logger logger = LogManager.getLogger(NotificationService.class);

    //TODO Move to external file
    //Pattern - second, minute, hour, day of month, month, day(s) of week
    //private final String TIME_TO_SEND_NOTIFICATIONS = "0 0 10 * * ?"; // every day at 10 a.m.
    private final String TIME_TO_SEND_NOTIFICATIONS = "*/10 * * * * *"; //every 10 seconds

    @Scheduled(cron = TIME_TO_SEND_NOTIFICATIONS)
    public void sendNotifications() {
        logger.info("Start sending notifications");

        //TODO Send notifications
        logger.info("Time - {} seconds.", System.currentTimeMillis() / 1000);

        logger.info("End sending notifications");
    }
}
