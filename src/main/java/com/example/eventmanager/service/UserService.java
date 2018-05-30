package com.example.eventmanager.service;

import com.example.eventmanager.dao.UserRepository;
import com.example.eventmanager.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:schedule.properties")
@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Long countAllUsers(){
        logger.info("Count all users");
        return userRepository.countAllUsers();
    }

    public List<User> getAllUsers(){
        //TODO Return list, not iterator
        Iterator<User> iter = userRepository.findAll().iterator();
        List<User> copy = new ArrayList<>();
        while (iter.hasNext())
            copy.add(iter.next());
        System.out.println(copy);
        return copy;
    }

    public List<User> getAllUsersPagination(int limit, int offset){
        logger.info("Get all users");
        return userRepository.findAllPagination(limit, offset);
    }

    public User getUser(Long id){
        return userRepository.findOne(id);
    }

    public void updateUser(User user){
        userRepository.update(user);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
    
    public void changePass(User user){
    	userRepository.changePass(user);
    }

    public void changeToken(User user){
        userRepository.changeToken(user);
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailExists(String email) {
        return userRepository.isEmailExists(email);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.isUsernameExists(username);
    }

    public List<User> searchByLoginOrByNameAndSurname(String queryString){
        return userRepository.searchByLoginOrByNameAndSurname(queryString);
    }

    public List<User> searchByLoginOrByNameAndSurnamePagination(String queryString, int limit, int offset){
        return userRepository.searchByLoginOrByNameAndSurnamePagination(queryString, limit, offset);
    }

    public Long countSearchByLoginOrByNameAndSurname(String queryString){
        return userRepository.countSearchByLoginOrByNameAndSurname(queryString);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser(){
        return findUser(getCurrentUsername());
    }

    public List<User> findAllActive(){
        return userRepository.findAllActive();
    }

    public List<User> findAllActivePagination(int limit, int offset){
        return userRepository.findAllActivePagination(limit, offset);
    }

    //Friends functionality

    public void saveRelationship(Long userOneId, Long userTwoId, int statusId, Long actionUserId) {
        logger.info("Save relationship for user with id {} and user with id {} with status id {}",
                userOneId, userTwoId, statusId);
        userRepository.saveRelationship(userOneId, userTwoId, statusId, actionUserId);
    }

    public void updateRelationship(Long userOneId, Long userTwoId, int statusId, Long actionUserId){
        logger.info("Update relationship for user with id {} and user with id {} with status id {}",
                userOneId, userTwoId, statusId);
        userRepository.updateRelationship(userOneId, userTwoId, statusId, actionUserId);
    }

    public void deleteRelationship(Long userOneId, Long userTwoId){
        logger.info("Delete relationship for user with id {} and user with id {}", userOneId, userTwoId);
        userRepository.deleteRelationship(userOneId, userTwoId);
    }

    public String getRelationshipStatus(Long userOneId, Long userTwoId){
        logger.info("Get relationship status for user with id {} and user with id {}", userOneId, userTwoId);
        return userRepository.getRelationshipStatus(userOneId, userTwoId);
    }

    public int getRelationshipStatusId(Long userOneId, Long userTwoId){
        logger.info("Get relationship status id for user with id {} and user with id {}", userOneId, userTwoId);
        return userRepository.getRelationshipStatusId(userOneId, userTwoId);
    }

    public Map<String, Object> getRelationshipStatusAndActiveUserId(Long userOneId, Long userTwoId){
        logger.info("Get relationship status and active user id for user with id {} and user with id {}", userOneId, userTwoId);
        return userRepository.getRelationshipStatusAndActiveUserId(userOneId, userTwoId);
    }

    public Map<String, Object> getRelationshipStatusIdAndActiveUserId(Long userOneId, Long userTwoId){
        logger.info("Get relationship status and active user id for user with id {} and user with id {}", userOneId, userTwoId);
        return userRepository.getRelationshipStatusIdAndActiveUserId(userOneId, userTwoId);
    }

    public List<User> getOutcomingRequests(Long userId){
        logger.info("Get outcoming requests for user with id {}", userId);
        return userRepository.getOutcomingRequests(userId);
    }

    public List<User> getIncomingRequests(Long userId){
        logger.info("Get incoming requests for user with id {}", userId);
        return userRepository.getIncomingRequests(userId);
    }

    public List<User> getFriendsByUserId(Long userId){
        logger.info("Get friends for user with id {}", userId);
        return userRepository.getFriendsByUserId(userId);
    }

    @Scheduled(cron = "${schedule.cron.everyDay}")
    public void deleteDisabledUsers() {
        logger.info("delete disabled users");
        List<User> usersForDelete = userRepository.findDisabledUsersForDelete();
        logger.info(usersForDelete.size() + " users found for the delete");
        userRepository.deleteAll(usersForDelete);
    }
}
