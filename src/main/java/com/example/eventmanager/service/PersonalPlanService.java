package com.example.eventmanager.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.example.eventmanager.dao.PersonalPlanSettingRepository;
import com.example.eventmanager.domain.PersonalPlanJobDescriptor;

import com.example.eventmanager.domain.PersonalPlanSetting;
import com.example.eventmanager.domain.PersonalPlanTriggerDescriptor;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


@Service
@Transactional
public class PersonalPlanService {

    private final int LIMIT = 10;

    private final Scheduler scheduler;
    private final Logger logger = LogManager.getLogger(PersonalPlanService.class);
    private final PersonalPlanSettingRepository planSettingRepository;
    private final UserService userService;

    @Autowired
    public PersonalPlanService(Scheduler scheduler, PersonalPlanSettingRepository planSettingRepository, UserService userService) {
        this.scheduler = scheduler;
        this.planSettingRepository = planSettingRepository;
        this.userService = userService;
    }

    public void updatePlanSetting(PersonalPlanSetting setting) {
        planSettingRepository.update(setting);
    }


    public PersonalPlanSetting getPlanSetting() {

        return planSettingRepository.findOne(userService.getCurrentUser().getId());
    }

    public PersonalPlanSetting getPlanSetting(Long id ){

        return planSettingRepository.findOne(id);
    }
    public void updateJob() {
        Long user_id = userService.getCurrentUser().getId();
        deleteJob(user_id);

        PersonalPlanSetting setting = getPlanSetting(user_id);
        if (setting.isSendPlan()) {
            createJob(user_id);
            logger.info("Updated job with key - {}", user_id);
        } else logger.info("Sending plan disable");
    }

    private void createJob(Long user_id) {
        PersonalPlanTriggerDescriptor planTriggerDescriptor = new PersonalPlanTriggerDescriptor();
        planTriggerDescriptor.setPersonalPlanSetting(getPlanSetting(user_id));
        PersonalPlanJobDescriptor planJobDescriptor = new PersonalPlanJobDescriptor();
        planJobDescriptor.setPlanTriggerDescriptor(Collections.singletonList(planTriggerDescriptor));
        planJobDescriptor.setUser_id(user_id);

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

    private void deleteJob(Long user_id) {
        String name = "Job for user " + user_id;
        try {
            scheduler.deleteJob(JobKey.jobKey(name));
            logger.info("Deleted job with key - {}", name);
        } catch (SchedulerException e) {
            logger.error("Could not delete job with key - {} due to error - {}", name, e.getLocalizedMessage());
        }
    }

    @PostConstruct
    public void restartJobs(){

        logger.info("Starting jobs for users");

        int offset = 0;
        boolean hasActiveUsers = true;
        do{
            logger.info("offset - " + offset);

            List<User> activeUsers = userService.findAllActivePagination(LIMIT, offset);
            logger.info("activeUsers - " + activeUsers.toString());

            for (User user : activeUsers) {
                String name = "Job for user " + user.getId();
                if (getPlanSetting(user.getId()).isSendPlan()){
                    try {
                        if (!scheduler.checkExists(JobKey.jobKey(name))){

                            createJob(user.getId());

                        } else {
                            logger.info("Job with key - {} is already exist", name);
                        }

                    } catch (SchedulerException e) {
                        logger.error("Job with key - {} is already exist - {}", name, e.getLocalizedMessage());
                    }
                } else {
                    logger.info("Sending personal plan for user - {} disable",user.getId());
                }
            }

            offset += LIMIT;
            if (activeUsers.size() < LIMIT)
                hasActiveUsers = false;

        } while (hasActiveUsers);

        logger.info("End starting jobs");

    }

}
