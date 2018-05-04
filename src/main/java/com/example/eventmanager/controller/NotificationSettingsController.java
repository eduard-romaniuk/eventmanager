package com.example.eventmanager.controller;

import com.example.eventmanager.domain.NotificationSettings;
import com.example.eventmanager.service.NotificationSettingsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notificationSettings")
public class NotificationSettingsController {

    private final NotificationSettingsService notificationSettingsService;
    private final Logger logger = LogManager.getLogger(NotificationSettingsController.class);

    @Autowired
    public NotificationSettingsController(NotificationSettingsService notificationSettingsService) {
        logger.info("Class initialized");
        this.notificationSettingsService = notificationSettingsService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void create(@RequestBody NotificationSettings notificationSettings) {
        logger.info("POST /");
        notificationSettingsService.save(notificationSettings);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<NotificationSettings>> listAll() {
        logger.info("GET /");

        List<NotificationSettings> notificationSettings = notificationSettingsService.getAll();
        return new ResponseEntity<>(notificationSettings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<NotificationSettings> get(@PathVariable("id") Long id) {
        logger.info("GET /" + id);

        NotificationSettings notificationSettings = notificationSettingsService.get(id);
        if (notificationSettings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notificationSettings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<NotificationSettings> update(@PathVariable("id") Long id,
                                                       @RequestBody NotificationSettings newNotificationSettings) {
        logger.info("POST /" + id);

        NotificationSettings oldNotificationSettings = notificationSettingsService.get(id);
        if (oldNotificationSettings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newNotificationSettings.setParticipant(oldNotificationSettings.getParticipant());
        notificationSettingsService.update(newNotificationSettings);
        return new ResponseEntity<>(newNotificationSettings, HttpStatus.OK);
    }


}
