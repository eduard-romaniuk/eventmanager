package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
//     private final String TIME_TO_SEND_NOTIFICATIONS = "0/30 * * * * *"; //every 30 seconds
    //TODO Change
    private final int LIMIT = 10;

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

        int offset = 0;
        boolean hasActiveUsers = true;
        do{
            logger.info("offset - " + offset);

            List<User> activeUsers = userService.findAllActivePagination(LIMIT, offset);
            logger.info("activeUsers - " + activeUsers.toString());

            for (User user : activeUsers) {
                sendEventNotificationToUser(user);
            }

            offset += LIMIT;
            if (activeUsers.size() < LIMIT)
                hasActiveUsers = false;

        } while (hasActiveUsers);

        logger.info("End sending notifications");
    }

    private void sendEventNotificationToUser(User user){
        logger.info("Start sending Event Notification to user with id {}", user.getId());

        List<Event> eventsWithoutCountdown =
                notificationSettingsService.findEventsToNotificateByUserId(user.getId(), LocalDate.now());
        List<Event> eventsWithCountdown =
                notificationSettingsService.findEventsWithCountdownToNotificateByUserId(user.getId(), LocalDate.now());

        if(eventsWithoutCountdown.size() > 0 || eventsWithCountdown.size() > 0){
            sendEventNotification(user, eventsWithoutCountdown, eventsWithCountdown);
        }

        logger.info("End sending Event Notification to user with id {}", user.getId());
    }

    private void sendEventNotification(User user, List<Event> eventsWithoutCountdown, List<Event> eventsWithCountdown) {
        String subject = "Notification about your events";
        String messageText = "Hello, " + user.getLogin() + "! \n\n";

        if(eventsWithoutCountdown.size() > 0){
            messageText += "Your upcoming events:\n";
            for (Event event : eventsWithoutCountdown){
                String eventInfo = "Event \'" + event.getName() + "\'\n" +
                        "Start at " + event.getTimeLineStart().format(formatter) + "\n" +
                        "End at " + event.getTimeLineFinish().format(formatter) + "\n\n";
                messageText += eventInfo;
            }
        }

        if(eventsWithCountdown.size() > 0){
            messageText += "Countdown to your selected events:\n";
            for (Event event : eventsWithCountdown){
                long countDown = DAYS.between(LocalDate.now(), event.getTimeLineStart().toLocalDate());

                String eventInfo = "Event \'" + event.getName() + "\'\n" +
                        "Start at " + event.getTimeLineStart().format(formatter) + "\n" +
                        "End at " + event.getTimeLineFinish().format(formatter) + "\n" +
                        "Left " + countDown + " days\n\n";
                messageText += eventInfo;
            }
        }

        messageText += "Have fun! \nYour Event manager team";

        emailService.sendTextMail(user.getEmail(), subject, messageText);
    }

}
