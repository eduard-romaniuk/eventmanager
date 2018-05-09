package com.example.eventmanager.service;

import com.example.eventmanager.dao.NotificationSettingsRepository;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.NotificationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NotificationSettingsService {

    private final NotificationSettingsRepository notificationSettingsRepository;
    private final Logger logger = LogManager.getLogger(NotificationSettingsService.class);

    @Autowired
    public NotificationSettingsService(NotificationSettingsRepository notificationSettingsRepository) {
        logger.info("Class initialized");
        this.notificationSettingsRepository = notificationSettingsRepository;
    }

    public void save(NotificationSettings notificationSettings) {
        logger.info("Save");
        notificationSettingsRepository.save(notificationSettings);
    }

    public NotificationSettings get(Long id){
        logger.info("Get with id {}", id);
        return notificationSettingsRepository.findOne(id);
    }

    public List<NotificationSettings> getAll(){
        logger.info("Get all");
        Iterator<NotificationSettings> iter = notificationSettingsRepository.findAll().iterator();
        List<NotificationSettings> copy = new ArrayList<>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public void update(NotificationSettings notificationSettings){
        logger.info("Update with id {}", notificationSettings.getParticipantId());
        notificationSettingsRepository.update(notificationSettings);
    }

    public List<NotificationSettings> findAllByUserId(Long userId){
        logger.info("Find all notification by user with id {}", userId);
        return notificationSettingsRepository.findAllNotificationByUserId(userId);
    }

    public List<Event> findEventsToNotificateByUserId(Long userId, LocalDate date){
        logger.info("Find all notification to notificate by user with id {}", userId);
        return notificationSettingsRepository.findEventsToNotificateByUserId(userId, date);
    }

    public List<Event> findEventsWithCountdownToNotificateByUserId(Long userId, LocalDate date){
        logger.info("Find all notification with countdown to notificate by user with id {}", userId);
        return notificationSettingsRepository.findEventsWithCountdownToNotificateByUserId(userId, date);
    }
}
