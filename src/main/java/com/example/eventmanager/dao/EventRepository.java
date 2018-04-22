package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@PropertySource("classpath:events.properties")
@Repository
public class EventRepository implements CrudRepository<Event> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;

    @Autowired
    public EventRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }


    public List<Event> findByCreator(Long creator_id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("creator_id", creator_id);
        return namedJdbcTemplate.query(env.getProperty("findByCreator"), namedParams, new EventMapper());
    }

    @Override
    public int save(Event event) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("creator_id", event.getCreator().getId());
        namedParams.addValue("name", event.getName());
        namedParams.addValue("description", event.getDescription());
        namedParams.addValue("place", event.getPlace());
        namedParams.addValue("timeline_start", event.getTimeLineStart());
        namedParams.addValue("timeline_finish", event.getTimeLineFinish());
        namedParams.addValue("period_in_days", event.getPeriod());
        namedParams.addValue("image_id", event.getImageId());
        namedParams.addValue("is_sent", event.isSent());
        namedParams.addValue("is_private", event.isPrivate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(env.getProperty("save"), namedParams, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public Event findOne(Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", id);
        return namedJdbcTemplate.query(env.getProperty("findById"), namedParams, new EventWithCreator());
    }

    @Override
    public Iterable<Event> findAll() {
        return namedJdbcTemplate.query(env.getProperty("findAll"), new EventMapper());
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
        namedParams.put("image_id", event.getImageId());
        namedParams.put("is_sent", event.isSent());
        namedParams.put("is_private", event.isPrivate());
        namedParams.put("eventId", event.getId());
        namedJdbcTemplate.update(env.getProperty("updateEvent"), namedParams);
    }

    @Override
    public int delete(Event entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", entity.getId());
        return namedJdbcTemplate.update(env.getProperty("delete"), namedParams);
    }


    public List<Event> findEventsWithUserParticipation(Long user_id) {
        return namedJdbcTemplate.query(env.getProperty("findWithUserParticipation"), new EventMapper());
    }

    public List<Event> findAllPublicEvents() {
        return namedJdbcTemplate.query(env.getProperty("findAllPublic"), new EventMapper());
    }

    public int addUserToEvent(Long user_id, Long event_id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", user_id);
        namedParams.put("event_id", event_id);
        return namedJdbcTemplate.update(env.getProperty("addUser"), namedParams);
    }

    public List<User> findParticipants(Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("event_id", id);
        return namedJdbcTemplate.query(env.getProperty("findParticipants"), namedParams, new UserMapper());
    }


    private static final class EventMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setPlace(rs.getString("place"));
            event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
            event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
            event.setPeriod(rs.getInt("period_in_days"));
            event.setImageId(rs.getLong("image_id"));
            event.setSent(rs.getBoolean("is_sent"));
            event.setPrivate(rs.getBoolean("is_private"));
            return event;
        }
    }

    private static final class EventWithCreator implements ResultSetExtractor<Event>{

        @Override
        public Event extractData(ResultSet rs) throws SQLException, DataAccessException {

            Event event = new Event();
            User creator = new User();
            while (rs.next()){
                event.setId(rs.getLong("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                event.setPlace(rs.getString("place"));
                event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
                event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
                event.setPeriod(rs.getInt("period_in_days"));
                event.setImageId(rs.getLong("image_id"));
                event.setSent(rs.getBoolean("is_sent"));
                event.setPrivate(rs.getBoolean("is_private"));
                creator.setId(rs.getLong("creator_id"));
                creator.setName(rs.getString("login"));
                creator.setName(rs.getString("creator_name"));
                creator.setName(rs.getString("surname"));
            }

            event.setCreator(creator);
            return event;
        }
    }

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User particapant = new User();
            particapant.setId(rs.getLong("id"));
            particapant.setUsername(rs.getString("login"));
            return particapant;
        }
    }
}
