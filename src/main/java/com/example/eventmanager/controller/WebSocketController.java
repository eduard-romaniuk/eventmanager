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
import java.util.List;

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

	@MessageMapping("/send/event/{eventId}/chats/{creator}/load")
	private void loadMessages(@DestinationVariable Long eventId, @DestinationVariable String creator) {
		Long chatId = getChatId(eventId,creator);
		List<Message> messages = msgService.getAllMessagesFromChat(chatId);
		StringBuilder str = new StringBuilder();
		for (Message msg : messages) {
			Long userId = msgService.findUserIdFromParticipant(msg.getParticipantId());
			String login = userService.getUser(userId).getLogin();
			str.append(login + " : " + msg.getText() + " --- " + msg.getDate().toString());
			str.append("<br>");
//			sendMessage("/event/" + eventId + "/chats/" + creator, login + " : " + msg.getText()
//					+ " --- " + msg.getDate().toString());
		}
		sendMessage("/event/" + eventId + "/chats/" + creator, str.toString());
	}

	@MessageMapping("/send/event/{eventId}/chats/{creator}")
	private void sendAndSaveMessage(@DestinationVariable Long eventId, @DestinationVariable String creator,
			String message) {

		Long userId = Long.parseLong(message.substring(0, message.indexOf(";")));
		String messageToSend = message.substring(message.indexOf(";") + 1);
		Date date = new Date();
		String login = userService.getUser(userId).getLogin();

		sendMessage("/event/" + eventId + "/chats/" + creator,
				login + ": " + messageToSend + " --- " + new SimpleDateFormat("HH:mm:ss").format(date));

		saveMessage(messageToSend, creator, eventId, userId, date);
	}

	private void sendMessage(String url, String message) {
		template.convertAndSend(url, message);
	}

	private void saveMessage(String text, String creator, Long eventId, Long userId, Date date) {
		Message msg = new Message();
		msg.setChatId(getChatId(eventId,creator));
		msg.setDate(date);
		msg.setParticipantId(msgService.findParticipantId(userId, eventId));
		msg.setText(text);
		msgService.saveMessage(msg);
	}
	
	private Long getChatId(Long eventId, String creator){
		if (creator.equals("withCreator")) {
			return chatService.findChatId(eventId, true);
		} else {
			return chatService.findChatId(eventId, false);
		}
	}

}