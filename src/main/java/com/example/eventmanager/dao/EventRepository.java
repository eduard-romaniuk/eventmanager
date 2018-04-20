package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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


@Repository
public class EventRepository implements CrudRepository<Event> {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public EventRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }
    public List<Event> findByCreator(Long creator_id){

        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"events\" WHERE creator_id = :creator_id";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("creator_id", creator_id);
        return namedJdbcTemplate.query(sql,namedParams, new EventMapper());

    }

    @Override
    public int save(Event entity) {
        String sql = "INSERT INTO  \"events\" " +
                "(creator_id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id,is_sent,is_private)" +
                " VALUES (:creator_id,:name,:description,:place, :timeline_start,:timeline_finish,:period_in_days,:image_id,:is_sent,:is_private)";

        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("creator_id",entity.getCreator().getId());
        namedParams.addValue("name",entity.getName());
        namedParams.addValue("description",entity.getDescriptionId());
        namedParams.addValue("place",entity.getPlase());
        namedParams.addValue("timeline_start",entity.getTimeLineStart());
        namedParams.addValue("timeline_finish",entity.getTimeLineFinish());
        namedParams.addValue("period_in_days",entity.getPeriod());
        namedParams.addValue("image_id",entity.getImageId());
        namedParams.addValue("is_sent",entity.isSent());
        namedParams.addValue("is_private",entity.isPrivate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql,namedParams,keyHolder);
        return keyHolder.getKey().intValue();

    }

    @Override
    public Event findOne(Long id) {
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                     "is_sent,is_private FROM \"events\" WHERE id = :eventId";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", id);
        return namedJdbcTemplate.queryForObject(sql,namedParams, new EventMapper());
    }

    @Override
    public Iterable<Event> findAll() {
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"events\" ";
        return namedJdbcTemplate.query(sql, new EventMapper());

    }

    @Override
    public void update(Event entity) {
        delete(entity);
        save(entity);
    }

    @Override
    public int delete(Event entity) {
        String sql = "DELETE FROM \"events\" WHERE id = :eventId ";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", entity.getId());
        return namedJdbcTemplate.update(sql,namedParams);

    }


     public List<Event> findEventsWithUserParticipation(Long user_id){
        String sql = "SELECT ev.id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"events\" ev INNER JOIN participants part ON ev.id = part.event_id " +
                "WHERE part.user_id= :user_id";
        return namedJdbcTemplate.query(sql, new EventMapper());
    }

    public List<Event> findAllPublicEvents(){
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"events\" WHERE is_private=FALSE AND is_sent=TRUE";
        return namedJdbcTemplate.query(sql, new EventMapper());
    }

    public int addUserToEvent(Long user_id,Long event_id){
        String sql = "INSERT INTO  \"participants\" (user_id,event_id) VALUES (:user_id,:ivent_id)";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id",user_id);
        namedParams.put("event_id",event_id);
        return namedJdbcTemplate.update(sql, namedParams);

    }

    public List<User> findParticipants(Long id){
        String sql = "SELECT users.id,login is_sent FROM \"users\" users INNER JOIN participants part ON users.id = part.user_id WHERE event_id= :event_id";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("event_id",id);
        return namedJdbcTemplate.query(sql,namedParams, new UserMapper());

    }



    private static final class EventMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescriptionId(rs.getLong("description"));
            event.setPlase(rs.getString("place"));
            event.setTimeLineStart(rs.getTimestamp("timeline_start").toLocalDateTime());
            event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
            event.setPeriod(rs.getInt("period_in_days"));
            event.setImageId(rs.getLong("image_id"));
            event.setSent(rs.getBoolean("is_sent"));
            event.setPrivate(rs.getBoolean("is_private"));
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
