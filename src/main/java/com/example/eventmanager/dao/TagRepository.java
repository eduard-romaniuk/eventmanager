package com.example.eventmanager.dao;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.Tag;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/tag.properties")
@Repository
public class TagRepository implements CrudRepository<Tag>{
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(TagRepository.class);

    @Autowired
    public TagRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env
    ){
        logger.info("TagRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }


    @Override
    public int save(Tag tag) {
        logger.info("Saving tag. Tag looks like: " + tag);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("tagName", tag.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = namedJdbcTemplate.update(env.getProperty("saveTag"), namedParams, keyHolder);

        logger.info(update + " row was updated from table tag...");
        logger.info("New tag was added = " + keyHolder.getKeys());

        Integer indId = (Integer) keyHolder.getKeys().get("id");
        tag.setId(indId.longValue());

        return update;
    }

    @Override
    public Tag findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<Tag> findAll() {
        return null;
    }

    @Override
    public void update(Tag entity) {

    }

    @Override
    public void delete(Tag entity) {

    }

    public Long findByName ( String tagName ){
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("tagName", tagName);

            return namedJdbcTemplate.queryForObject(env.getProperty("findIdByTagName"), namedParams,Long.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is not tag with name = " + tagName);
            return null; //TODO: Optional
        }
    }

    public int saveItemTags(Item item) {
        logger.info("Saving tags for item with id : " + item.getId());

        int update = 0;

        for ( Tag tag : item.getTags()) {
           update += addItemTag(item.getId(), tag.getId());
        }

        logger.info("Summary was updated : " + update + " row");

        return update;
    }

    public int addItemTag (Long itemId, Long tagId){
        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("itemId", itemId);
        namedParams.addValue("tagId", tagId);

        int update = namedJdbcTemplate.update(env.getProperty("saveItemTags"), namedParams);
        logger.info(update + " row was updated from table item_tags...");

        return update;
    }

    public List<Tag> getItemsForWishList (Item item){
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("itemId", item.getId());
            return namedJdbcTemplate.query(env.getProperty("getTagsForItem"), namedParams,
                    (rs, rowNum) -> {
                        Tag tag = new Tag();

                        tag.setId(rs.getLong("id"));
                        tag.setName(rs.getString("tag_name"));

                        logger.info("Tag for item " + item.getId() + " - "  + tag.getName() + " got!");

                        return tag;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Tags for items with id: " + item.getId() + " not found");
            return Collections.emptyList();
        }
    }


}
