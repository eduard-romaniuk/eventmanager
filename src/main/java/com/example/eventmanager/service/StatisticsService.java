package com.example.eventmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventmanager.dao.StatisticsRepository;
import com.example.eventmanager.domain.Statistics;

@Service
public class StatisticsService {
	private final StatisticsRepository statRepository;
	
	@Autowired
	public StatisticsService(StatisticsRepository statRepository) {
		this.statRepository = statRepository;
	}

	public Statistics getStatistics(Long userId){
		return statRepository.getAllStatistics(userId);
	}
}
