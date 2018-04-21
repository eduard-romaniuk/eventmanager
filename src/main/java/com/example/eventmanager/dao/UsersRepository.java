package com.example.eventmanager.dao;

import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UsersRepository implements CrudRepository<User> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public UsersRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();

            //TODO Add other fields
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));

            return user;
        }
    }

    //TODO Move to separate file
    final private static String FIND_BY_USERNAME_SQL = "SELECT id, username, password FROM users WHERE username = :username";
    final private static String FIND_ONE_SQL = "SELECT username, password FROM users WHERE id = :id";
    final private static String FIND_ALL_SQL = "SELECT id, username, password FROM users";
    final private static String UPDATE_USER_SQL = "UPDATE users SET username = :username, password = :password WHERE id = :id";
    final private static String DELETE_USER_SQL = "DELETE FROM users WHERE id = :id";
    final private static String SAVE_USER_SQL = "INSERT INTO users (username, password) VALUES (:username, :password)";

    public User findByUsername(String username) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("username", username);
            return namedJdbcTemplate.queryForObject(FIND_BY_USERNAME_SQL, namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    @Override
    public User findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return namedJdbcTemplate.queryForObject(FIND_ONE_SQL, namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return namedJdbcTemplate.query(FIND_ALL_SQL, new UserMapper());
    }

    @Override
    public void update(User user) {
        Map<String, Object> namedParams = new HashMap<String, Object>();
        namedParams.put("username", user.getUsername());
        namedParams.put("password", user.getPassword());
        namedParams.put("id", user.getId());
        namedJdbcTemplate.update(UPDATE_USER_SQL, namedParams);
    }

    @Override
    public void delete(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("id", user.getId());
        namedJdbcTemplate.update(DELETE_USER_SQL,namedParams);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null || !this.exists(user.getId())) {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("username", user.getUsername());
            namedParams.put("password", user.getPassword());
            namedJdbcTemplate.update(SAVE_USER_SQL, namedParams);
        } else {
            update(user);
        }
    }

}
