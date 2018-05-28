package com.example.eventmanager.dao.utils;

import com.example.eventmanager.domain.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetHandler {

    public static Map<Long, Integer> getWeightOfTags(ResultSet rs) throws SQLException {
        HashMap<Long, Integer> weight = new HashMap<>();

        while( rs.next() ) {
            weight.put(rs.getLong("tag_id"), rs.getInt("tags_count"));
        }

        return weight;
    }

}
