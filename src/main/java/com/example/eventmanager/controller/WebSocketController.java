package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventmanager.domain.Message;
import com.example.eventmanager.service.ChatService;
import com.example.eventmanager.service.MessageService;
import com.example.eventmanager.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/chats")
public class WebSocketController {
	private final SimpMessagingTemplate template;
	private final UserService userService;
	private final MessageService msgService;
	private final ChatService chatService;

	@Autowired
	WebSocketController(SimpMessagingTemplate template, UserService userService, MessageService msgService,
			ChatService chatService) {
		this.template = template;
		this.userService = userService;
		this.msgService = msgService;
		this.chatService = chatService;
	}
	
	@MessageMapping("/send/event/{eventId}/chats/{creator}")
	private void sendAndSaveMessage(@DestinationVariable Long eventId, @DestinationVariable String creator, String message) {
		String[] data = message.split(";");
		Long userId = Long.parseLong(data[0]);
		String messageToSend = data[1];
		Date date = new Date();
		String login = userService.getUser(userId).getLogin();
		
		template.convertAndSend("/event/" + eventId + "/chats/" + creator,
				login + ": " + messageToSend + " --- " + new SimpleDateFormat("HH:mm:ss").format(date));
		
		saveMessage(messageToSend, creator, eventId, userId, date);
		
	}
	
	private void saveMessage(String text,String creator, Long eventId, Long userId, Date date){
		Message msg = new Message();
		if(creator.equals("withCreator")){
			msg.setChatId(chatService.findChatId(eventId,true));
		}else{
			msg.setChatId(chatService.findChatId(eventId,false));
		}
		msg.setDate(date);
		msg.setParticipantId(msgService.findParticipantId(userId, eventId));
		msg.setText(text);
		msgService.saveMessage(msg);
	}
	
}