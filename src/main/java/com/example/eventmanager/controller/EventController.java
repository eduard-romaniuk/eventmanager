package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventView;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.ExportEventService;
import com.fasterxml.jackson.annotation.JsonView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {

    private final EventService eventService;
    private final ExportEventService exportService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(EventController.class);

    @Autowired
    public EventController(EventService eventService, ExportEventService exportService, EmailService emailService) {
        this.emailService = emailService;
        logger.info("Class initialized");

        this.exportService = exportService;
        this.eventService = eventService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Long> createEvent(@RequestBody Event event, UriComponentsBuilder ucBuilder) {
        logger.info("POST /");

        eventService.createEvent(event);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("id", "1" + event.getId());
        //headers.setLocation(ucBuilder.path("/event/{id}").buildAndExpand(event.getId()).toUri());
        return new ResponseEntity<>(event.getId(), HttpStatus.CREATED);
    }

    @JsonView(EventView.ShortView.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> listAllPublicEvent() {
        logger.info("GET /");

        List<Event> events = eventService.getAllPublicEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @JsonView(EventView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Event> getEvent(@PathVariable("id") Long id) {
        logger.info("GET /" + id);

        Event event = eventService.getEvent(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @JsonView(EventView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Long id, @RequestBody Event newEvent) {
        logger.info("POST /" + id);

        Event oldEvent = eventService.getEvent(id);
        if (oldEvent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newEvent.setId(oldEvent.getId());
        newEvent.setCreator(oldEvent.getCreator());
        eventService.updateEvent(newEvent);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        logger.info("DELETE /" + id);

        Event event = eventService.getEvent(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventService.deleteEvent(event);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(EventView.FullView.class)
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getParticipants(@PathVariable Long id) {
        logger.info("GET /" + id + "/participants");

        List<User> userList = eventService.getParticipants(id);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    //    TODO: Refactor methods which below
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public void publishEvent(@RequestBody Event event) {
        logger.info("POST /publish");

        eventService.publishEvent(event);
    }

    @RequestMapping(value = "/withUserParticipation", method = RequestMethod.POST)
    public List<Event> withUserParticipation(@RequestBody User user) {
        logger.info("POST /withUserParticipation");

        return eventService.getEventsWithUserParticipation(user);
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public void publish(@RequestBody User user, Event event) {
        logger.info("POST /join");

        eventService.joinToEvent(user, event);
    }

    @RequestMapping(value = "/addParticipants", method = RequestMethod.POST)
    public void addParticipants(@RequestBody List<User> users, Event event) {
        logger.info("POST /addParticipants");

        eventService.AddUsersToEvent(users, event);
    }

    @RequestMapping(value = "/downloadPlan", method = RequestMethod.GET)
    public void downloadEventsPlan(@RequestParam String from, @RequestParam String to, HttpServletResponse response) {
        logger.info("GET /downloadPlan");

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        JasperPrint eventsPlan = exportService.createEventsPlan(fromDate, toDate);

        try {
            final OutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(eventsPlan, outputStream);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping(value = "/sendPlan", method = RequestMethod.GET)
    public void sendEventsPlan(@RequestParam String from, @RequestParam String to) {
        logger.info("GET /sendPlan");
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        emailService.sendEventsPlan(fromDate, toDate);

    }

}


