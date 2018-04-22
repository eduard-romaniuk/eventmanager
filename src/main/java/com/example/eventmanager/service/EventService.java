package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UsersRepository usersRepository) {
        this.eventRepository = eventRepository;
        this.usersRepository = usersRepository;
    }

    /* TODO add Principal */
    public void createEvent(Event event){
       // event.setCreator(creator);
        event.setId((long) eventRepository.save(event));
        //eventRepository.addUserToEvent(event.getId(),creator.getId());
    }

    public void publishEvent(Event event){
        event.setSent(true);
        eventRepository.update(event);
    }

    public void editEvent(Event event){
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

    public List<User> getParticipants(Event event){

        return eventRepository.findParticipants(event.getId());
    }
}
