package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
    private FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        List<Folder> existFolderList = this.folderRepository.findAllByUserAndNameIn(user, folderNames);
        List<Folder> folderList = new ArrayList();

        for(String folderName : folderNames) {
            if (this.isExistFolderName(folderName, existFolderList)) {
                throw new IllegalArgumentException("폴더명이 중복 되었습니다.");
            }

            Folder folder = new Folder(folderName, user);
            folderList.add(folder);
        }

        this.folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = this.folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList();

        for(Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }

    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for(Folder existFolder : existFolderList) {
            if (folderName.equals(existFolder.getName())) {
                return true;
            }
        }

        return false;
    }
}