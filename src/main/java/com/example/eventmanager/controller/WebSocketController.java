package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.eventmanager.domain.Message;
import com.example.eventmanager.domain.MessageWrapper;
import com.example.eventmanager.service.MessageService;
import com.example.eventmanager.service.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/chats")
public class WebSocketController {
	private final SimpMessagingTemplate template;
	private final MessageService msgService;

	@Autowired
	WebSocketController(SimpMessagingTemplate template, UserService userService, MessageService msgService) {
		this.template = template;
		this.msgService = msgService;
	}

	
	@RequestMapping(value = "/{id}/{creator}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageWrapper>> loadMessages(@PathVariable Long id,@PathVariable String creator) {
		Long chatId = msgService.getChatId(id,creator);
		List<Message> messages = msgService.getAllMessagesFromChat(chatId);
		List<MessageWrapper> msgWrappers = new ArrayList<MessageWrapper>();
		for (Message msg : messages) {
			Long userId = msgService.findUserIdFromParticipant(msg.getParticipantId());
			msgWrappers.add(msgService.prepareMessage(userId, msg.getText(), msg.getDate()));
		}
        return new ResponseEntity<>(msgWrappers, HttpStatus.OK);
    }	
	
	@MessageMapping("/send/chats/{eventId}/{creator}")
	private void sendAndSaveMessage(@DestinationVariable Long eventId, @DestinationVariable String creator, @Header Long userId, String message) {	
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateToSend = dt.format(date); 

		template.convertAndSend("/chats/"+ eventId +"/"+ creator, msgService.prepareMessage(userId,message, dateToSend));
		msgService.saveMessage(message, creator, eventId, userId, dateToSend);
	}

}