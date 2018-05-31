package com.example.eventmanager.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/item.properties")
@Repository
public class ImageRepository{

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(ItemRepository.class);


    @Autowired
    public ImageRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env
    ) {
        logger.info("ImageRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int save(String image, Long itemId) {
        logger.info("Saving image: " + image);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("image", image);
        namedParams.addValue("itemId", itemId);

        int update = namedJdbcTemplate.update(env.getProperty("saveImage"), namedParams);

        logger.info(update + " row was updated from table images...");

        return  update;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public int saveImagesForItem (List<String> images, Long itemId) {
        logger.info("Saving images for item " + itemId);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        int update = 0;
        for (String image : images) {
            update += save(image, itemId);
        }

        return  update;
    }


    public List<String> getImagesForItem( Long itemId ) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("itemId", itemId);
            return namedJdbcTemplate.query(env.getProperty("getImagesForItem"), namedParams,
                    (rs, rowNum) -> {

                        String image = rs.getString("image");

                        logger.info("Image for item " + itemId + " - "  + image + " got!");

                        return image;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Images for item with id: " + itemId + " not found");
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateImagesForItem(List<String> images, Long itemId){
        deleteAllImagesForItem(itemId);
        saveImagesForItem(images, itemId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteAllImagesForItem(Long itemId) {
        logger.info("Deleting images for item: " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteImagesForItem"), namedParams);

        logger.info(deleted + " row was deleted from table images...");


        return deleted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delete (String image, Long itemId) {
        logger.info("Deleting image : "  + image );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("image", image);
        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteImage"), namedParams);

        logger.info(deleted + " row was deleted from table images...");


        return deleted;
    }
}
