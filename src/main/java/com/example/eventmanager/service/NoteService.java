package com.example.eventmanager.service;


import com.example.eventmanager.dao.NoteRepository;
import com.example.eventmanager.domain.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;


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


    public void saveNote(Note note){
        if(getNote(note.getId()) == null) {
            note.setId((long) noteRepository.save(note));
        } else {
            noteRepository.update(note);
        }
    }

    public void updateNote(Note note){
        noteRepository.update(note);
    }

    public Note getNote (Long id){
        return noteRepository.findOne(id);
    }

    public Note getNoteForUpdate (Long id){
        return noteRepository.findOneForUpdate(id);
    }

    public List<Note> getAllFolderNotes(Long folderId, Long currentUserId){

       return noteRepository.findAllFolderNotes(folderId, currentUserId);
    }

    public void moveNotes(Note[] notes) {
        for(Note note: notes) {
            if(note.getFolder()!=null) {
                noteRepository.moveNote(note);
            }
        }
    }

    public void deleteNote(Note note){
        noteRepository.delete(note);
    }
}
