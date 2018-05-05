package com.example.eventmanager.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalPlanSendEmailJob implements Job {

    private final Logger logger = LogManager.getLogger(PersonalPlanSendEmailJob.class);
    @Autowired
    private EmailService emailService;

    public PersonalPlanSendEmailJob() {

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("Job with key -{} triggered to send a personal plan via email ",jobExecutionContext.getJobDetail().getKey());
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        emailService.sendPersonalPlanNotification(map.getLong("user_id"));
        logger.info("Job with key -{} completed",jobExecutionContext.getJobDetail().getKey());

    }
}
