package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.EventView;
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

    private static final int PENDING = 0;
    private static final int ACCEPTED = 1;
    private static final int DECLINED = 2;
    private static final int BLOCKED = 3;

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

//    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
//    public void update(@PathVariable("id") Long id, @RequestBody User user) {
//        logger.info("PATCH /");
//
//        userService.updateUser(user);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User newUser) {
        logger.info("PUT /" + id);
        System.out.println("newUser - " + newUser.toString());
        User oldUser = userService.getUser(id);
        if (oldUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newUser.setId(oldUser.getId());
        userService.updateUser(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public void changePass(@RequestBody User user) {
        logger.info("PUT /changePassword");

        userService.changePass(securityService.encodePass(user));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Principal user(Principal user) {
        logger.info("GET /");

        return user;
    }

    @RequestMapping(value = "/by-username/{username}", method = RequestMethod.GET)
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

    @JsonView(EventView.FullView.class)
    @RequestMapping(value = "/{id}/events", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable Long id) {
        logger.info("GET /" + id + "/events");

        List<Event> eventList = eventService.getUserEvents(id);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @JsonView(UserView.ShortView.class)
    @RequestMapping(value = "/search", params = "login", method = RequestMethod.GET)
    public ResponseEntity<List<User>> searchByLogin(@RequestParam("login") String login) {
        logger.info("GET /users/search?login=" + login);

        List<User> users = userService.searchByLogin(login);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Friends functionality

    @RequestMapping(value = "/addFriendRequest", params = {"from", "to"}, method = RequestMethod.POST)
    public ResponseEntity<String> addFriendRequest(@RequestParam Long from, @RequestParam Long to) {
        logger.info("POST /addFriendRequest");

        if(userService.getRelationshipStatus(from, to).equals("")){
            userService.saveRelationship(from, to, PENDING, from);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("Users {} and {} already have some relationship", from, to);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/answerFriendRequest", params = {"requester", "accepter", "accept"},
            method = RequestMethod.PUT)
    public ResponseEntity<String> answerFriendRequest(@RequestParam Long requester, @RequestParam Long accepter,
                                                      @RequestParam Boolean accept) {
        logger.info("PUT /answerFriendRequest");

        if(userService.getRelationshipStatus(requester, accepter).equals("pending")){
            userService.updateRelationship(requester, accepter, accept ? ACCEPTED : DECLINED, accepter);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("There is no request for friendship from user {} or user {} or they have other relationship",
                    requester, accepter);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    //TODO What behavior?
//    @RequestMapping(value = "/blockFriend", params = {"from", "to"}, method = RequestMethod.PUT)
//    public void blockFriend(@RequestParam Long from, @RequestParam Long to) {
//        logger.info("PUT /blockFriend");
//        userService.updateRelationship(from, to, BLOCKED, from);
//    }

    @RequestMapping(value = "/deleteRelationship", params = {"from", "to"}, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRelationship(@RequestParam Long from, @RequestParam Long to) {
        logger.info("DELETE /deleteRelationship");

        if(userService.getRelationshipStatus(from, to).matches("accepted|pending")){
            userService.deleteRelationship(from, to);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("User {} and user {} was not friends", from, to);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/getRelationshipStatus", params = {"from", "to"}, method = RequestMethod.GET)
    public String getRelationshipStatus(@RequestParam Long from, @RequestParam Long to) {
        logger.info("GET /getRelationshipStatus");
        return userService.getRelationshipStatus(from, to);
    }

    @RequestMapping(value = "/{id}/friends", method = RequestMethod.GET)
    public List<User> getFriendsByUserId(@PathVariable(value = "id") Long userId) {
        logger.info("GET /{}/friends", userId);
        return userService.getFriendsByUserId(userId);
    }

}
