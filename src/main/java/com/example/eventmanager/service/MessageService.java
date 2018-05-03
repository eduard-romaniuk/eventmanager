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
	@Autowired
	private MessageRepository msgRepository;
	
	public void saveMessage(Message msg){
		msgRepository.save(msg);
	}
	
	public List<Message> getAllMessages(){
        Iterator<Message> iter = msgRepository.findAll().iterator();
        List<Message> copy = new ArrayList<>();
        while (iter.hasNext())
            copy.add(iter.next());
        //System.out.println(copy);
		return copy;
	}
}
