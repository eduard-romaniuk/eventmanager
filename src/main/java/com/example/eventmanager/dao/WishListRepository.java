package com.example.eventmanager.dao;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.WishList;
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

@PropertySource("classpath:queries/wishlist.properties")
@Repository
public class WishListRepository implements CrudRepository<WishList>{

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(ItemRepository.class);

    private final ItemRepository itemRepository;

    @Autowired
    public WishListRepository(
            NamedParameterJdbcTemplate namedJdbcTemplate,
            Environment env,
            ItemRepository itemRepository
    ) {
        logger.info("ItemRepository initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;

        this.itemRepository = itemRepository;

    }
    @Override
    public int save(WishList entity) {
        return 0;
    }

    @Override
    public WishList findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<WishList> findAll() {
        return null;
    }

    @Override
    public void update(WishList entity) {

    }

    @Override
    public void delete(WishList entity) {

    }

    public WishList getWishListByUser (Long userId){
        try {
            Map<String, Object> namedParams = new HashMap<>();

            namedParams.put("userId", userId);

            return namedJdbcTemplate.query(env.getProperty("getWishListByUser"), namedParams,
                    rs -> {
                        rs.next();

                        WishList wishList = new WishList();

                        wishList.setId(rs.getLong("id"));
                        wishList.setName(rs.getString("name"));
                        wishList.setUserId(userId);
                        wishList.setItems(itemRepository.getItemsForWishList(wishList.getId()));

                        logger.info("WishList got! " + wishList.toString());
                        return wishList;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("WishList for user with id: " + userId + " not found");
            return null;
        }
    }
}
