package com.example.eventmanager.service;

import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    public User findUser(String username) {
        return usersRepository.findByUsername(username);
    }

    public void updateUser(User user) {
        usersRepository.update(user);
    }

    public boolean isEmailExists(String email) {
        return usersRepository.isEmailExists(email);
    }

    public boolean isUsernameExists(String username) {
        return usersRepository.isUsernameExists(username);
    }
}
