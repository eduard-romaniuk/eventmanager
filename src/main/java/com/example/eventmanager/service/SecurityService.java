package com.example.eventmanager.service;

import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User encodePass(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public User verificationToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        return user;
    }
}
