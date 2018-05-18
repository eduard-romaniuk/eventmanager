package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Note;
import com.example.eventmanager.domain.NoteView;
import com.example.eventmanager.service.NoteService;
import com.example.eventmanager.service.UserService;
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
@RequestMapping(value = "/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final Logger logger = LogManager.getLogger(NoteController.class);

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        logger.info("Class initialized");

        this.userService = userService;

        this.noteService = noteService;
    }

    @JsonView(NoteView.ShortView.class)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Note> createNote(@RequestBody Note note, UriComponentsBuilder ucBuilder) {
        logger.info("POST /");
        noteService.createNote(note);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @JsonView(NoteView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Note> getNote(@PathVariable("id") Long id) {
        logger.info("GET /" + id);

        Note note = noteService.getNote(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @JsonView(NoteView.FullView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Note> updateNote(@PathVariable("id") Long id, @RequestBody Note newNote) {
        logger.info("POST /" + id);

        Note oldNote = noteService.getNote(id);
        if (oldNote == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newNote.setId(oldNote.getId());
        newNote.setCreator(oldNote.getCreator());
        noteService.updateNote(newNote);
        return new ResponseEntity<>(newNote, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id) {
        logger.info("DELETE /" + id);

        Note note = noteService.getNote(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        noteService.deleteNote(note);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(NoteView.FullView.class)
    @RequestMapping(value = "/folder/{folderId}", method = RequestMethod.GET)
    public ResponseEntity<List<Note>> getAllFolderNotes(@PathVariable Long folderId) {
        logger.info("GET /folder/{folderId}");
        logger.info("Current user id = " + userService.getCurrentUser().getId());
        List<Note> noteList = noteService.getAllFolderNotes(folderId, userService.getCurrentUser().getId());
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }
}


