package com.example.eventmanager.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
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
}
