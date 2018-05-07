package com.example.eventmanager.dao;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.NotificationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/notification.properties")
@Repository
public class NotificationSettingsRepository implements CrudRepository<NotificationSettings> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(NotificationSettingsRepository.class);

    @Autowired
    public NotificationSettingsRepository(NamedParameterJdbcTemplate namedJdbcTemplate,
                                          Environment env) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    private final class NotificationSettingsMapper implements RowMapper<NotificationSettings> {
        @Override
        public NotificationSettings mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            NotificationSettings notificationSettings = new NotificationSettings();

            notificationSettings.setParticipantId(resultSet.getLong("participant_id"));
            notificationSettings.setCountDownOn(resultSet.getBoolean("count_down_on"));
            notificationSettings.setPeriod(resultSet.getInt("period"));
            notificationSettings.setStartDate(resultSet.getDate("start_date") != null ?
                            resultSet.getDate("start_date").toLocalDate() : null);
            notificationSettings.setEmailNotificationOn(resultSet.getBoolean("email_notif_on"));
            notificationSettings.setBellNotificationOn(resultSet.getBoolean("bell_notif_on"));

            return notificationSettings;
        }
    }

    @Override
    public int save(NotificationSettings notificationSettings) {
        logger.info("Save");

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("participant_id", notificationSettings.getParticipantId());
        namedParams.put("count_down_on", notificationSettings.getCountDownOn());
        namedParams.put("period", notificationSettings.getPeriod());
        namedParams.put("start_date", notificationSettings.getStartDate());
        namedParams.put("email_notif_on", notificationSettings.getEmailNotificationOn());
        namedParams.put("bell_notif_on", notificationSettings.getBellNotificationOn());

        return namedJdbcTemplate.update(env.getProperty("saveNotification"), namedParams);
    }

    @Override
    public NotificationSettings findOne(Long id) {
        try {
            logger.info("FindOne with id {}", id);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("participant_id", id);
            return namedJdbcTemplate.queryForObject(env.getProperty("findOneNotification"),
                    namedParams, new NotificationSettingsMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.error("NotificationSettings with id {} not found", id);
            return null;
        }
    }

    @Override
    public Iterable<NotificationSettings> findAll() {
        logger.info("FindAll");
        return namedJdbcTemplate.query(env.getProperty("findAllNotification"), new NotificationSettingsMapper());
    }

    @Override
    public void update(NotificationSettings notificationSettings) {
        logger.info("Update with id {}", notificationSettings.getParticipantId());

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("count_down_on", notificationSettings.getCountDownOn());
        namedParams.put("period", notificationSettings.getPeriod());
        namedParams.put("start_date", notificationSettings.getStartDate());
        namedParams.put("email_notif_on", notificationSettings.getEmailNotificationOn());
        namedParams.put("bell_notif_on", notificationSettings.getBellNotificationOn());
        namedParams.put("participant_id", notificationSettings.getParticipantId());

        namedJdbcTemplate.update(env.getProperty("updateNotification"), namedParams);
    }

    @Override
    public void delete(NotificationSettings notificationSettings) {
        logger.error("Delete does not supported");
    }

    public List<NotificationSettings> findAllNotificationByUserId(Long userId) {
        try {
            logger.info("Find all notification by user with id {}", userId);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", userId);

            return namedJdbcTemplate.query(env.getProperty("findAllNotificationByUserId"),
                    namedParams, new NotificationSettingsMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Event notification for user with id {} not found", userId);
            return Collections.emptyList();
        }
    }

    public List<Event> findEventsToNotificateByUserId(Long userId, LocalDate date) {
        try {
            logger.info("Find events to notificate by user with id {}", userId);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", userId);
            namedParams.put("date", date);
            namedParams.put("count_down_on", false);

            return namedJdbcTemplate.query(env.getProperty("findEventsToNotificateByUserId"),
                    namedParams, new EventRepository.EventMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Find events to notificate for user with id {} not found", userId);
            return Collections.emptyList();
        }
    }

    public List<Event> findEventsWithCountdownToNotificateByUserId(Long userId, LocalDate date) {
        try {
            logger.info("Find events to notificate with countdown by user with id {}", userId);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", userId);
            namedParams.put("date", date);
            namedParams.put("count_down_on", true);

            return namedJdbcTemplate.query(env.getProperty("findEventsToNotificateByUserId"),
                    namedParams, new EventRepository.EventMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Find events to notificate with countdown for user with id {} not found", userId);
            return Collections.emptyList();
        }
    }

}
