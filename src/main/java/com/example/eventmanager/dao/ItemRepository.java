package com.example.eventmanager.dao;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.WishList;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/item.properties")
@Repository
public class ItemRepository implements CrudRepository<Item>{

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(ItemRepository.class);

    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public ItemRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env,
            TagRepository tagRepository,
            LikeRepository likeRepository
    ) {
        logger.info("ItemRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
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

        return  update;
    }


    @Override
    public Item findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<Item> findAll() {
        return null;
    }

    @Override
    public void update(Item entity) {

    }

    @Override
    public void delete(Item entity) {

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
                        item.setDescription(rs.getString("description"));
                        item.setWishListId(wishListId);
                        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));
                        item.setTags(tagRepository.getTagsForItem(item.getId()));

                        logger.info("Item got! " + item.toString());
                        return item;
                    }
                    );
        } catch (EmptyResultDataAccessException e) {
            logger.info("Items for wish list with id: " + wishListId + " not found");
            return Collections.emptyList();
        }
    }
//
//    private static final class ItemMapper implements RowMapper<Item> {
//        @Override
//        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Item item = new Item();
//
//            item.setId(rs.getLong("id"));
//            item.setName(rs.getString("name"));
//            item.setPriority(rs.getInt("priority"));
//            item.setDescription(rs.getString("description"));
//            item.setCountLikes(rs.getInt("likesCount"));
//            item.setWishList(wishListId);
//            item.setTags();
//
//            return item;
//        }
//    }
}
