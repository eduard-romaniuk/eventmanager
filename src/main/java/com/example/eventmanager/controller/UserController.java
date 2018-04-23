package com.example.eventmanager.controller;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.domain.UserView;
import com.example.eventmanager.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
