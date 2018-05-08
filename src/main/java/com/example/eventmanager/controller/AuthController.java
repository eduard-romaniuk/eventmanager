package com.example.eventmanager.controller;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.SecurityService;
import com.example.eventmanager.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final UserService userService;
    private final SecurityService securityService;
    private final EmailService emailService;
    private final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService, SecurityService securityService, EmailService emailService) {
        logger.info("Class initialized");

        this.userService = userService;
        this.securityService = securityService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Principal user(Principal user) {
        logger.info("GET /");

        return user;
    }

    @RequestMapping(value = "/changePassword/{login}/{token}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changePassByToken(@RequestBody String pass, @PathVariable String login, @PathVariable String token) {
        logger.info("PUT /changePassword");
        User user = userService.findUser(login);
        if (token.equals(user.getToken())) {
            user.setPassword(pass);
            userService.changePass(securityService.encodePass(user));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/recover/password/{login}", method = RequestMethod.POST)
    public void recoverPassword(@PathVariable String login) {
        logger.info("POST /recover/password/" + login);

        User user = userService.findUser(login);
        emailService.sendTextMail(
                user.getEmail(),
                "Recover password",
                "Click the link below:\n" +
                        "https://web-event-manager.firebaseapp.com/recover-password/" +
                        user.getToken());
    }

    @RequestMapping(value = "/recover/login/{email}", method = RequestMethod.POST)
    public void recoverLogin(@PathVariable String email) {
        logger.info("POST /recover/login/" + email);

        User user = userService.findUserByEmail(email);
        emailService.sendTextMail(
                user.getEmail(),
                "Your login",
                "Login: " + user.getLogin() +
                        "\nClick the link below to recover your password:\n" +
                        "https://web-event-manager.firebaseapp.com/recover-password/" +
                        user.getToken());
    }
}
