package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.domain.UserView;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.SecurityService;
import com.example.eventmanager.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final EmailService emailService;
    private final EventService eventService;
    private final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, SecurityService securityService, EmailService emailService,
                          EventService eventService) {
        logger.info("Class initialized");

        this.userService = userService;
        this.securityService = securityService;
        this.emailService = emailService;
        this.eventService = eventService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void create(@RequestBody User user) {
        logger.info("POST /");

        userService.saveUser(
                securityService.encodePass(
                        securityService.verificationToken(user)));
        emailService.sendVerificationLink(user.getEmail(), user.getToken());
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH)
    public void update(@RequestBody User user) {
        logger.info("PATCH /");

        userService.updateUser(user);
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public void changePass(@RequestBody User user) {
        logger.info("PUT /");
        
        userService.changePass(securityService.encodePass(user));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Principal user(Principal user) {
        logger.info("GET /");

        return user;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User get(@PathVariable String username) {
        logger.info("GET /" + username);

        return userService.findUser(username);
    }

    @RequestMapping(value = "/exists/username/{username}", method = RequestMethod.GET)
    public boolean existsUsername(@PathVariable String username) {
        logger.info("GET /exists/username/" + username);

        return userService.isUsernameExists(username);
    }

    @RequestMapping(value = "/exists/email/{email}", method = RequestMethod.GET)
    public boolean existsEmail(@PathVariable String email) {
        logger.info("GET /exists/email/" + email);

        return userService.isEmailExists(email);
    }

    @JsonView(UserView.ShortView.class)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        logger.info("GET /all");

        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @JsonView(UserView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        logger.info("GET /" + id);

        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @JsonView(UserView.FullView.class)
    @RequestMapping(value = "/{id}/events", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable Long id) {
        logger.info("GET /" + id + "/events");

        List<Event> eventList = eventService.getUserEvents(id);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }


}
