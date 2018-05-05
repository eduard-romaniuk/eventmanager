package com.example.eventmanager.service;

import java.util.Collections;
import java.util.Set;

import com.example.eventmanager.domain.PersonalPlanJobDescriptor;

import com.example.eventmanager.domain.PersonalPlanTriggerDescriptor;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonalPlanService {

    private final Scheduler scheduler;
    private final Logger logger = LogManager.getLogger(PersonalPlanService.class);
    private final PlanSettingService planSettingService;
    private final UserService userService;

    @Autowired
    public PersonalPlanService(Scheduler scheduler, PlanSettingService planSettingService, UserService userService) {
        this.scheduler = scheduler;
        this.planSettingService = planSettingService;
        this.userService = userService;
    }

    public void createJob(User user) {
        //user = userService.getCurrentUser();
        PersonalPlanTriggerDescriptor planTriggerDescriptor = new PersonalPlanTriggerDescriptor();
        planTriggerDescriptor.setPlanSetting(planSettingService.getPlanSetting(user.getId()));
        PersonalPlanJobDescriptor planJobDescriptor = new PersonalPlanJobDescriptor();
        planJobDescriptor.setPlanTriggerDescriptor(Collections.singletonList(planTriggerDescriptor));
        planJobDescriptor.setUser_id(user.getId());

        JobDetail jobDetail = planJobDescriptor.buildJobDetail();
        Set<Trigger> triggersForJob = planJobDescriptor.buildTriggers();
        logger.info("About to save job with key - {}", jobDetail.getKey());
        try {
            scheduler.scheduleJob(jobDetail, triggersForJob, true);
            scheduler.getJobDetail(jobDetail.getKey());
            logger.info("Job with key - {} saved sucessfully", jobDetail.getKey());
        } catch (SchedulerException e) {
            logger.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

    @Transactional
    public void updateJob(User user) {
        user = userService.getCurrentUser();
        deleteJob(user);
        createJob(user);
        logger.info("Updated job with key - {}",userService.getCurrentUser().getId());
    }

    public void deleteJob(User user) {
        String name = "Job for user " + user.getId();
        try {
            scheduler.deleteJob(JobKey.jobKey(name));
            logger.info("Deleted job with key - {}", name);
        } catch (SchedulerException e) {
            logger.error("Could not delete job with key - {} due to error - {}", name, e.getLocalizedMessage());
        }
    }

}
