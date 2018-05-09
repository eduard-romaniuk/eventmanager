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
		Date date = new Date();
		String login = "";
		
		template.convertAndSend("/event/" + eventId + "/chats/" + creator,
				login + ": " + message + " --- " + new SimpleDateFormat("HH:mm:ss").format(date));
		
		Message msg = new Message();
		//Long userId = userService.getCurrentUser().getId();
		//Long participantId = msgService.findParticipantId(userId, eventId);
		if(creator.equals("withCreator")){
			msg.setChatId(chatService.findChatId(eventId,true));
		}else{
			msg.setChatId(chatService.findChatId(eventId,false));
		}
		//msg.setDate(date);
		//msg.setParticipantId(participantId);
		//msg.setText(message);
		//msgService.saveMessage(msg);
	}
}