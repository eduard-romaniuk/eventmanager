package com.example.eventmanager.dao;


import com.example.eventmanager.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class UsersRepository implements CrudRepository<User> {

    private JdbcTemplate jdbcTemplate = JdbcTemplateSingleton.getInstance();

    public User findByUsername(String username) {
        try {
            return (User) jdbcTemplate.queryForObject(
                    "SELECT id, login, password FROM \"users\" WHERE login = ?",
                    new Object[]{username},
                    (RowMapper<Object>) (rs, rowNum) -> new User(
                            rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("password")));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User findOne(Long id) {
        try {
            return (User) jdbcTemplate.queryForObject(
                    "SELECT login, password FROM \"users\" WHERE id = ?",
                    new Object[]{id},
                    (RowMapper<Object>) (rs, rowNum) -> new User(
                            id,
                            rs.getString("login"),
                            rs.getString("password")));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return jdbcTemplate
                .queryForList("SELECT id, login, password FROM \"users\"")
                .stream()
                .map(row -> new User(
                        new Long((Integer) row.get("id")),
                        (String) row.get("login"),
                        (String) row.get("password")))
                .collect(Collectors.toList());
    }

    @Override
    public void update(User user) {
        delete(user);
        save(user);
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update(
                "DELETE FROM \"users\" WHERE id = ?",
                user.getId()
        );
    }

    @Override
    public void save(User user) {
        if (user.getId() == null || !this.exists(user.getId())) {
            jdbcTemplate.update(
                    "INSERT INTO \"users\" (login, password) VALUES (?, ?)",
                    user.getUsername(),
                    user.getPassword());
        } else {
            update(user);
        }
    }

}
