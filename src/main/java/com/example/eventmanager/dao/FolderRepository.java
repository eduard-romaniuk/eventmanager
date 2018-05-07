package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Folder;
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
        namedJdbcTemplate.update(env.getProperty("saveFolder"), namedParams, keyHolder);
        return (Integer)keyHolder.getKeys().get("id");
    }

    @Override
    public void update(Folder folder) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("folder_name", folder.getName());
        namedJdbcTemplate.update(env.getProperty("updateFolder"), namedParams);
    }

    @Override
    public void delete(Folder folder) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("eventId", folder.getId());
        namedJdbcTemplate.update(env.getProperty("deleteFolder"), namedParams);
    }

    @Override
    public Iterable<Folder> findAll() {
        try {
            return namedJdbcTemplate.query(env.getProperty("findAllFolders"), new FolderMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folders not found");
            return Collections.emptyList();
        }
    }

    @Override
    public Folder findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return namedJdbcTemplate.query(env.getProperty("findFolderById"), namedParams, new FolderMapper()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Folder not found");
            return null;
        }
    }

    private static final class FolderMapper implements RowMapper<Folder> {
        @Override
        public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
            Folder folder = new Folder();
            folder.setId(rs.getLong("id"));
            folder.setName(rs.getString("folder_name"));
            return folder;
        }
    }
}
