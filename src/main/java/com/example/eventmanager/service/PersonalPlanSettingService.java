package com.example.eventmanager.service;

import com.example.eventmanager.dao.PersonalPlanSettingRepository;
import com.example.eventmanager.domain.PersonalPlanSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalPlanSettingService {

    private final PersonalPlanSettingRepository planSettingRepository;
    private final UserService userService;


    @Autowired
    public PersonalPlanSettingService(PersonalPlanSettingRepository planSettingRepository, UserService userService) {
        this.planSettingRepository = planSettingRepository;
        this.userService = userService;
    }

    public int createPlanSetting(PersonalPlanSetting plan){
        plan.setUser(userService.getCurrentUser());
        return planSettingRepository.save(plan);
    }

    public void updatePlanSetting(PersonalPlanSetting plan){
        planSettingRepository.update(plan);
    }

    public PersonalPlanSetting getPlanSetting(){
        return planSettingRepository.findOne(userService.getCurrentUser().getId());
    }

    public PersonalPlanSetting getPlanSetting(Long id ){
        return planSettingRepository.findOne(id);
    }
}
