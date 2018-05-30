package com.example.eventmanager.dao;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.ItemsTag;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public int saveItemTags(List<Tag> tags, Long itemId) {
        logger.info("Saving tags for item with id : " + itemId);


        int update = 0;

        List<String> existsTags = getTagsForItem(itemId)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        for ( Tag tag : tags) {
            if (!existsTags.contains(tag.getName())) {
                update += addItemTag(itemId, tag.getId());
            }
        }

        logger.info("Summary was updated : " + update + " row");

        return update;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addItemTag (Long itemId, Long tagId){
        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("itemId", itemId);
        namedParams.addValue("tagId", tagId);

        int update = namedJdbcTemplate.update(env.getProperty("saveItemTags"), namedParams);
        logger.info(update + " row was updated from table item_tags...");

        return update;
    }

    public List<Tag> getTagsForItem (Long itemId){
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("itemId", itemId);
            return namedJdbcTemplate.query(env.getProperty("getTagsForItem"), namedParams,
                    (rs, rowNum) -> {
                        Tag tag = new Tag();

                        tag.setId(rs.getLong("id"));
                        tag.setName(rs.getString("tag_name"));

                        logger.info("Tag for item " + itemId + " - "  + tag + " got!");

                        return tag;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Tags for items with id: " + itemId + " not found");
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteUnusedTags ( List<Tag> usedTags, Long itemId) {

        if (usedTags.isEmpty()) {
            return deleteAllTagsForItem(itemId);
        }

        logger.info("Deleting unused tags for item: " + itemId );

        List<Long> tagsIds = usedTags.stream().map(
                Tag::getId
        ).collect(Collectors.toList());

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", itemId);
        namedParams.put("usedTags", tagsIds);


        int deleted = namedJdbcTemplate.update(env.getProperty("deleteAllUnusedTagForItem"), namedParams);

        logger.info(deleted + " row was deleted from table item_tags...");

        cleanTags();

        return deleted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteAllTagsForItem( Long itemId) {
        logger.info("Deleting tags for item: " + itemId );

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", itemId);

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteAllTagsForItem"), namedParams);

        logger.info(deleted + " row was deleted from table item_tags...");

        cleanTags();

        return deleted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int cleanTags () {
        logger.info( "Cleaning tags...");

        Map<String, Object> namedParams = new HashMap<>();

        int deleted = namedJdbcTemplate.update(env.getProperty("cleanTags"), namedParams);

        logger.info(deleted + " row was deleted from table tags after cleaning...");

        return deleted;
    }


}
