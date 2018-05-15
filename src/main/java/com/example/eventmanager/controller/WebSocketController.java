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

	private final class MessageTransformer{
		private String login;
		private String image;
		private String text;
		private String date;
		
		public String getLogin() {
			return login;
		}
		public String getImage() {
			return image;
		}
		public String getText() {
			return text;
		}
		public String getDate() {
			return date;
		}
		public void setLogin(String login) {
			this.login = login;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public void setText(String text) {
			this.text = text;
		}
		public void setDate(String date) {
			this.date = date;
		}	
	}
	
	@MessageMapping("/send/event/{eventId}/chats/{creator}/load")
	private void loadMessages(@DestinationVariable Long eventId, @DestinationVariable String creator) {
		Long chatId = getChatId(eventId,creator);
		List<Message> messages = msgService.getAllMessagesFromChat(chatId);
		for (Message msg : messages) {
			Long userId = msgService.findUserIdFromParticipant(msg.getParticipantId());
			sendMessage("/event/" + eventId + "/chats/" + creator, prepareMessage(userId, msg.getText(), msg.getDate()));
		}
	}

	@MessageMapping("/send/event/{eventId}/chats/{creator}")
	private void sendAndSaveMessage(@DestinationVariable Long eventId, @DestinationVariable String creator, String message) {
		
		Long userId = Long.parseLong(message.substring(0, message.indexOf(";")));
		String messageToSend = message.substring(message.indexOf(";") + 1);
		Date date = new Date();

		sendMessage("/event/" + eventId + "/chats/" + creator, prepareMessage(userId,messageToSend, date));
		saveMessage(messageToSend, creator, eventId, userId, date);
	}

	private void sendMessage(String url, MessageTransformer message) {
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
	
	private MessageTransformer prepareMessage(Long userId,String messageToSend, Date date){
		String login = userService.getUser(userId).getLogin();
		String image = userService.getUser(userId).getImage();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(image==null) {
			if(userService.getUser(userId).getSex()){
				image = "../../../assets/images/default_user_male.png";
			}else{
				image = "../../../assets/images/default_user_female.png";
			}
		}
		MessageTransformer msg = new MessageTransformer();
		msg.setLogin(login);
		msg.setImage(image);
		msg.setText(messageToSend);
		msg.setDate(dt.format(date));
		return msg;
	}

}