package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
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

    public List<Event> eventsForPeriod(LocalDate fromDate, LocalDate toDate){

        return eventRepository.eventsForPeriod(userService.getCurrentUser().getId(),fromDate,toDate);
    }
    public List<Event> eventsFromDate(LocalDate fromDate){

        return eventRepository.eventsFromDate(userService.getCurrentUser().getId(),fromDate);
    }

    public void joinToEvent(Long event_id){

        eventRepository.addUserToEvent(userService.getCurrentUser().getId(),event_id);

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

    public String getPriority(Long event_id){

        return eventRepository.getPriority(userService.getCurrentUser().getId(),event_id);
    }

    public void changePriority(Long event_id, Long priority_id){

        eventRepository.changePriority(userService.getCurrentUser().getId(),event_id,priority_id);
    }

    public boolean isParticipant(Long event_id){

        return eventRepository.isParticipant(userService.getCurrentUser().getId(),event_id);
    }
}
