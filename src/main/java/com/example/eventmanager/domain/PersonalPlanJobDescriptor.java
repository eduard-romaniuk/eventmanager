package com.example.eventmanager.domain;

import com.example.eventmanager.service.PersonalPlanSendEmailJob;
import lombok.Data;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Data
public class PersonalPlanJobDescriptor {

    private List<PersonalPlanTriggerDescriptor>  planTriggerDescriptors;


    public List<PersonalPlanTriggerDescriptor> getPlanTriggerDescriptors() {
        return planTriggerDescriptors;
    }

    public void setPlanTriggerDescriptor(List<PersonalPlanTriggerDescriptor> planTriggerDescriptors) {
        this.planTriggerDescriptors = planTriggerDescriptors;
    }

    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (PersonalPlanTriggerDescriptor triggerDescriptor : planTriggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }
        return triggers;
    }

    public JobDetail buildJobDetail() {

        return JobBuilder.newJob(PersonalPlanSendEmailJob.class)
                .withIdentity("Job for user " + planTriggerDescriptors.get(0).getPlanSetting().getUser().getId())
                .build();
    }


}
