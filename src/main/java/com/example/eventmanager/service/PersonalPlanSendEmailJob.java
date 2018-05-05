package com.example.eventmanager.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

@Service
public class PersonalPlanSendEmailJob implements Job {

    private final Logger logger = LogManager.getLogger(PersonalPlanSendEmailJob.class);

    public PersonalPlanSendEmailJob() {

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("Job triggered to send a personal plan via email ");
        //JobDataMap map = jobExecutionContext.getMergedJobDataMap();
       // emailService.sendPersonalPlanNotification();
        logger.info("Job completed");

    }
}
