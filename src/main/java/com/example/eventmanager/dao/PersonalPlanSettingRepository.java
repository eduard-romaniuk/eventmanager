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
    public int save(PersonalPlanSetting planSetting) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", planSetting.getUser().getId());
        namedParams.put("sendPlan", planSetting.isSendPlan());
        namedParams.put("from", planSetting.getFromDate());
        namedParams.put("planPeriod", planSetting.getPlanPeriod());
        namedParams.put("notificationPeriod", planSetting.getNotificationPeriod());
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
    public void update(PersonalPlanSetting planSetting) {

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("sendPlan", planSetting.isSendPlan());
        namedParams.put("fromDate", planSetting.getFromDate());
        namedParams.put("planPeriod", planSetting.getPlanPeriod());
        namedParams.put("notificationPeriod", planSetting.getNotificationPeriod());
        namedParams.put("user_id", planSetting.getUser().getId());
        namedJdbcTemplate.update(env.getProperty("plan.update"), namedParams);


    }

    @Override
    public void delete(PersonalPlanSetting entity) {

    }

    private static final class PersonalPlanExtractor implements ResultSetExtractor<PersonalPlanSetting> {

        @Override
        public PersonalPlanSetting extractData(ResultSet rs) throws SQLException {
            PersonalPlanSetting setting = new PersonalPlanSetting();
            User user = new User();
            while (rs.next()) {
                setting.setSendPlan(rs.getBoolean("personal_plan_notification"));
                setting.setFromDate(rs.getDate("from_date").toLocalDate());
                setting.setPlanPeriod(rs.getInt("plan_period"));
                setting.setNotificationPeriod(rs.getInt("notification_period"));
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                setting.setUser(user);
            }

            return setting;
        }
    }
}
