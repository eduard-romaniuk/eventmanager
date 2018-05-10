package com.example.eventmanager.service;


import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.dao.FolderRepository;
import com.example.eventmanager.domain.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FolderService {

    private final FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }


    public Folder createFolder(Folder folder){
        folderRepository.save(folder);
        return folder;
    }

    public List<Folder> getUserFolders(Long id){

        return folderRepository.findByUser(id);
    }

    public Folder getFolder(Long id) {
        return folderRepository.findOne(id);
    }
}
