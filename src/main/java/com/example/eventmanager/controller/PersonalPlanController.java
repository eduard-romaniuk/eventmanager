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
@RequestMapping(value = "/user{id}/plan")
public class PersonalPlanController {

    private final Logger logger = LogManager.getLogger(PersonalPlanController.class);

    private final PersonalPlanService planService;

    @Autowired
    public PersonalPlanController(PersonalPlanService planService) {
        this.planService = planService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> createPlanSetting(@PathVariable Long id, @RequestBody PersonalPlanSetting setting) {


        logger.info("POST / create personal plan setting for user{}",id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
