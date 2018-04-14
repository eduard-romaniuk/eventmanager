package com.example.eventmanager.dao;


import com.example.eventmanager.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class UsersRepository implements CrudRepository<User> {

    private JdbcTemplate jdbcTemplate = JdbcTemplateSingleton.getInstance();

    public User findByUsername(String username) {
        return (User) jdbcTemplate.queryForObject(
                "SELECT id, username, password FROM \"users\" WHERE username = ?",
                new Object[]{username},
                (RowMapper<Object>) (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")));
    }

    @Override
    public User findOne(Long id) {
        return (User) jdbcTemplate.queryForObject(
                "SELECT username, password FROM \"users\" WHERE id = ?",
                new Object[]{id},
                (RowMapper<Object>) (rs, rowNum) -> new User(
                        id,
                        rs.getString("username"),
                        rs.getString("password")));
    }

    @Override
    public Iterable<User> findAll() {
        return jdbcTemplate
                .queryForList("SELECT id, username, password FROM \"users\"")
                .stream()
                .map(row -> new User(
                        new Long((Integer) row.get("id")),
                        (String) row.get("username"),
                        (String) row.get("password")))
                .collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO \"users\" (username, password) VALUES (?, ?)",
                user.getUsername(),
                user.getPassword());
    }

}
