package com.isep.hometrade.service;

import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.dao.PhotoRepository;
import com.isep.hometrade.map.PhotoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void savePhoto(PhotoDto photoDto, HousingEntity housingEntity) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setName(photoDto.getName());
        photoEntity.setPath(photoDto.getPath());
        photoEntity.setHousingEntity(housingEntity);
        photoRepository.save(photoEntity);
    }

    public Set<PhotoEntity> findPhotosByHousing(HousingEntity housingEntity) {
        List<PhotoEntity> photos = photoRepository.findAllByHousing(housingEntity);
        return new HashSet<>(photos);
    }

    public void deletePhotoById(Long id) {
        photoRepository.deleteById(id);
    }

}
