package com.example.eventmanager.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.eventmanager.domain.Statistics;

@PropertySource("classpath:queries/statistics.properties")
@Repository
public class StatisticsRepository {
	private final NamedParameterJdbcTemplate namedJdbcTemplate;
	private final Environment env;
	 
	@Autowired
    public StatisticsRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }
	
	public Statistics getAllStatistics(Long userId){
		Statistics stat = new Statistics();
		Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("users_id", userId);
        stat.setAsCreator(namedJdbcTemplate.queryForObject(env.getProperty("countAsCreator"), namedParams, Long.class));
		stat.setAsParticipant(namedJdbcTemplate.queryForObject(env.getProperty("countAsParticipant"), namedParams, Long.class));
		stat.setAvgAge(namedJdbcTemplate.queryForObject(env.getProperty("countAvgAge"), namedParams, String.class));
		stat.setAvgAge(stat.getAvgAge().substring(0, stat.getAvgAge().indexOf("day")+4));
		stat.setFemales(namedJdbcTemplate.queryForObject(env.getProperty("countFemale"), namedParams, Long.class));
		stat.setMales(namedJdbcTemplate.queryForObject(env.getProperty("countMale"), namedParams, Long.class));
		System.out.println(stat.getMales()+" "+ stat.getFemales());
		stat.setMales(stat.getMales()*100/(stat.getMales()+stat.getFemales()));
		stat.setFemales(100-stat.getMales());
		stat.setLikes(namedJdbcTemplate.queryForObject(env.getProperty("countLikes"), namedParams, Long.class));
		stat.setMessagesSent(namedJdbcTemplate.queryForObject(env.getProperty("countMessages"), namedParams, Long.class));
		stat.setWithUs(namedJdbcTemplate.queryForObject(env.getProperty("countWithUs"), namedParams, String.class));
		return stat;
	}
	
}
