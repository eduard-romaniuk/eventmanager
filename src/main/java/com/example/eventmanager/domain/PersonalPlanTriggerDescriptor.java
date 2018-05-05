package com.example.eventmanager.domain;

import org.quartz.JobDataMap;
import org.quartz.Trigger;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import static java.time.ZoneId.systemDefault;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.ObjectUtils.isEmpty;


public class PersonalPlanTriggerDescriptor {

    private String name;
    private PlanSetting planSetting;
    private String cron;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanSetting getPlanSetting() {
        return planSetting;
    }

    public void setPlanSetting(PlanSetting planSetting) {
        this.planSetting = planSetting;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    private String buildCrone(){
        return "0 0 0 1/"+planSetting.getNotificationPeriod()+" * ? *";
    }

    private String buildName() {
        return "Trigger for user " + planSetting.getUser().getId();
    }

    public Trigger buildTrigger(){
        cron = buildCrone();
        if (!isEmpty(cron)) {
            if (!isValidExpression(cron))
                throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression");
            return newTrigger()
                    .withIdentity(buildName())
                    .withSchedule(cronSchedule("0 * * ? * *")
                            .withMisfireHandlingInstructionFireAndProceed()
                            .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                    .build();

        } else throw new IllegalStateException("unsupported trigger descriptor " + this);

    }


}
