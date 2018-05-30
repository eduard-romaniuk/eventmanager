package com.example.eventmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventmanager.dao.MessageRepository;
import com.example.eventmanager.domain.Message;
import com.example.eventmanager.domain.MessageWrapper;
import com.example.eventmanager.domain.User;

@Service
public class MessageService {
	private MessageRepository msgRepository;
	private final UserService userService;
	private final ChatService chatService;

	@Autowired
	public MessageService(MessageRepository msgRepository, UserService userService, ChatService chatService) {
		this.msgRepository = msgRepository;
		this.userService = userService;
		this.chatService = chatService;
	}

	public void saveMessage(String text, String creator, Long eventId, Long userId, String date) {
		Message msg = new Message();
		msg.setChatId(getChatId(eventId,creator));
		msg.setDate(date);
		msg.setParticipantId(findParticipantId(userId, eventId));
		msg.setText(text);
		msgRepository.save(msg);
	}

	public List<Message> getAllMessagesFromChat(Long chatId) {
		Iterator<Message> iter = msgRepository.findAllFromChat(chatId).iterator();
		List<Message> copy = new ArrayList<>();
		while (iter.hasNext())
			copy.add(iter.next());
		return copy;
	}

	public Long findParticipantId(Long userId, Long eventId) {
		return msgRepository.findParticipantId(userId, eventId);
	}

	public Long findUserIdFromParticipant(Long participantId) {
		return msgRepository.findUserIdFromParticipant(participantId);
	}
	
	public MessageWrapper prepareMessage(Long userId,String messageToSend, String date){
		User user = userService.getUser(userId); 
		String login = user.getLogin();
		String image = user.getImage();
		if(image==null) {
			if(user.getSex()){
				image = "../../../assets/images/default_user_male.png";
			}else{
				image = "../../../assets/images/default_user_female.png";
			}
		}
		MessageWrapper msg = new MessageWrapper();
		msg.setLogin(login);
		msg.setImage(image);
		msg.setText(messageToSend);
		msg.setDate(date);
		return msg;
	}
	
	public Long getChatId(Long eventId, String creator){
		if (creator.equals("withCreator")) {
			return chatService.findChatId(eventId, true);
		} else {
			return chatService.findChatId(eventId, false);
		}
	}
}
