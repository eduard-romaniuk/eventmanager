package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.PersonalPlanSetting;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonalPlanSendEmailJob implements Job {

    private final Logger logger = LogManager.getLogger(PersonalPlanSendEmailJob.class);

    @Autowired
    private  UserService userService;
    @Autowired
    private  PersonalPlanService planService;
    @Autowired
    private  EventService eventService;
    @Autowired
    private  ExportEventService exportEventService;



    public PersonalPlanSendEmailJob() {

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("Job with key -{} triggered to send a personal plan via email ",jobExecutionContext.getJobDetail().getKey());

        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        Long user_id = map.getLong("user_id");
        PersonalPlanSetting personalPlanSetting = planService.getPlanSetting(user_id);
        String email = userService.getUser(user_id).getEmail();

        LocalDate from = personalPlanSetting.getFromDate();
        LocalDate to = from.plusDays(personalPlanSetting.getPlanPeriod());

        List<Event> events = eventService.eventsForPeriod(user_id,from,to);
        JasperPrint eventsPlan = exportEventService.createEventsPlan(from, to, events);

        exportEventService.sendEventsPlan(email,eventsPlan,from,to);

        personalPlanSetting.setFromDate(to);
        planService.updatePlanSetting(personalPlanSetting);

        logger.info("Event from date for user {} was updating, current date : {}",user_id, personalPlanSetting.getFromDate());
        logger.info("Job with key -{} completed",jobExecutionContext.getJobDetail().getKey());

    }
}
