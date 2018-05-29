package com.example.eventmanager.dao.utils;

import com.example.eventmanager.dao.LikeRepository;
import com.example.eventmanager.domain.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetHandler {
    LikeRepository likeRepository;

    public ResultSetHandler(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    private Map<Long, Item> items = new HashMap<>();

    public static Map<Long, Integer> getWeightOfTags(ResultSet rs) throws SQLException {
        HashMap<Long, Integer> weight = new HashMap<>();

        while( rs.next() ) {
            weight.put(rs.getLong("tag_id"), rs.getInt("tags_count"));
        }

        return weight;
    }

    public Item getItem( ResultSet rs ) throws SQLException {

        Long itemId = rs.getLong("id");

        if(items.containsKey(itemId)){
            return items.get(itemId);
        }

        Item item = new Item();

        item.setId(itemId);
        item.setName(rs.getString("name"));
        item.setPriority(rs.getInt("priority_id"));
        item.setWishListId(rs.getLong("wishlist_id"));
        item.setLikes(likeRepository.getLikesCountForItem(item.getId()));

        return item;
    }

}
