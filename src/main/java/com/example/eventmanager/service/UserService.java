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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
}
