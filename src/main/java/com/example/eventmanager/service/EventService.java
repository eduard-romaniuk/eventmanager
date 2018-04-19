package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UsersRepository usersRepository;


    public void createEvent(Event event, User creator){
        event.setCreator(creator);
        eventRepository.save(event);
    }

    public void publishEvent(Event event){
        boolean sent = true;
        event.setSent(sent);
        eventRepository.update(event);
    }

    public void editEvent(Event event){

        eventRepository.update(event);
    }

    public Event showEvent (Long id){
        return eventRepository.findOne(id);
    }

    public List<Event> showUserEvents(User user,Event event){

       return new ArrayList<Event>(eventRepository.findByCreator(user.getId()));
    }

    public List<Event> ShowAllEventsWithUserParticipation(User user,Event event){

        return null;
    }

    public List<Event> ShowAllPublicEvents(User user,Event event){

        return null;
    }

    public void JoinToEvent(User user,Event event){

    }

    public void AddUserToEvent(User user,Event event){

    }

    public List<User> ShowParticipants(Event event){

        return null;
    }
}
