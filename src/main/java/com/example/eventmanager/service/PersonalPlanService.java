package com.example.eventmanager.service;

import static org.quartz.JobKey.jobKey;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.example.eventmanager.domain.PersonalPlanJobDescriptor;

import com.example.eventmanager.domain.PersonalPlanTriggerDescriptor;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonalPlanService {

    private final Scheduler scheduler;
    private final Logger logger = LogManager.getLogger(PersonalPlanService.class);
    private final PlanSettingService planSettingService;

    @Autowired
    public PersonalPlanService(Scheduler scheduler, PlanSettingService planSettingService) {
        this.scheduler = scheduler;
        this.planSettingService = planSettingService;
    }


    public PersonalPlanJobDescriptor createJob(Long id) {

        PersonalPlanTriggerDescriptor planTriggerDescriptor = new PersonalPlanTriggerDescriptor();
        planTriggerDescriptor.setPlanSetting(planSettingService.getPlanSetting(id));
        PersonalPlanJobDescriptor planJobDescriptor = new PersonalPlanJobDescriptor();
        planJobDescriptor.setPlanTriggerDescriptor(Collections.singletonList(planTriggerDescriptor));

        JobDetail jobDetail = planJobDescriptor.buildJobDetail();
        Set<Trigger> triggersForJob = planJobDescriptor.buildTriggers();
        logger.info("About to save job with key - { "+jobDetail.getKey()+" }" );
        try {
            scheduler.scheduleJob(jobDetail, triggersForJob, true);
            scheduler.getJobDetail(jobDetail.getKey());
            logger.info("Job with key - { "+jobDetail.getKey()+" } saved sucessfully", jobDetail.getKey());
        } catch (SchedulerException e) {
            logger.error("Could not save job with key - { "+jobDetail.getKey()+" } due to error - { "+e.getLocalizedMessage()+" }");
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
        return planJobDescriptor;
    }

}
