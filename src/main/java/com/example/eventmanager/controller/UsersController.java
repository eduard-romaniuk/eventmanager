package com.example.eventmanager.controller;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.SecurityService;
import com.example.eventmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public void create(@RequestBody User user) {
        userService.saveUser(
                securityService.encodePass(
                        securityService.verificationToken(user)));
        emailService.sendVerificationLink(user.getEmail(), user.getToken());
    }

    @RequestMapping(value="/", method = RequestMethod.PATCH)
    public void update(@RequestBody User user) {
        userService.updateUser(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User get(@PathVariable String username){
        return userService.findUser(username);
    }

    @RequestMapping(value = "exists/username/{username}", method = RequestMethod.GET)
    public boolean existsUsername(@PathVariable String username){
        return userService.isUsernameExists(username);
    }

    @RequestMapping(value = "exists/email/{email}", method = RequestMethod.GET)
    public boolean existsEmail(@PathVariable String email){
        return userService.isEmailExists(email);
    }
}
