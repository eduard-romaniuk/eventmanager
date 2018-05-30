package com.example.eventmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventmanager.dao.ChatRepository;

@Service
public class ChatService {
	private ChatRepository chatRepository;

	@Autowired
	public ChatService(ChatRepository chatRepository) {
		this.chatRepository = chatRepository;
	}
	
	public Long findChatId(Long eventId, boolean withCreator){
		return chatRepository.findChatId(eventId, withCreator);
	}
	

}
