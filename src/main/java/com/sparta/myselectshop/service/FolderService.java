package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        List<Folder> existFolderList = this.folderRepository.findAllByUserAndNameIn(user, folderNames);
        List<Folder> folderList = new ArrayList<>();

        for(String folderName : folderNames) {
            if (this.isExistFolderName(folderName, existFolderList)) {
                throw new IllegalArgumentException("중복된 폴더명을 제거해주세요. 폴더명 : "+ folderName);
            }

            Folder folder = new Folder(folderName, user);
            folderList.add(folder);
        }

        this.folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = this.folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

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