package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventView;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EventService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
//@RequestMapping(value = "/event")
public class EventController {

   private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/event")
    public ResponseEntity<Void> createEvent(@RequestBody Event event, UriComponentsBuilder ucBuilder) {
        System.out.println("Post /event");
        eventService.createEvent(event);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/{id}").buildAndExpand(event.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @JsonView(EventView.ShortView.class)
    @GetMapping(value = "/event")
    public ResponseEntity<List<Event>> listAllPublicEvent() {
        List<Event> events = eventService.getAllPublicEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @JsonView(EventView.FullView.class)
    @GetMapping(value = "/event/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") Long id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @JsonView(EventView.FullView.class)
    @PostMapping(value = "/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Long id, @RequestBody Event newEvent) {
        Event oldEvent = eventService.getEvent(id);
        if (oldEvent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newEvent.setId(oldEvent.getId());
        newEvent.setCreator(oldEvent.getCreator());
        eventService.updateEvent(newEvent);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/event/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventService.deleteEvent(event);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(EventView.FullView.class)
    @GetMapping(value = "/event/{id}/participants")
    public ResponseEntity<List<User>> getParticipants(@PathVariable Long id) {
        List<User> userList = eventService.getParticipants(id);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }





    @RequestMapping(value = "/publish")
    public void publishEvent(@RequestBody Event event) {
        eventService.publishEvent(event);
    }

    @RequestMapping(value = "/withUserParticipation")
    public List<Event> withUserParticipation(@RequestBody User user) {
        return eventService.getEventsWithUserParticipation(user);
    }

    @RequestMapping(value = "/join")
    public void publish(@RequestBody User user, Event event) {
        eventService.joinToEvent(user, event);
    }

    @RequestMapping(value = "/addParticipants", method = RequestMethod.POST)
    public void addParticipants(@RequestBody List<User> users, Event event) {
        eventService.AddUsersToEvent(users, event);
    }

}
