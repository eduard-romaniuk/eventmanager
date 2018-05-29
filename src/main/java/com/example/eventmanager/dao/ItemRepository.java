package com.example.eventmanager.dao;

import com.example.eventmanager.dao.utils.ResultSetHandler;
import com.example.eventmanager.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@PropertySource("classpath:queries/item.properties")
@Repository
public class ItemRepository implements CrudRepository<Item>{

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(ItemRepository.class);

    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final BookerRepository bookerRepository;

    @Autowired
    public ItemRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env,
            TagRepository tagRepository,
            LikeRepository likeRepository,
            ImageRepository imageRepository,
            BookerRepository bookerRepository
    ) {
        logger.info("ItemRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
        this.imageRepository = imageRepository;
        this.bookerRepository = bookerRepository;
    }

    @Override
    public int save(Item item) {
        logger.info("Saving item: " + item);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("name", item.getName());
        namedParams.addValue("description", item.getDescription());
        namedParams.addValue("priority_id", item.getPriority());
        namedParams.addValue("wishlist_id", item.getWishListId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = namedJdbcTemplate.update(env.getProperty("saveItem"), namedParams, keyHolder);

        logger.info(update + " row was updated from table items...");
        logger.info("New item was added = " + keyHolder.getKeys());

        item.setId(
                ((Integer)keyHolder
                        .getKeys()
                        .get("id"))
                        .longValue()
        );

        imageRepository.saveImagesForItem(item.getImages(), item.getId());

        return  update;
    }


    @Override
    public Item findOne(Long itemId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("itemId", itemId);

            return namedJdbcTemplate.query(env.getProperty("getItemById"), namedParams,
                    rs -> {
                        rs.next();

                        Item item = new Item();

                        item.setId(itemId);
                        item.setName(rs.getString("name"));
                        item.setPriority(rs.getInt("priority_id"));
                        item.setDescription(rs.getString("description"));
                        item.setWishListId(rs.getLong("wishlist_id"));
                        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));
                        item.setTags(tagRepository.getTagsForItem(item.getId()));
                        item.setImages(imageRepository.getImagesForItem(item.getId()));
                        item.setBookers(bookerRepository.getBookersForItem(itemId));

                        logger.info("Item got! " + item.toString());
                        return item;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Items with id: " + itemId + " not found");
            return null;
        }

    }

    @Override
    public Iterable<Item> findAll() {
        return null;
    }

    @Override
    public void update(Item item) {

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("name", item.getName());
        namedParams.put("description", item.getDescription());
        namedParams.put("priorityId", item.getPriority());
        namedParams.put("itemId", item.getId());

        namedJdbcTemplate.update(env.getProperty("updateItem"), namedParams);

        imageRepository.updateImagesForItem(item.getImages(), item.getId());

    }

    @Override
    public void delete(Item item) {

        imageRepository.deleteAllImagesForItem(item.getId());

        Map<String, Object> namedParams = new HashMap<>();

        namedParams.put("itemId", item.getId());

        int deleted = namedJdbcTemplate.update(env.getProperty("deleteItem"), namedParams);

        logger.info(deleted + " items was deleted from items");
    }


    public Long copyItem ( Long toWishListId, Long itemId ) {
        logger.info("Copy item: " + itemId);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("toWishListId", toWishListId);
        namedParams.addValue("itemId", itemId);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = namedJdbcTemplate.update(env.getProperty("copyItem"), namedParams, keyHolder);

        logger.info(update + " row was updated from table items...");
        logger.info("Item was copy = " + keyHolder.getKeys());

        Long newItemId = ((Integer)keyHolder
                .getKeys()
                .get("id"))
                .longValue();

        imageRepository.saveImagesForItem(imageRepository.getImagesForItem(itemId), newItemId);

        return newItemId;
    }


    public List<Item> getItemsForWishList ( Long wishListId ){
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("wishListId", wishListId);
            return namedJdbcTemplate.query(env.getProperty("getItemsForWishList"), namedParams,
                    (rs, rowNum) -> {
                        Item item = new Item();


                        item.setId(rs.getLong("id"));
                        item.setName(rs.getString("name"));
                        item.setPriority(rs.getInt("priority_id"));
                        item.setWishListId(wishListId);
                        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));
                        item.setBookers(bookerRepository.getBookersForItem(item.getId()));

                        logger.info("Item got! " + item.toString());
                        return item;
                    }
                    );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Items for wish list with id: " + wishListId + " not found");
            return Collections.emptyList();
        }
    }

    public List<Item> getPopularItems ( Long limit, Long offset ){
        try {
            Map<String, Object> namedParams = new HashMap<>();

            String query = new StringBuilder()
                    .append(env.getProperty("getPopularItems"))
                    .append(" LIMIT ")
                    .append(limit)
                    .append(" OFFSET ")
                    .append(offset)
                    .toString();

            return namedJdbcTemplate.query(query,
                    (rs, rowNum) -> {
                        Item item = new Item();

                        item.setId(rs.getLong("id"));
                        item.setName(rs.getString("name"));
                        item.setPriority(rs.getInt("priority_id"));
                        item.setWishListId(rs.getLong("wishlist_id"));
                        item.setLikes(rs.getInt("likes_count"));

                        logger.info("Item got! " + item.toString());
                        return item;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not found any items");
            return Collections.emptyList();
        }
    }

    public List<Item> getBookedItems ( Long userId ){
        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("userId", userId);

            return namedJdbcTemplate.query(env.getProperty("getBookedItems"), namedParams,
                    (rs, rowNum) -> {
                        Item item = new Item();

                        item.setId(rs.getLong("id"));
                        item.setName(rs.getString("name"));
                        item.setPriority(rs.getInt("priority_id"));
                        item.setWishListId(rs.getLong("wishlist_id"));
                        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));

                        logger.info("Item got ! " + item.toString());
                        return item;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not found any items");
            return Collections.emptyList();
        }
    }

    public List<Item> getEventBookingItems ( Long eventId ){
        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("eventId", eventId);

            return namedJdbcTemplate.query(env.getProperty("getBookingItemsForEvent"), namedParams,
                    (rs, rowNum) -> {
                        Item item = new Item();

                        item.setId(rs.getLong("id"));
                        item.setName(rs.getString("name"));
                        item.setPriority(rs.getInt("priority_id"));
                        item.setWishListId(rs.getLong("wishlist_id"));
                        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));

                        logger.info("Item got!  " + item.toString());
                        return item;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not found any items");
            return Collections.emptyList();
        }
    }


    public Map<Long, Integer> getWeightOfSystemTags ( int limit ){
        try {

            String query = new StringBuilder()
                    .append(env.getProperty("getWeightOfSystemTags"))
                    .append(" LIMIT ")
                    .append(limit)
                    .toString();

            return namedJdbcTemplate.query(query, ResultSetHandler::getWeightOfTags);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not found any tags");
            return Collections.emptyMap();
        }
    }

    public Map<Long, Integer> getWeightOfMyTags ( Long userId ){

        try {

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("userId", userId);

            return namedJdbcTemplate.query(env.getProperty("getWeightOfMyTags"), namedParams,ResultSetHandler::getWeightOfTags);

        } catch (EmptyResultDataAccessException e) {

            logger.info("Not found any tags");
            return Collections.emptyMap();

        }
    }

    public Map<Long, Integer> getWeightOfFriendsTags ( Long userId ){

        try {

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("userId", userId);

            return namedJdbcTemplate.query(env.getProperty("getWeightOfFriendsTags"), namedParams, ResultSetHandler::getWeightOfTags);

        } catch (EmptyResultDataAccessException e) {

            logger.info("Not found  any tags");
            return Collections.emptyMap();

        }
    }


    public List<ItemsTag> getItemsWithTags (Set<Long> tagIds, Long userId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            ResultSetHandler resultSetHandler = new ResultSetHandler(likeRepository);

            namedParams.put("userId", userId);
            namedParams.put("tagsIds", tagIds);

            return namedJdbcTemplate.query(env.getProperty("getTagWithItem"), namedParams,
                    (rs, rowNum) -> {
                        ItemsTag tag = new ItemsTag();

                        tag.setTagId(rs.getLong("tag_id"));

                        tag.setItem(resultSetHandler.getItem(rs));

                        logger.info("Tag: " + tag.getTagId() + " with item: " + tag.getItem().getId() + " got!");
                        return tag;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not found any items");
            return Collections.emptyList();
        }
    }




}
