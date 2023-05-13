package com.isep.hometrade.service;

import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.PhotoRepository;
import com.isep.hometrade.map.PhotoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public void savePhoto(PhotoDto photoDto, HouseEntity houseEntity) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setName(photoDto.getName());
        photoEntity.setPath(photoDto.getPath());
        photoEntity.setHouseEntity(houseEntity);
        photoRepository.save(photoEntity);
    }

    public Set<PhotoEntity> findPhotosByHouse(HouseEntity houseEntity) {
        List<PhotoEntity> photos = photoRepository.findAllByHouse(houseEntity);
        return new HashSet<>(photos);
    }

    public PhotoEntity findPhotoByPath(String path) {
        return photoRepository.findByPath(path);
    }

    public void deletePhotoByPath(String path) {
        PhotoEntity photoEntity = findPhotoByPath(path);
        if (photoEntity != null) {
            photoRepository.deleteById(photoEntity.getIdPhoto());
        }
    }

}
