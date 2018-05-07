package com.example.eventmanager.controller;


import com.example.eventmanager.domain.PersonalPlanSetting;
import com.example.eventmanager.service.PersonalPlanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users/plan")
public class PersonalPlanController {

    private final Logger logger = LogManager.getLogger(PersonalPlanController.class);

    private final PersonalPlanService planService;


    @Autowired
    public PersonalPlanController(PersonalPlanService planService) {
        this.planService = planService;

    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> updatePlanSetting(@RequestBody PersonalPlanSetting setting) {

        planService.updatePlanSetting(setting);
        planService.updateJob();
        logger.info("POST / update personal plan setting for user - {}",setting.getUser().getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<PersonalPlanSetting> getPlanSetting() {

        PersonalPlanSetting setting = planService.getPlanSetting();
        if (setting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("GET / personal plan setting for user - {}",setting.getUser().getId());
        return new ResponseEntity<>(setting, HttpStatus.OK);

    }
}
