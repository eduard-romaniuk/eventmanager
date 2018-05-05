package com.example.eventmanager.domain;

import org.quartz.Trigger;

import java.util.TimeZone;

import static java.time.ZoneId.systemDefault;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.ObjectUtils.isEmpty;


public class PersonalPlanTriggerDescriptor {

    private String name;
    private PersonalPlanSetting personalPlanSetting;
    private String cron;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonalPlanSetting getPersonalPlanSetting() {
        return personalPlanSetting;
    }

    public void setPersonalPlanSetting(PersonalPlanSetting personalPlanSetting) {
        this.personalPlanSetting = personalPlanSetting;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    private String buildCrone(){
        return "0 */"+ getPersonalPlanSetting().getNotificationPeriod()+" * ? * *";
    }

    private String buildName() {
        return "Trigger for user " + personalPlanSetting.getUser().getId();
    }

    public Trigger buildTrigger(){
        cron = buildCrone();
        if (!isEmpty(cron)) {
            if (!isValidExpression(cron))
                throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression");
            return newTrigger()
                    .withIdentity(buildName())
                    .withSchedule(cronSchedule(cron)
                            .withMisfireHandlingInstructionFireAndProceed()
                            .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                    .build();

        } else throw new IllegalStateException("unsupported trigger descriptor " + this);

    }


}
