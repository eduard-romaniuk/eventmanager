package com.example.eventmanager.dao;


import com.example.eventmanager.domain.Folder;
import com.example.eventmanager.domain.Note;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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

    private final DefaultTransactionDefinition def;
    private DataSourceTransactionManager txManager;

    private TransactionStatus updateStatus;

    @Autowired
    public NoteRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env, JdbcTemplate jdbcTemplate) {
        logger.info("Class initialized");

        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;

        this.txManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());

        this.def = new DefaultTransactionDefinition();
        def.setName("UpdateNote");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setTimeout(60);
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
            return namedJdbcTemplate.query(env.getProperty("note.findById"), namedParams, new NoteWithCreatorAndFolderMapper()).get(FIRST_ELEMENT);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Note not found");
            return null;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Note not found");
            return null;
        }
    }

    public Note findOneForUpdate(Long id) {
        //updateStatus = txManager.getTransaction(def);
        logger.info("find note for update");
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("noteId", id);
            return namedJdbcTemplate.query(env.getProperty("note.findByIdForUpdate"), namedParams, new NoteWithCreatorAndFolderMapper()).get(FIRST_ELEMENT);
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
        logger.info("Update note");
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("name", note.getName());
        namedParams.put("description", note.getDescription());
        namedParams.put("image", note.getImage());
        namedParams.put("noteId", note.getId());

        namedJdbcTemplate.update(env.getProperty("note.updateNote"), namedParams);
        txManager.commit(updateStatus);
        //logger.info("END OF Update note");
    }

    public void moveNote(Note note) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("noteId", note.getId());
        namedParams.put("folderId", note.getFolder().getId());
        namedJdbcTemplate.update(env.getProperty("note.moveNote"), namedParams);
    }

    @Override
    public void delete(Note entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("noteId", entity.getId());
        namedJdbcTemplate.update(env.getProperty("note.delete"), namedParams);
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
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setId(rs.getLong("id"));
            note.setName(rs.getString("name"));
            note.setDescription(rs.getString("description"));
            note.setImage(rs.getString("image"));
            return note;
        }
    }

    public static final class NoteWithCreatorAndFolderMapper implements RowMapper<Note> {
        private final Logger logger = LogManager.getLogger(NoteWithCreatorAndFolderMapper.class);
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setId(rs.getLong("id"));
            note.setName(rs.getString("name"));
            note.setDescription(rs.getString("description"));
            note.setImage(rs.getString("image"));
            User creator = new User();
            creator.setId(rs.getLong("creator_id"));
            creator.setName(rs.getString("creator_name"));
            note.setCreator(creator);
            Folder folder = new Folder();
            folder.setId(rs.getLong("folder_id"));
            note.setFolder(folder);
            logger.info("Loaded Note: " + note);
            return note;
        }
    }
}
