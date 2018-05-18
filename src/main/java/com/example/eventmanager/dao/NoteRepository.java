package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Note;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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


@PropertySource("classpath:queries/note.properties")
@Repository
public class NoteRepository implements CrudRepository<Note> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final Logger logger = LogManager.getLogger(NoteRepository.class);
    private static final int FIRST_ELEMENT = 0;

    @Autowired
    public NoteRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }


    public List<Note> findByCreator(Long creatorId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("creator_id", creatorId);
            return namedJdbcTemplate.query(env.getProperty("note.findByCreator"), namedParams, new NoteMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Note not found");
            return Collections.emptyList();
        } catch (IndexOutOfBoundsException e) {
            logger.info("Note not found");
            return null;
        }
    }

    @Override
    public int save(Note note) {
        logger.info("Saving note");
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("creator_id", note.getCreator().getId());
        namedParams.addValue("name", note.getName());
        namedParams.addValue("description", note.getDescription());
        namedParams.addValue("image", note.getImage());
        namedParams.addValue("folder_id", note.getFolder() == null? null: note.getFolder().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(env.getProperty("note.save"), namedParams, keyHolder);
        return (Integer)keyHolder.getKeys().get("id");
    }

    @Override
    public Note findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("noteId", id);
            return namedJdbcTemplate.query(env.getProperty("note.findById"), namedParams, new NoteMapper()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Note not found");
            return null;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Note not found");
            return null;
        }
    }

    @Override
    public Iterable<Note> findAll() {
        try {
            return namedJdbcTemplate.query(env.getProperty("note.findAllNotes"), new NoteMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Notes not found");
            return Collections.emptyList();
        } catch (IndexOutOfBoundsException e) {
            logger.info("Notes not found");
            return null;
        }
    }

    @Override
    public void update(Note note) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("name", note.getName());
        namedParams.put("description", note.getDescription());
        namedParams.put("image", note.getImage());
        namedParams.put("eventId", note.getId());
        namedJdbcTemplate.update(env.getProperty("note.updateNote"), namedParams);
    }

    @Override
    @Transactional
    public void delete(Note entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("noteId", entity.getId());
        namedJdbcTemplate.update(env.getProperty("note.delete.note"), namedParams);
    }

    public List<Note> findAllFolderNotes(Long folderId, Long currentUserId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("folderId", folderId);
        namedParams.put("userId", currentUserId);
        try {
            if(folderId == 0) { //root folder
                return namedJdbcTemplate.query(env.getProperty("note.findAllRootFolderNotes"), namedParams, new NoteMapper());
            }
            else {  //not root folder
                return namedJdbcTemplate.query(env.getProperty("note.findAllFolderNotes"), namedParams, new NoteMapper());
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("Notes not found");
            return Collections.emptyList();
        } catch (IndexOutOfBoundsException e) {
            logger.info("Notes not found");
            return null;
        }
    }

    public static final class NoteMapper implements RowMapper<Note> {
        private final Logger logger = LogManager.getLogger(NoteMapper.class);
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setId(rs.getLong("id"));
            note.setName(rs.getString("name"));
            note.setDescription(rs.getString("description"));
            note.setImage(rs.getString("image"));
            logger.info("Loaded Note: " + note);
            return note;
        }
    }
}
