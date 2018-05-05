package com.example.eventmanager.dao;

import com.example.eventmanager.domain.PlanSetting;
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
public class PlanSettingRepository implements CrudRepository<PlanSetting> {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;

    @Autowired
    public PlanSettingRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }

    @Override
    public int save(PlanSetting plan) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", plan.getUser().getId());
        namedParams.put("sendPlan", plan.isSendPlan());
        namedParams.put("from", plan.getFromDate());
        namedParams.put("planPeriod", plan.getPlanPeriod());
        namedParams.put("notificationPeriod", plan.getNotificationPeriod());
        return namedJdbcTemplate.update(env.getProperty("plan.insert"), namedParams);

    }

    @Override
    public PlanSetting findOne(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return namedJdbcTemplate.query(env.getProperty("plan.findById"), namedParams,new PersonalPlanExtractor());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<PlanSetting> findAll() {
        return Collections.emptyList();
    }

    @Override
    public void update(PlanSetting plan) {

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("sendPlan", plan.isSendPlan());
        namedParams.put("from", plan.getFromDate());
        namedParams.put("planPeriod", plan.getPlanPeriod());
        namedParams.put("notificationPeriod", plan.getNotificationPeriod());
        namedParams.put("user_id", plan.getUser().getId());
        namedJdbcTemplate.update(env.getProperty("plan.update"), namedParams);


    }

    @Override
    public void delete(PlanSetting entity) {

    }

    private static final class PersonalPlanExtractor implements ResultSetExtractor<PlanSetting> {

        @Override
        public PlanSetting extractData(ResultSet rs) throws SQLException {
            PlanSetting plan = new PlanSetting();
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
