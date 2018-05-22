package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    public User encodePass(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public User verificationToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        return user;
    }

    public boolean comparePass(User user, String password) {
        System.out.println("compare pass");
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser(){
        return userService.findUser(getCurrentUsername());
    }
}
