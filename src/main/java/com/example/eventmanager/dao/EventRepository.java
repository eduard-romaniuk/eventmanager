package com.example.eventmanager.dao;


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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
            return namedJdbcTemplate.query(env.getProperty("findByCreator"), namedParams, new EventMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    @Override
    public int save(Event event) {
        System.out.println(event);
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(env.getProperty("saveEvent"), namedParams, keyHolder);
        //return keyHolder.getKey().intValue();
        System.out.println("newEvent = " + keyHolder.getKeys());
        return (Integer)keyHolder.getKeys().get("id");
    }

    @Override
    public Event findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("eventId", id);
            return namedJdbcTemplate.query(env.getProperty("findById"), namedParams, new EventWithCreator()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Event not found");
            return null;
        }
    }

    @Override
    public Iterable<Event> findAll() {
        try {
            return namedJdbcTemplate.query(env.getProperty("findAllEvent"), new EventMapper());
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
        namedJdbcTemplate.update(env.getProperty("updateEvent"), namedParams);
    }

    @Override
    public void delete(Event entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", entity.getId());
        namedJdbcTemplate.update(env.getProperty("delete"), namedParams);
    }


    public List<Event> findEventsWithUserParticipation(Long userId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", userId);
            return namedJdbcTemplate.query(env.getProperty("findWithUserParticipation"), namedParams, new EventMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Events not found");
            return Collections.emptyList();
        }
    }

    public List<Event> findAllPublicEvents() {
        try {
            return namedJdbcTemplate.query(env.getProperty("findAllPublic"), new EventMapper());
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
        namedJdbcTemplate.update(env.getProperty("addUser"), namedParams);
    }

    public List<User> findParticipants(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("event_id", id);
            return namedJdbcTemplate.query(env.getProperty("findParticipants"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Participants not found");
            return Collections.emptyList();
        }
    }

    public List<Event> eventsListForPeriod(Long id, LocalDate fromDate, LocalDate toDate){
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            namedParams.put("fromDate", fromDate);
            namedParams.put("toDate", toDate);
            return namedJdbcTemplate.query(env.getProperty("forPeriod"), namedParams,new EventWithCreator());
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
            return namedJdbcTemplate.queryForObject(env.getProperty("getEventPriority"), namedParams,String.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Priority not set for user"+user_id+"and event"+event_id);
            return "";
        }
    }
    public void changePriority(Long user_id, Long event_id,Long priority_id) {
        
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", user_id);
            namedParams.put("event_id", event_id);
            namedParams.put("priority_id", priority_id);
            namedJdbcTemplate.update(env.getProperty("changeEventPriority"),namedParams);
            logger.info( user_id+"change priority for event"+event_id);
            
    }

    

    public static final class EventMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setPlace(rs.getString("place"));
            if(rs.getTimestamp("timeline_start") != null) {
                event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
            }
            if(rs.getTimestamp("timeline_finish") != null) {
                event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
            }
            event.setPeriod(rs.getInt("period_in_days"));
            event.setImage(rs.getString("image"));
            event.setSent(rs.getBoolean("is_sent"));
            event.setPrivate(rs.getBoolean("is_private"));
            return event;
        }
    }

    private static final class EventWithCreator implements ResultSetExtractor<List<Event>> {

        @Override
        public List<Event> extractData(ResultSet rs) throws SQLException {

            List<Event> events = new ArrayList<>();
            while (rs.next()) {
                Event event = new Event();
                User creator = new User();
                event.setId(rs.getLong("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                event.setPlace(rs.getString("place"));
                if(rs.getTimestamp("timeline_start") != null) {
                    event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
                }
                if(rs.getTimestamp("timeline_finish") != null) {
                    event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
                }
                event.setPeriod(rs.getInt("period_in_days"));
                event.setImage(rs.getString("image"));
                event.setSent(rs.getBoolean("is_sent"));
                event.setPrivate(rs.getBoolean("is_private"));
                creator.setId(rs.getLong("creator_id"));
                creator.setLogin(rs.getString("login"));
                creator.setName(rs.getString("creator_name"));
                creator.setSurName(rs.getString("surname"));
                event.setCreator(creator);
                events.add(event);
            }

           return events;
        }
    }

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            System.out.println("EventRepository.UserMapper.mapRow");
            User participant = new User();
            participant.setId(rs.getLong("id"));
            participant.setLogin(rs.getString("login"));
            return participant;
        }
    }
}
