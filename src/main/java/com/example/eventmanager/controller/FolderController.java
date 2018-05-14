package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Folder;
import com.example.eventmanager.domain.FolderView;
import com.example.eventmanager.service.FolderService;
import com.example.eventmanager.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "/folder")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;
    private final Logger logger = LogManager.getLogger(FolderController.class);

    @Autowired
    public FolderController(FolderService folderService, UserService userService) {
        logger.info("Class initialized");
        this.folderService = folderService;
        this.userService = userService;
    }

    @JsonView(FolderView.ShortView.class)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {
        logger.info("POST /");

        return new ResponseEntity<>(folderService.createFolder(folder), HttpStatus.CREATED);
    }

    @JsonView(FolderView.FullView.class)
    @RequestMapping(value = "/{userId}/all", method = RequestMethod.GET)
    public ResponseEntity<List<Folder>> getFolderByUser(@PathVariable Long userId) {
        logger.info("GET /{userId}/all");

        List<Folder> folderList = folderService.getUserFolders(userId);
        return new ResponseEntity<>(folderList, HttpStatus.OK);
    }

    @JsonView(FolderView.FullView.class)
    @RequestMapping(value = "/{id}/checkUser", method = RequestMethod.GET)
    public ResponseEntity<Folder> getFolderByIdWithCheck(@PathVariable Long id) {
        logger.info("GET /{id}/checkUser");

        Long currentUserId = userService.getCurrentUser().getId();
        logger.info("currentUserId = " + currentUserId);
        Folder folder = folderService.getFolderByIdAndUserId(id, currentUserId);
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

}


