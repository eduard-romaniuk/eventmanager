package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.dao.FolderRepository;
import com.example.eventmanager.domain.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FolderService {

    private final FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }


    public void createFolder(Folder folder){
        folder.setId((long) folderRepository.save(folder));
        //eventRepository.addUserToEvent(userService.getCurrentUser().getId(),event.getId());
    }
}
