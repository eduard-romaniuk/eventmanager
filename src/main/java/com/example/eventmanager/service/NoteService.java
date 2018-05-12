package com.example.eventmanager.service;


import com.example.eventmanager.dao.NoteRepository;
import com.example.eventmanager.domain.Note;
import com.example.eventmanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }


    public void createNote(Note note){
        note.setId((long) noteRepository.save(note));
    }


    public void updateNote(Note note){
        noteRepository.update(note);
    }

    public Note getNote (Long id){
        return noteRepository.findOne(id);
    }

    public List<Note> getRootFolderEvents(){

       return noteRepository.findAllRootFolderEvents(userService.getCurrentUser().getId());
    }

    public void deleteNote(Note note){
        noteRepository.delete(note);
    }
}
