package com.example.eventmanager.dao;


import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
@PropertySource("classpath:users.queries.properties")
public class UsersRepository implements CrudRepository<User> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UsersRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    private Environment env;

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setToken(resultSet.getString("token"));
            user.setName(resultSet.getString("name"));
            user.setSurName(resultSet.getString("surname"));
            user.setSex(resultSet.getBoolean("sex"));
            user.setVerified(resultSet.getBoolean("verified"));
            return user;
        }
    }

    public User findByUsername(String username) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("username", username);
            return namedParameterJdbcTemplate.queryForObject(env.getProperty("user.findByUsername"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    public boolean isUsernameExists(String username) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("username", username);
            namedParameterJdbcTemplate.queryForObject(env.getProperty("user.isUsernameExists"), namedParams, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("email", email);
            namedParameterJdbcTemplate.queryForObject(env.getProperty("user.isEmailExists"), namedParams, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return false;
        }
    }

    @Override
    public User findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return namedParameterJdbcTemplate.queryForObject(env.getProperty("user.findOne"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return namedParameterJdbcTemplate.query(env.getProperty("user.findAll"), new UserMapper());
    }

    @Override
    public void update(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("id", user.getId());
        namedParams.put("username", user.getUsername());
        namedParams.put("password", user.getPassword());
        namedParams.put("email", user.getEmail());
        namedParams.put("token", user.getToken());
        namedParams.put("name", user.getName());
        namedParams.put("surname", user.getSurName());
        namedParams.put("sex", user.getSex());
        namedParams.put("verified", user.getVerified());
        namedParameterJdbcTemplate.update(env.getProperty("user.update"), namedParams);
    }

    @Override
    public void delete(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("id", user.getId());
        namedParameterJdbcTemplate.update(env.getProperty("user.delete"), namedParams);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null || !this.exists(user.getId())) {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("username", user.getUsername());
            namedParams.put("password", user.getPassword());
            namedParams.put("email", user.getEmail());
            namedParams.put("token", user.getToken());
            namedParams.put("name", user.getName());
            namedParams.put("surname", user.getSurName());
            namedParams.put("sex", user.getSex());
            namedParams.put("verified", user.getVerified());
            namedParameterJdbcTemplate.update(env.getProperty("user.save"), namedParams);
        } else {
            update(user);
        }
    }

}
