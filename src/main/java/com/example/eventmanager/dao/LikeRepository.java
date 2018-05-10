package com.example.eventmanager.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


@PropertySource("classpath:queries/like.properties")
@Repository

public class LikeRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(LikeRepository.class);

    @Autowired
    public LikeRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env
    ){
        logger.info("LikeRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    public Integer getLikesCountForItem (Long itemId) {

        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("itemId", itemId);

            return namedJdbcTemplate.queryForObject(
                    env.getProperty("getLikesCountForItem"),
                    namedParams,
                    Integer.class
            );

        } catch (EmptyResultDataAccessException e) {
            logger.info("Likes for the item not found. Returned 0...");
            return 0;
        }
    }

    public boolean isUserLikesItem (Long userId, Long itemId){

        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("itemId", itemId);
            namedParams.put("userId", userId);

            namedJdbcTemplate.queryForObject(
                    env.getProperty("getUserLikeForItem"),
                    namedParams,
                    Long.class
            );

            return true;

        } catch (EmptyResultDataAccessException e) {
            logger.info("User " + userId + " dont liked item " + itemId);
            return false;
        }
    }

    public int save (Long userId, Long itemId){
        logger.info("Saving like: userId " + userId + " itemId " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("userId", userId);
        namedParams.put("itemId", itemId);


        int update = namedJdbcTemplate.update(env.getProperty("saveLike"), namedParams);

        logger.info(update + " row was added to table likes...");

        return  update;
    }

    public int delete (Long userId, Long itemId){
        logger.info("Deleting like: userId " + userId + " itemId " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("userId", userId);
        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteLike"), namedParams);

        logger.info(deleted + " row was deleted from table likes...");

        return deleted;
    }

    public int deleteLikesForItem (Long itemId) {
        logger.info("Deleting likes for item: " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteLikesForItem"), namedParams);

        logger.info(deleted + " row was deleted from table likes...");

        return deleted;
    }
}
