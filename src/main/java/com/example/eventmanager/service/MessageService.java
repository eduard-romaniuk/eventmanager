package com.example.eventmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventmanager.dao.MessageRepository;
import com.example.eventmanager.domain.Message;

@Service
public class MessageService {
	private MessageRepository msgRepository;

	@Autowired
	public MessageService(MessageRepository msgRepository) {
		this.msgRepository = msgRepository;
	}

	public void saveMessage(Message msg) {
		msgRepository.save(msg);
	}

	public List<Message> getAllMessagesFromChat(Long chatId) {
		Iterator<Message> iter = msgRepository.findAllFromChat(chatId).iterator();
		List<Message> copy = new ArrayList<>();
		while (iter.hasNext())
			copy.add(iter.next());
		return copy;
	}
	public Long findParticipantId(Long userId, Long eventId){
		return msgRepository.findParticipantId(userId, eventId);
	}
	public Long findUserIdFromParticipant(Long participantId){
		return msgRepository.findUserIdFromParticipant(participantId);
	}
}

