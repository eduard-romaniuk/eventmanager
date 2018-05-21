package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventmanager.domain.Statistics;
import com.example.eventmanager.service.StatisticsService;
import com.example.eventmanager.service.UserService;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {
	private final StatisticsService statService;
	private final UserService userService;
	
	@Autowired
	public StatisticsController(StatisticsService statService, UserService userService){
		this.statService = statService;
		this.userService = userService;
	}
	
 	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Statistics> getStatistics() {
 		Statistics stat = new Statistics();
 		Long userId = userService.getCurrentUser().getId();
 		stat = statService.getStatistics(userId);
        return new ResponseEntity<>(stat, HttpStatus.OK);
    }
}
