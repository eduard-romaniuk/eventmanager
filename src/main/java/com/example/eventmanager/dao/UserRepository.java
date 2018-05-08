package com.example.eventmanager.dao;

import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/user.properties")
@Repository
public class UserRepository implements CrudRepository<User> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(UserRepository.class);

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    private final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();

            user.setId(resultSet.getLong("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setName(resultSet.getString("name"));
            user.setSurName(resultSet.getString("surname"));
            user.setEmail(resultSet.getString("email"));
            user.setBirth(resultSet.getDate("birth") != null ? resultSet.getDate("birth").toLocalDate() : null);
            user.setPhone(resultSet.getString("phone"));
            user.setSex(resultSet.getBoolean("sex"));
            user.setImage(resultSet.getString("image"));
            user.setVerified(resultSet.getBoolean("is_active"));
            user.setRegDate(resultSet.getDate("reg_date").toLocalDate());
            user.setToken(resultSet.getString("conf_link"));

            return user;
        }
    }

    public User findByUsername(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            return namedJdbcTemplate.queryForObject(env.getProperty("findByUsername"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found");
            return null;
        }
    }

    @Override
    public User findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return namedJdbcTemplate.queryForObject(env.getProperty("findOne"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found");
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return namedJdbcTemplate.query(env.getProperty("findAllUser"), new UserMapper());
    }

    @Override
    public void update(User user) {
        System.out.println("UserRepository.update");
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", user.getLogin());
        namedParams.put("password", user.getPassword());
        namedParams.put("name", user.getName());
        namedParams.put("surname", user.getSurName());
        namedParams.put("email", user.getEmail());
        namedParams.put("birth", user.getBirth());
        namedParams.put("phone", (user.getPhone()== null || user.getPhone().equals("")) ?
                null : user.getPhone());
        namedParams.put("sex", user.getSex());
        namedParams.put("image", user.getImage());
        namedParams.put("is_active", user.getVerified());
        namedParams.put("id", user.getId());

        namedJdbcTemplate.update(env.getProperty("update"), namedParams);
    }

    @Override
    public void delete(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("id", user.getId());
        namedJdbcTemplate.update(env.getProperty("event.delete"), namedParams);
    }

    public void changePass(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("password", user.getPassword());
        namedParams.put("id", user.getId());

        namedJdbcTemplate.update(env.getProperty("changePassword"), namedParams);
    }

    @Override
    public int save(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", user.getLogin());
        namedParams.put("password", user.getPassword());
        namedParams.put("name", user.getName());
        namedParams.put("surname", user.getSurName());
        namedParams.put("email", user.getEmail());
        namedParams.put("birth", user.getBirth());
        namedParams.put("phone", user.getPhone());
        namedParams.put("sex", user.getSex());
        namedParams.put("image", user.getImage());
        namedParams.put("is_active", user.getVerified());
        namedParams.put("reg_date", LocalDate.now());
        namedParams.put("conf_link", user.getToken());

        return namedJdbcTemplate.update(env.getProperty("save"), namedParams);
    }

    public boolean isUsernameExists(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            namedJdbcTemplate.queryForObject(env.getProperty("isUsernameExists"), namedParams, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("Username not found");
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("email", email);
            namedJdbcTemplate.queryForObject(env.getProperty("isEmailExists"), namedParams, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("Email not found");
            return false;
        }
    }

    public List<User> searchByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", "%" + login.toLowerCase().trim() + "%");
            return namedJdbcTemplate.query(env.getProperty("searchUserByLogin"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found");
            return null;
        }
    }

    public List<User> findAllActive() {
        return namedJdbcTemplate.query(env.getProperty("findAllActiveUsers"), new UserMapper());
    }

    public List<User> findAllActivePagination(int limit, int offset) {
        String query = new StringBuilder()
                .append(env.getProperty("findAllActiveUsers"))
                .append(" LIMIT ")
                .append(limit)
                .append(" OFFSET ")
                .append(offset)
                .toString();
        return namedJdbcTemplate.query(query, new UserMapper());
    }

    public User findByEmail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("email", email);
            return namedJdbcTemplate.queryForObject(env.getProperty("findUserByEmail"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found");
            return null;
        }
    }

    //Friends functionality

    public int saveRelationship(Long userOneId, Long userTwoId, int statusId, Long actionUserId) {
        logger.info("Save relationship for user with id {} and user with id {} with status id {}",
                userOneId, userTwoId, statusId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_one_id", userOneId);
        namedParams.put("user_two_id", userTwoId);
        namedParams.put("status_id", statusId);
        namedParams.put("action_user_id", actionUserId);

        return namedJdbcTemplate.update(env.getProperty("saveRelationship"), namedParams);
    }

    public void updateRelationship(Long userOneId, Long userTwoId, int statusId, Long actionUserId) {
        logger.info("Update relationship for user with id {} and user with id {} with status id {}",
                userOneId, userTwoId, statusId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_one_id", userOneId);
        namedParams.put("user_two_id", userTwoId);
        namedParams.put("status_id", statusId);
        namedParams.put("action_user_id", actionUserId);

        namedJdbcTemplate.update(env.getProperty("updateRelationship"), namedParams);
    }

    public void deleteRelationship(Long userOneId, Long userTwoId) {
        logger.info("Delete relationship for user with id {} and user with id {}", userOneId, userTwoId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_one_id", userOneId);
        namedParams.put("user_two_id", userTwoId);

        namedJdbcTemplate.update(env.getProperty("deleteRelationship"), namedParams);
    }

    public String getRelationshipStatus(Long userOneId, Long userTwoId) {
        try {
            logger.info("Get relationship status for user with id {} and user with id {}", userOneId, userTwoId);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_one_id", userOneId);
            namedParams.put("user_two_id", userTwoId);

            return namedJdbcTemplate.queryForObject(env.getProperty("getRelationshipStatus"), namedParams, String.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User with id {} and user with id {} are not friends", userOneId, userTwoId);
            return "";
        }
    }

    public Map<String, Object> getRelationshipStatusAndActiveUserId(Long userOneId, Long userTwoId) {
        try {
            logger.info("Get relationship status and active user id for user with id {} and user with id {}", userOneId, userTwoId);

            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_one_id", userOneId);
            namedParams.put("user_two_id", userTwoId);

            return namedJdbcTemplate.queryForMap(env.getProperty("getRelationshipStatusAndActiveUserId"),
                    namedParams);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User with id {} and user with id {} are not friends", userOneId, userTwoId);

            Map <String, Object> noRelationshipMap = new HashMap<>();
            noRelationshipMap.put("", 0);
            return noRelationshipMap;
        }
    }

    public List<User> getOutcomingRequests(Long userId){
        logger.info("Get outcoming requests for user with id {}", userId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);

        return namedJdbcTemplate.query(env.getProperty("getOutcomingRequests"), namedParams, new UserMapper());
    }

    public List<User> getIncomingRequests(Long userId){
        logger.info("Get incoming requests for user with id {}", userId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);

        return namedJdbcTemplate.query(env.getProperty("getIncomingRequests"), namedParams, new UserMapper());
    }

    public List<User> getFriendsByUserId(Long userId){
        logger.info("Get friends for user with id {}", userId);

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);

        return namedJdbcTemplate.query(env.getProperty("getFriendsByUserId"), namedParams, new UserMapper());
    }
}



