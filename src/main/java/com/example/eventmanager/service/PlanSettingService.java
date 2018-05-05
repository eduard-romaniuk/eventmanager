package com.example.eventmanager.service;

import com.example.eventmanager.dao.PlanSettingRepository;
import com.example.eventmanager.domain.PlanSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanSettingService {

    private final PlanSettingRepository planSettingRepository;
    private final UserService userService;


    @Autowired
    public PlanSettingService(PlanSettingRepository planSettingRepository, UserService userService) {
        this.planSettingRepository = planSettingRepository;
        this.userService = userService;
    }

    public int createPlanSetting(PlanSetting plan){
        plan.setUser(userService.getCurrentUser());
        return planSettingRepository.save(plan);
    }

    public void updatePlanSetting(PlanSetting plan){
        planSettingRepository.update(plan);
    }

    public PlanSetting getPlanSetting(){
        return planSettingRepository.findOne(userService.getCurrentUser().getId());
    }

    public PlanSetting getPlanSetting(Long id ){
        return planSettingRepository.findOne(id);
    }
}
