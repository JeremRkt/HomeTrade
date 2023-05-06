package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.AddressRepository;
import com.isep.hometrade.dao.HouseRepository;
import com.isep.hometrade.util.FileUpload;
import com.isep.hometrade.map.HouseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class HouseService {
    private final HouseRepository houseRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository, AddressRepository addressRepository) {
        this.houseRepository = houseRepository;
        this.addressRepository = addressRepository;
    }

    public void saveHouse(HouseDto houseDto, UserEntity userEntity) throws IOException {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setNumber(houseDto.getAddressEntity().getNumber());
        addressEntity.setStreet(houseDto.getAddressEntity().getStreet());
        addressEntity.setCity(houseDto.getAddressEntity().getCity());
        addressEntity.setCode(houseDto.getAddressEntity().getCode());
        addressEntity.setCountry(houseDto.getAddressEntity().getCountry());
        HouseEntity houseEntity = new HouseEntity();
        houseEntity.setUserEntity(userEntity);
        houseEntity.setTitle(houseDto.getTitle());
        houseEntity.setDescription(houseDto.getDescription());
        houseEntity.setAddressEntity(addressEntity);
        addressRepository.save(addressEntity);
        houseRepository.save(houseEntity);
        Set<PhotoEntity> photoEntities = new HashSet<>();
        for (MultipartFile multipartFile : houseDto.getPhotoEntities()) {
            !!!!!Vérifier dans le controller pas ici
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String name = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                String genericPath = "images/house-" + houseEntity.getIdHouse();
                FileUpload.saveFile("C:/wamp64/www/" + genericPath, name, multipartFile);
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setLink("http://localhost/" + genericPath + "/" + name);
                photoEntity.setHouseEntity(houseEntity);
                photoEntities.add(photoEntity);
            }
        }
        houseEntity.setPhotoEntities(photoEntities);
        houseRepository.save(houseEntity);
    }


}
