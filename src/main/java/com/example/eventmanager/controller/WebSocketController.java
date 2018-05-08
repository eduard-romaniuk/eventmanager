package com.example.eventmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventmanager.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/chats")
public class WebSocketController {
    private final SimpMessagingTemplate template;
    private final UserService userService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template,UserService userService){
        this.template = template;
        this.userService = userService;
    }

//    @MessageMapping("/send/message")
//    public void onReceivedMesage(String message){
//    	
//        this.template.convertAndSend("/chat",message + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        
//    }
    
    @MessageMapping("/send/event/{id}/chats/{creator}")
    private void sendMessageToPrivateRoom(@DestinationVariable Long id, @DestinationVariable String creator, String message) {
    	String login = "";//userService.getCurrentUser().getLogin();
        template.convertAndSend("/event/"+id+"/chats/"+creator,login+": " +message + " --- " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}