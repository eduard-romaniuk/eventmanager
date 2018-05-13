package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Category;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@PropertySource("classpath:queries/event.properties")
@Repository
public class EventRepository implements CrudRepository<Event> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(EventRepository.class);
    private static final int FIRST_ELEMENT = 0;
    private static final int NORMAL_PRIORITY = 0;

    @Autowired
    public EventRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }


    public List<Event> findByCreator(Long creatorId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("creator_id", creatorId);
            return namedJdbcTemplate.query(env.getProperty("event.findByCreator"), namedParams, new EventMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    @Override
    public int save(Event event) {
        logger.info("Saving event");
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("creator_id", event.getCreator().getId());
        namedParams.addValue("name", event.getName());
        namedParams.addValue("description", event.getDescription());
        namedParams.addValue("place", event.getPlace());
        namedParams.addValue("timeline_start", event.getTimeLineStart());
        namedParams.addValue("timeline_finish", event.getTimeLineFinish());
        namedParams.addValue("period_in_days", event.getPeriod());
        namedParams.addValue("image", event.getImage());
        namedParams.addValue("is_sent", event.isSent());
        namedParams.addValue("is_private", event.isPrivate());
        namedParams.addValue("categoryId", event.getCategory().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(env.getProperty("event.save"), namedParams, keyHolder);
        return (Integer) keyHolder.getKeys().get("id");
    }

    @Override
    public Event findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("eventId", id);
            return namedJdbcTemplate.query(env.getProperty("event.findById"), namedParams, new EventExtractor()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Event not found");
            return null;
        }
    }

    @Override
    public Iterable<Event> findAll() {
        try {
            return namedJdbcTemplate.query(env.getProperty("event.findAllEvents"), new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    @Override
    public void update(Event event) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("name", event.getName());
        namedParams.put("description", event.getDescription());
        namedParams.put("place", event.getPlace());
        namedParams.put("timeline_start", event.getTimeLineStart());
        namedParams.put("timeline_finish", event.getTimeLineFinish());
        namedParams.put("period_in_days", event.getPeriod());
        namedParams.put("image", event.getImage());
        namedParams.put("is_sent", event.isSent());
        namedParams.put("is_private", event.isPrivate());
        namedParams.put("eventId", event.getId());
        namedParams.put("categoryId", event.getCategory().getId());
        namedJdbcTemplate.update(env.getProperty("event.updateEvent"), namedParams);
    }

    @Override
    @Transactional
    public void delete(Event entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", entity.getId());
        namedJdbcTemplate.update(env.getProperty("event.delete.participants"), namedParams);
        namedJdbcTemplate.update(env.getProperty("event.delete.event"), namedParams);
    }

    public Long countSearchResults(String pattern, LocalDateTime start, LocalDateTime finish, String category) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("name", '%' + pattern.toLowerCase().trim().replace(' ', '%') + '%');
        namedParams.put("timeline_start", start);
        namedParams.put("timeline_finish", finish);
        namedParams.put("category", category.equals("") ? "%" : category);
        return namedJdbcTemplate.queryForObject(env.getProperty("event.countSearchResults"), namedParams, Long.class);
    }

    public List<Event> searchWithFiltersPagination(String pattern, LocalDateTime start, LocalDateTime finish,
                                         String category, Long limit, Long offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("name", '%' + pattern.toLowerCase().trim().replace(' ', '%') + '%');
            namedParams.put("timeline_start", start);
            namedParams.put("timeline_finish", finish);
            namedParams.put("category", category.equals("") ? "%" : category);
            String query = new StringBuilder()
                    .append(env.getProperty("event.search"))
                    .append(" LIMIT ")
                    .append(limit)
                    .append(" OFFSET ")
                    .append(offset)
                    .toString();
            return namedJdbcTemplate.query(query, namedParams, new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("searchWithFilters | Events not found");
            return Collections.emptyList();
        }
    }

    public List<Event> findEventsWithUserParticipation(Long userId, Boolean isPrivate, Boolean isSent) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("userId", userId);
            namedParams.put("private", isPrivate);
            namedParams.put("sent", isSent);
            return namedJdbcTemplate.query(env.getProperty("event.findWithUserParticipation"), namedParams, new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    public List<Event> findAllPublicEvents() {
        try {
            return namedJdbcTemplate.query(env.getProperty("event.findAllPublic"), new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    public List<Event> findAllUserEvents(Long userId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("userId", userId);
        try {
            return namedJdbcTemplate.query(env.getProperty("event.findAllUserEvents"), namedParams, new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    public void addUserToEvent(Long user_id, Long event_id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", user_id);
        namedParams.put("event_id", event_id);
        namedParams.put("priority_id", NORMAL_PRIORITY);
        namedJdbcTemplate.update(env.getProperty("event.addUser"), namedParams);
    }

    public List<User> findParticipants(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("event_id", id);
            return namedJdbcTemplate.query(env.getProperty("event.findParticipants"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Participants not found");
            return Collections.emptyList();
        }
    }

    public List<Event> eventsForPeriod(Long id, LocalDate fromDate, LocalDate toDate) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            namedParams.put("fromDate", fromDate);
            namedParams.put("toDate", toDate);
            return namedJdbcTemplate.query(env.getProperty("event.forPeriod"), namedParams, new EventExtractor());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }

    }

    public String getPriority(Long user_id, Long event_id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", user_id);
            namedParams.put("event_id", event_id);
            return namedJdbcTemplate.queryForObject(env.getProperty("event.getEventPriority"), namedParams, String.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Priority not set for user" + user_id + "and event" + event_id);
            return "";
        }
    }

    public void changePriority(Long user_id, Long event_id, Long priority_id) {

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", user_id);
        namedParams.put("event_id", event_id);
        namedParams.put("priority_id", priority_id);
        namedJdbcTemplate.update(env.getProperty("event.changeEventPriority"), namedParams);
        logger.info(user_id + "change priority for event" + event_id);

    }

    public boolean isParticipant(Long user_id, Long event_id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", user_id);
            namedParams.put("event_id", event_id);
            namedJdbcTemplate.queryForObject(env.getProperty("event.userIsParticipant"), namedParams, Long.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("User is not a participant");
            return false;
        }
    }

    public void deleteParticipant(Long user_id, Long event_id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", user_id);
        namedParams.put("event_id", event_id);
        namedJdbcTemplate.update(env.getProperty("event.leave"), namedParams);
    }

    public List<Category> getCategories() {
        try {
            return namedJdbcTemplate.query(env.getProperty("event.categories"), new CategoryMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Categories not found");
            return Collections.emptyList();
        }

    }


    public static final class EventMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setPlace(rs.getString("place"));
            if (rs.getTimestamp("timeline_start") != null) {
                event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
            }
            if (rs.getTimestamp("timeline_finish") != null) {
                event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
            }
            event.setPeriod(rs.getInt("period_in_days"));
            event.setImage(rs.getString("image"));
            event.setSent(rs.getBoolean("is_sent"));
            event.setPrivate(rs.getBoolean("is_private"));
            return event;
        }
    }

    private static final class EventExtractor implements ResultSetExtractor<List<Event>> {

        @Override
        public List<Event> extractData(ResultSet rs) throws SQLException {

            List<Event> events = new ArrayList<>();
            while (rs.next()) {
                Event event = new Event();
                Category category = new Category();
                User creator = new User();
                event.setId(rs.getLong("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                event.setPlace(rs.getString("place"));
                if (rs.getTimestamp("timeline_start") != null) {
                    event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
                }
                if (rs.getTimestamp("timeline_finish") != null) {
                    event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
                }
                event.setPeriod(rs.getInt("period_in_days"));
                event.setImage(rs.getString("image"));
                event.setSent(rs.getBoolean("is_sent"));
                event.setPrivate(rs.getBoolean("is_private"));
                category.setId(rs.getLong("category_id"));
                category.setName(rs.getString("category_name"));
                creator.setId(rs.getLong("creator_id"));
                creator.setLogin(rs.getString("login"));
                creator.setName(rs.getString("creator_name"));
                creator.setSurName(rs.getString("surname"));
                event.setCreator(creator);
                event.setCategory(category);
                events.add(event);
            }

            return events;
        }
    }

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User participant = new User();
            participant.setId(rs.getLong("id"));
            participant.setLogin(rs.getString("login"));
            return participant;
        }
    }

    private static final class CategoryMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));
            return category;
        }
    }
}
