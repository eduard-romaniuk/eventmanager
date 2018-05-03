package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.dao.UserRepository;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }


    public void createEvent(Event event){
       // event.setCreator(creator);
        event.setId((long) eventRepository.save(event));
        eventRepository.addUserToEvent(userService.getCurrentUser().getId(),event.getId());
    }

    public void publishEvent(Event event){
        event.setSent(true);
        eventRepository.update(event);
    }

    public void updateEvent(Event event){
        eventRepository.update(event);
    }

    public Event getEvent (Long id){
        return eventRepository.findOne(id);
    }

    public List<Event> getUserEvents(Long id){

       return eventRepository.findByCreator(id);
    }

    public List<Event> getEventsWithUserParticipation(User user){

        return eventRepository.findEventsWithUserParticipation(user.getId());
    }

    public List<Event> getAllPublicEvents(){

        return eventRepository.findAllPublicEvents();
    }

    public void joinToEvent(User user,Event event){

        eventRepository.addUserToEvent(user.getId(),event.getId());

    }

    public void AddUsersToEvent(List<User> users,Event event){

        for (User user:users) {
            eventRepository.addUserToEvent(user.getId(),event.getId());
        }
    }

    public List<User> getParticipants(Long id){

        return eventRepository.findParticipants(id);
    }

    public void deleteEvent(Event event){
        eventRepository.delete(event);
    }
}
