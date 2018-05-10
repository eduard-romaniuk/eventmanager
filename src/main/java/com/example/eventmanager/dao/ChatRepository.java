package com.example.eventmanager.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.eventmanager.domain.Message;

@PropertySource("classpath:queries/messages.properties")
@Repository
public class ChatRepository implements CrudRepository<Message> {

	 private final NamedParameterJdbcTemplate namedJdbcTemplate;
	    private final Environment env;
	    private final Logger logger = LogManager.getLogger(MessageRepository.class);
	
	    @Autowired
	    public ChatRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
	        logger.info("Class initialized");
	
	        this.namedJdbcTemplate = namedJdbcTemplate;
	        this.env = env;
	    }
	    
	    public Long findChatId(Long eventId, boolean withCreator){
	    	Map<String, Object> namedParams = new HashMap<>();
	    	namedParams.put("event_id", eventId);
	    	namedParams.put("with_creator", withCreator);
	    	return namedJdbcTemplate.queryForObject(env.getProperty("findChatId"), namedParams, Long.class);
	    }
	    
		@Override
		public int save(Message entity) {
			return 0;
		}
	
		@Override
		public Message findOne(Long id) {
			return null;
		}
	
		@Override
		public Iterable<Message> findAll() {
			return null;
		}
	
		@Override
		public void update(Message entity) {
		}
	
		@Override
		public void delete(Message entity) {
		}
		
}
