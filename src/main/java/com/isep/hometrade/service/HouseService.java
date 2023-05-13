package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.HouseRepository;
import com.isep.hometrade.map.HouseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HouseService {

    private final HouseRepository houseRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public HouseEntity saveHouse(HouseDto houseDto, String uuid, AddressEntity addressEntity, UserEntity userEntity) {
        HouseEntity houseEntity = new HouseEntity();
        houseEntity.setName(houseDto.getName());
        houseEntity.setDescription(houseDto.getDescription());
        houseEntity.setUuid(uuid);
        houseEntity.setAddressEntity(addressEntity);
        houseEntity.setUserEntity(userEntity);
        houseRepository.save(houseEntity);
        return houseEntity;
    }

    public void updateHouse(HouseEntity houseEntity, HouseDto houseDto, AddressEntity addressEntity) {
        houseEntity.setName(houseDto.getName());
        houseEntity.setDescription(houseDto.getDescription());
        houseEntity.setAddressEntity(addressEntity);
        houseRepository.save(houseEntity);
    }

    public Set<HouseEntity> findHousesByUser(UserEntity userEntity) {
        return userEntity.getHouseEntities();
    }

    public HouseEntity findHouseById(Long id) {
        return houseRepository.findById(id).orElse(null);
    }

    public AddressEntity deleteHouseById(Long id) {
        AddressEntity addressEntity = findHouseById(id).getAddressEntity();
        houseRepository.deleteById(id);
        return addressEntity;
    }

    public Set<HouseEntity> find5LatestHouses() {
        Pageable pageable = PageRequest.of(0, 5);
        List<HouseEntity> houseEntities = houseRepository.findLatestHouses(pageable);
        return new HashSet<>(houseEntities);
    }

}