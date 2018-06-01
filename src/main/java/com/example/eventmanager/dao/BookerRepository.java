package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Booker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/booker.properties")
@Repository

public class BookerRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(LikeRepository.class);

    @Autowired
    public BookerRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env
    ){
        logger.info("BookerRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int save(Booker booker) {
        logger.info("Saving booker. Booker looks like: " + booker);

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("userId", booker.getUserId());
        namedParams.put("itemId", booker.getItemId());
        namedParams.put("eventId", booker.getEventId());


        int update = namedJdbcTemplate.update(env.getProperty("saveBooker"), namedParams);

        logger.info(update + " row was added to table bookers...");

        return  update;
    }

    public List<Booker> getBookersForItem (Long itemId) {

        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("itemId", itemId);
            return namedJdbcTemplate.query(env.getProperty("getBookersForItem"), namedParams,
                    (rs, rowNum) -> {
                        Booker booker = new Booker();

                        booker.setItemId(itemId);
                        booker.setEventId(rs.getLong("event_id"));
                        booker.setUserId(rs.getLong("user_id"));

                        logger.info("Booker for item " + itemId + " user: " + booker.getUserId() + " got!");

                        return booker;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Bookers for item with id: " + itemId + " not found");
            return Collections.emptyList();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public int delete (Booker booker){
        logger.info("Deleting booker: userId " + booker.getUserId() + " itemId " + booker.getItemId() + " eventId " + booker.getEventId());

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("userId", booker.getUserId());
        namedParams.put("itemId", booker.getItemId());
        namedParams.put("eventId", booker.getEventId());

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteBooker"), namedParams);

        logger.info(deleted + " row was deleted from table bookers...");

        return deleted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteBookersForItem (Long itemId) {
        logger.info("Deleting bookers for item: " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteAllBookersForItem"), namedParams);

        logger.info(deleted + " row was deleted from table bookers...");

        return deleted;
    }

    public List<Booker> getAllBookingForUser (Long userId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("userId", userId);
            return namedJdbcTemplate.query(env.getProperty("getAllBookingForUser"), namedParams,
                    (rs, rowNum) -> {
                        Booker booker = new Booker();

                        booker.setUserId(userId);
                        booker.setEventId(rs.getLong("event_id"));
                        booker.setItemId(rs.getLong("item_id"));

                        logger.info("Booking for user " + userId + " for item: " + booker.getItemId() + " got!");

                        return booker;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Booking items for user : " + userId + " not found");
            return Collections.emptyList();
        }
    }

    public Boolean isBooked( Booker booker ) {

        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("itemId", booker.getItemId());
            namedParams.put("userId", booker.getUserId());
            namedParams.put("eventId", booker.getEventId());

            namedJdbcTemplate.queryForObject(
                    env.getProperty("getBooker"),
                    namedParams,
                    Long.class
            );

            return true;

        } catch (EmptyResultDataAccessException e) {
            logger.error("Booker " + booker.getUserId() + " not found for item " + booker.getItemId());
            return false;
        }
    }
}
