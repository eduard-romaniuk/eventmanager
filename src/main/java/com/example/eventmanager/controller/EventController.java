package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {

    @Autowired
    EventService eventService;


    @RequestMapping(value="/create", method = RequestMethod.POST)
    public void save(@RequestBody Event event,User user) {
       eventService.createEvent(event,user);
    }

    @RequestMapping(value="/publish")
    public void publishEvent(@RequestBody Event event){
        eventService.publishEvent(event);
    }
    @RequestMapping(value="/edit")
    public void edit(@RequestBody Event event){
        eventService.editEvent(event);
    }
    @RequestMapping(value="/withUserParticipation")
    public List<Event> withUserParticipation(@RequestBody User user){
        return eventService.getEventsWithUserParticipation(user);
    }

    @RequestMapping(value = "/allPublic")
    public List<Event> allPublic() {
        return eventService.getAllPublicEvents();
    }

    @RequestMapping(value="/join")
    public void publish(@RequestBody User user,Event event){
        eventService.joinToEvent(user, event);
    }

    @RequestMapping(value="/addParticipants", method = RequestMethod.POST)
    public void addParticipants(@RequestBody List<User> users,Event event) {
       eventService.AddUsersToEvent(users,event);
    }

    @RequestMapping(value="/participants")
    public List<User> participants(@RequestBody Event event){
      return   eventService.getParticipants(event);
    }

}
