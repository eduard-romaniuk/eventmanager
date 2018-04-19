package com.example.eventmanager.dao;

import com.example.eventmanager.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UsersRepository implements CrudRepository<User> {

    private JdbcTemplate jdbcTemplate = JdbcTemplateSingleton.getInstance();

    public class UserMapper implements RowMapper<User> {
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
    final private static String FIND_BY_USERNAME_SQL = "SELECT id, username, password FROM users WHERE username = ?";
    final private static String FIND_ONE_SQL = "SELECT username, password FROM users WHERE id = ?";
    final private static String FIND_ALL_SQL = "SELECT id, username, password FROM users";
    final private static String UPDATE_USER_SQL = "UPDATE users SET username = ?, password = ? WHERE id = ?";
    final private static String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    final private static String SAVE_USER_SQL = "INSERT INTO users (username, password) VALUES (?,?)";

    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_USERNAME_SQL, new UserMapper(), username);
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    @Override
    public User findOne(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_ONE_SQL, new UserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            //TODO Logging
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, new UserMapper());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE_USER_SQL, user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update(DELETE_USER_SQL, user.getId());
    }

    @Override
    public void save(User user) {
        if (user.getId() == null || !this.exists(user.getId())) {
            jdbcTemplate.update(SAVE_USER_SQL, user.getUsername(), user.getPassword());
        } else {
            update(user);
        }
    }

}
