package com.example.eventmanager.controller;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.domain.UserView;
import com.example.eventmanager.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @JsonView(UserView.ShortView.class)
    @GetMapping(value = "")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("In listAllUsers - " + users.toString());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @JsonView(UserView.FullView.class)
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @JsonView(UserView.FullView.class)
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User newUser) {
        System.out.println("newUser - " + newUser.toString());
        User oldUser = userService.getUser(id);
        if (oldUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newUser.setId(oldUser.getId());
        userService.updateUser(newUser);
        System.out.println("In updateUser - " + newUser.toString());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

//TODO Uncomment when eventService appear
//    @JsonView(UserView.FullView.class)
//    @GetMapping(value = "/{id}/events")
//    public ResponseEntity<List<Event>> getUserEvents(@PathVariable Long id) {
//        List<Event> eventList = eventService.getUserEvents(id);
//        return new ResponseEntity<>(eventList, HttpStatus.OK);
//    }
}
