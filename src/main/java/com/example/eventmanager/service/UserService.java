package com.example.eventmanager.service;

import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.thymeleaf.expression.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@CrossOrigin(origins = "http://localhost:4200")
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    public List<User> getAllUsers(){
        //TODO Return list, not iterator
        Iterator<User> iter = usersRepository.findAll().iterator();
        List<User> copy = new ArrayList<User>();
        while (iter.hasNext())
            copy.add(iter.next());

        return copy;
    }

    public User getUser(Long id){
        return usersRepository.findOne(id);
    }

    public void updateUser(User user){
        usersRepository.update(user);
    }

    public void deleteUser(User user){
        usersRepository.delete(user);
    }

    public User findUser(String username) {
        return usersRepository.findByUsername(username);
    }
    public boolean isEmailExists(String email) {
        return usersRepository.isEmailExists(email);
    }

    public boolean isUsernameExists(String username) {
        return usersRepository.isUsernameExists(username);
    }
}
