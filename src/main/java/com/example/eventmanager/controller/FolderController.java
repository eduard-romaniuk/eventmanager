package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Folder;
import com.example.eventmanager.domain.FolderView;
import com.example.eventmanager.service.FolderService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping(value = "/folder")
public class FolderController {

    private final FolderService folderService;
    private final Logger logger = LogManager.getLogger(FolderController.class);

    @Autowired
    public FolderController(FolderService folderService) {
        logger.info("Class initialized");
        this.folderService = folderService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {
        logger.info("POST /");

        return new ResponseEntity<>(folderService.createFolder(folder), HttpStatus.CREATED);
    }

    @JsonView(FolderView.FullView.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
    public ResponseEntity<List<Folder>> getFolderByUser(@PathVariable Long id) {
        logger.info("GET /{id}/all");

        List<Folder> folderList = folderService.getUserFolders(id);
        return new ResponseEntity<>(folderList, HttpStatus.OK);
    }

    @JsonView(FolderView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Folder> getFolderById(@PathVariable Long id) {
        logger.info("GET /{id}");

        Folder folder = folderService.getFolder(id);
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

}


