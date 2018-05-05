package com.example.eventmanager.service;

import com.example.eventmanager.dao.NotificationSettingsRepository;
import com.example.eventmanager.domain.NotificationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        logger.info("save");
        notificationSettingsRepository.save(notificationSettings);
    }

    public NotificationSettings get(Long id){
        logger.info("get with id {}", id);
        return notificationSettingsRepository.findOne(id);
    }

    public List<NotificationSettings> getAll(){
        logger.info("getAll");
        Iterator<NotificationSettings> iter = notificationSettingsRepository.findAll().iterator();
        List<NotificationSettings> copy = new ArrayList<>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public void update(NotificationSettings notificationSettings){
        logger.info("update with id {}", notificationSettings.getParticipant());
        notificationSettingsRepository.update(notificationSettings);
    }

    public List<NotificationSettings> findAllByUserId(Long userId){
        logger.info("findAllNotificationByUserId with user id {}", userId);
        return notificationSettingsRepository.findAllNotificationByUserId(userId);
    }

    public List<Map<String,Object>> findNotificationToSendByUserId(Long userId, LocalDate date){
        logger.info("findAllNotificationByUserId with user id {}", userId);
        return notificationSettingsRepository.findNotificationToSendByUserId(userId, date);
    }
}
