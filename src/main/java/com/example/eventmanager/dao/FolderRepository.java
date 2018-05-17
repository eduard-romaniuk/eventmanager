package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Folder;
import com.example.eventmanager.domain.Member;
import com.example.eventmanager.domain.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@PropertySource("classpath:queries/folders.properties")
@Repository
public class FolderRepository implements CrudRepository<Folder> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(FolderRepository.class);
    private static final int FIRST_ELEMENT = 0;

    @Autowired
    public FolderRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }



    @Override
    public int save(Folder folder) {
        logger.info("Saving folder");
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("folder_name", folder.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(env.getProperty("folder.saveFolder"), namedParams, keyHolder);
        logger.info("Created folder with id: " + keyHolder.getKeys().get("id"));
        folder.setId(new Long((int)keyHolder.getKeys().get("id")));

        namedParams = new MapSqlParameterSource();
        namedParams.addValue("folderId", folder.getId());
        namedParams.addValue("userId", folder.getCreator().getId());
        namedParams.addValue("isCreator", true);
        namedJdbcTemplate.update(env.getProperty("folder.connectFolderToUser"), namedParams, keyHolder);

        return folder.getId().intValue();
    }

    public List<Folder> findByUser(Long userId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("userId", userId);
            return namedJdbcTemplate.query(env.getProperty("folder.findAllFoldersByUser"), namedParams, new FolderMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folders not found");
            return Collections.emptyList();
        } catch (IndexOutOfBoundsException e) {
            logger.info("Folder not found");
            return null;
        }
    }

    @Override
    public void update(Folder folder) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("folderName", folder.getName());
        namedJdbcTemplate.update(env.getProperty("folder.updateFolder"), namedParams);
    }

    @Override
    @Transactional
    public void delete(Folder folder) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("folderId", folder.getId());
        namedJdbcTemplate.update(env.getProperty("note.moveNotesToRoot"),namedParams);
        namedJdbcTemplate.update(env.getProperty("folder.delete.userConnection"), namedParams);
        namedJdbcTemplate.update(env.getProperty("folder.delete"), namedParams);
    }

    @Override
    public Iterable<Folder> findAll() {
        try {
            return namedJdbcTemplate.query(env.getProperty("folder.findAllFolders"), new FolderMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folders not found");
            return Collections.emptyList();
        } catch (IndexOutOfBoundsException e) {
            logger.info("Folder not found");
            return null;
        }
    }

    @Override
    public Folder findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return namedJdbcTemplate.query(env.getProperty("folder.findFolderById"), namedParams, new FolderMapper()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folder not found");
            return null;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Folder not found");
            return null;
        }
    }

    public Folder findByIdAndUserId(Long folderId, Long userId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("folderId", folderId);
            namedParams.put("userId", userId);
            Folder folder = namedJdbcTemplate.query(env.getProperty("folder.findFolderByIdAndUser"), namedParams, new FolderMapper()).get(FIRST_ELEMENT);
            if(folder != null) {
                folder.setCreator(namedJdbcTemplate.query(env.getProperty("folder.findFolderCreator"), namedParams, new UserMapper()).get(FIRST_ELEMENT));
            }
            logger.info("Loaded folder: " + folder);
            return folder;
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folder not found");
            return null;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Folder not found");
            return null;
        }
    }

    public List<Member> getAllMembers(Long folderId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("folderId", folderId);
            return namedJdbcTemplate.query(env.getProperty("folder.findAllMembers"), namedParams, new MemberMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Members not found");
            return null;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Members not found");
            return null;
        }
    }

    private static final class FolderMapper implements RowMapper<Folder> {
        private final Logger logger = LogManager.getLogger(FolderMapper.class);

        @Override
        public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
            Folder folder = new Folder();
            folder.setId(rs.getLong("id"));
            folder.setName(rs.getString("folder_name"));
            logger.info("Loaded Folder:" + folder);
            return folder;
        }
    }

    private static final class UserMapper implements RowMapper<User> {
        private final Logger logger = LogManager.getLogger(UserMapper.class);

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            logger.info("Loaded user:" + user);
            return user;
        }
    }

    private static final class MemberMapper implements RowMapper<Member> {
        private final Logger logger = LogManager.getLogger(MemberMapper.class);

        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLogin(rs.getString("login"));
            member.setIsMember(rs.getBoolean("isMember"));
            logger.info("Loaded member:" + member);
            return member;
        }
    }
}
