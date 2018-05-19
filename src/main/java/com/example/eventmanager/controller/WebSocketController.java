package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventmanager.domain.Message;
import com.example.eventmanager.service.ChatService;
import com.example.eventmanager.service.MessageService;
import com.example.eventmanager.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
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
	@RequestMapping(value = "/{id}/{creator}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageTransformer>> loadMessages(@PathVariable Long id,@PathVariable String creator) {
		Long chatId = getChatId(id,creator);
		List<Message> messages = msgService.getAllMessagesFromChat(chatId);
		List<MessageTransformer> msgTransformers = new ArrayList<MessageTransformer>();
		for (Message msg : messages) {
			Long userId = msgService.findUserIdFromParticipant(msg.getParticipantId());
			msgTransformers.add(prepareMessage(userId, msg.getText(), msg.getDate()));
		}
        return new ResponseEntity<>(msgTransformers, HttpStatus.OK);
    }	
	
	@MessageMapping("/send/chats/{eventId}/{creator}")
	private void sendAndSaveMessage(@DestinationVariable Long eventId, @DestinationVariable String creator, String message) {
		//StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		//System.out.println(accessor.getHeader("userId").toString());
		//System.out.println(message.getPayload());
		Long userId = Long.parseLong(message.substring(0, message.indexOf(";")));
		String messageToSend = message.substring(message.indexOf(";") + 1);
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateToSend = dt.format(date); 

		sendMessage("/chats/"+ eventId +"/"+ creator, prepareMessage(userId,messageToSend, dateToSend));
		saveMessage(messageToSend, creator, eventId, userId, dateToSend);
	}

	private void sendMessage(String url, MessageTransformer message) {
		template.convertAndSend(url, message);
	}

	private void saveMessage(String text, String creator, Long eventId, Long userId, String date) {
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
	
	private MessageTransformer prepareMessage(Long userId,String messageToSend, String date){
		String login = userService.getUser(userId).getLogin();
		String image = userService.getUser(userId).getImage();
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
		msg.setDate(date);
		return msg;
	}

}