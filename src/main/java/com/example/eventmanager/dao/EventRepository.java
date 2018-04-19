package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
                "is_sent,is_private FROM \"event\" WHERE id = :creator_id";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", creator_id);
        return namedJdbcTemplate.query(sql,namedParams, new EventMapper());

    }

    @Override
    public int save(Event entity) {
        String sql = "INSERT INTO  \"event\" " +
                "(creator_id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id,is_sent,is_private)" +
                " VALUES (:creator_id,:name,:description,:place, :timeline_start,:timeline_finish,:period_in_days,:image_id,:is_sent,:is_private)";

        Map<String, Object> namedParams = new HashMap<String, Object>();
        namedParams.put("creator_id",entity.getCreator().getId());
        namedParams.put("name",entity.getName());
        namedParams.put("description",entity.getDescriptionId());
        namedParams.put("place",entity.getPlase());
        namedParams.put("timeline_start",entity.getTimeLineStart());
        namedParams.put("timeline_finish",entity.getTimeLineFinish());
        namedParams.put("period_in_days",entity.getPeriod());
        namedParams.put("image_id",entity.getImageId());
        namedParams.put("is_sent",entity.isSent());
        namedParams.put("is_private",entity.isPrivate());
        return namedJdbcTemplate.update(sql, namedParams);

    }

    @Override
    public Event findOne(Long id) {
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                     "is_sent,is_private FROM \"event\" WHERE id = :eventId";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", id);
        return namedJdbcTemplate.queryForObject(sql,namedParams, new EventMapper());
    }

    @Override
    public Iterable<Event> findAll() {
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"event\" ";
        return namedJdbcTemplate.query(sql, new EventMapper());

    }

    @Override
    public void update(Event entity) {
        delete(entity);
        save(entity);
    }

    @Override
    public int delete(Event entity) {
        String sql = "DELETE FROM \"event\" WHERE id = :eventId ";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", entity.getId());
        return namedJdbcTemplate.update(sql,namedParams);

    }

     public List<Event> findAllPublicEvents(){
        String sql = "SELECT id,name,description,place,timeline_start,timeline_finish,period_in_days,image_id," +
                "is_sent,is_private FROM \"event\" WHERE is_private=FALSE ";
        return namedJdbcTemplate.query(sql, new EventMapper());
    }

    private static final class EventMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescriptionId(rs.getLong("description"));
            event.setPlase(rs.getString("place"));
            event.setTimeLineFinish(rs.getTimestamp("timeline_start").toLocalDateTime());
            event.setTimeLineFinish(rs.getTimestamp("timeline_finish").toLocalDateTime());
            event.setPeriod(rs.getInt("period_in_days"));
            event.setImageId(rs.getLong("image_id"));
            event.setSent(rs.getBoolean("is_sent"));
            event.setPrivate(rs.getBoolean("is_private"));
            return event;
        }
    }
}
