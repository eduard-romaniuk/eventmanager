package com.example.eventmanager.service;


import com.example.eventmanager.dao.FolderRepository;
import com.example.eventmanager.domain.Folder;
import com.example.eventmanager.domain.Member;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final Logger logger = LogManager.getLogger(FolderService.class);

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

    public Folder getFolderByIdAndUserId(Long id, Long userId) {
        return folderRepository.findByIdAndUserId(id, userId);
    }

    public void deleteFolder(Folder folder){
        folderRepository.delete(folder);
    }

    public List<Member> getAllMembers(Long folderId) {
        logger.info("Get incoming requests for folder with id {}", folderId);
        return folderRepository.getAllMembers(folderId);
    }
}
