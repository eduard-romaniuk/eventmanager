package com.example.eventmanager.service;

import com.example.eventmanager.dao.PersonalPlanSettingRepository;
import com.example.eventmanager.domain.PersonalPlanSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalPlanSettingService {

    private final PersonalPlanSettingRepository planSettingRepository;

    @Autowired
    public PersonalPlanSettingService(PersonalPlanSettingRepository planSettingRepository, UserService userService) {
        this.planSettingRepository = planSettingRepository;

    }

    public void updatePlanSetting(PersonalPlanSetting plan){
        planSettingRepository.update(plan);
    }


    public PersonalPlanSetting getPlanSetting(Long id ){
        return planSettingRepository.findOne(id);
    }
}
