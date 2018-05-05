package com.example.eventmanager.dao;

import com.example.eventmanager.domain.PersonalPlanSetting;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@PropertySource("classpath:queries/plan-setting.properties")
@Repository
public class PersonalPlanSettingRepository implements CrudRepository<PersonalPlanSetting> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;

    @Autowired
    public PersonalPlanSettingRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    @Override
    public int save(PersonalPlanSetting plan) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", plan.getUser().getId());
        namedParams.put("sendPlan", plan.isSendPlan());
        namedParams.put("from", plan.getFromDate());
        namedParams.put("planPeriod", plan.getPlanPeriod());
        namedParams.put("notificationPeriod", plan.getNotificationPeriod());
        return namedJdbcTemplate.update(env.getProperty("plan.insert"), namedParams);

    }

    @Override
    public PersonalPlanSetting findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return namedJdbcTemplate.query(env.getProperty("plan.findById"), namedParams,new PersonalPlanExtractor());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<PersonalPlanSetting> findAll() {
        return Collections.emptyList();
    }

    @Override
    public void update(PersonalPlanSetting plan) {

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("sendPlan", plan.isSendPlan());
        namedParams.put("fromDate", plan.getFromDate());
        namedParams.put("planPeriod", plan.getPlanPeriod());
        namedParams.put("notificationPeriod", plan.getNotificationPeriod());
        namedParams.put("user_id", plan.getUser().getId());
        namedJdbcTemplate.update(env.getProperty("plan.update"), namedParams);


    }

    @Override
    public void delete(PersonalPlanSetting entity) {

    }

    private static final class PersonalPlanExtractor implements ResultSetExtractor<PersonalPlanSetting> {

        @Override
        public PersonalPlanSetting extractData(ResultSet rs) throws SQLException {
            PersonalPlanSetting plan = new PersonalPlanSetting();
            User user = new User();
            while (rs.next()) {
                plan.setSendPlan(rs.getBoolean("personal_plan_notification"));
                plan.setFromDate(rs.getDate("from_date").toLocalDate());
                plan.setPlanPeriod(rs.getInt("plan_period"));
                plan.setNotificationPeriod(rs.getInt("notification_period"));
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                plan.setUser(user);
            }

            return plan;
        }
    }
}
