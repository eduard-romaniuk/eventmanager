package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventmanager.domain.Chat;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.UserService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/chats")
public class WebSocketController {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/send/message")
    public void onReceivedMesage(String message){
    	
        this.template.convertAndSend("/chat",message + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        
    }
    
    @MessageMapping("/{roomId}")
    private void sendMessageTpPrivateRoom(String message, @DestinationVariable Chat chat) throws IOException {
        this.template.convertAndSend("/privateRoom/" + chat.getId(), message);
    }
}