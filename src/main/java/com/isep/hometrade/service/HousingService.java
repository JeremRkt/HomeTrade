package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.HousingRepository;
import com.isep.hometrade.map.HousingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HousingService {

    private final HousingRepository housingRepository;

    @Autowired
    public HousingService(HousingRepository housingRepository) {
        this.housingRepository = housingRepository;
    }

    public HousingEntity saveHousing(HousingDto housingDto, String uuid, AddressEntity addressEntity, UserEntity userEntity) {
        HousingEntity housingEntity = new HousingEntity();
        housingEntity.setType(housingDto.getType());
        housingEntity.setName(housingDto.getName());
        housingEntity.setDescription(housingDto.getDescription());
        housingEntity.setUuid(uuid);
        Date date = new Date();
        housingEntity.setCreation(date);
        housingEntity.setModification(date);
        housingEntity.setAddressEntity(addressEntity);
        housingEntity.setUserEntity(userEntity);
        housingRepository.save(housingEntity);
        return housingEntity;
    }

    public void updateHousing(HousingEntity housingEntity, HousingDto housingDto, AddressEntity addressEntity) {
        housingEntity.setType(housingDto.getType());
        housingEntity.setName(housingDto.getName());
        housingEntity.setDescription(housingDto.getDescription());
        housingEntity.setModification(new Date());
        housingEntity.setAddressEntity(addressEntity);
        housingRepository.save(housingEntity);
    }

    public Set<HousingEntity> findHousingsByUser(UserEntity userEntity) {
        return userEntity.getHousingEntities();
    }

    public HousingEntity findHousingById(Long id) {
        return housingRepository.findById(id).orElse(null);
    }

    public AddressEntity deleteHousingById(Long id) {
        HousingEntity housingEntity = findHousingById(id);
        AddressEntity addressEntity = housingEntity.getAddressEntity();
        housingRepository.delete(housingEntity);
        return addressEntity;
    }

    public Set<HousingEntity> find5LastHousings() {
        Pageable pageable = PageRequest.of(0, 5);
        List<HousingEntity> housingEntities = housingRepository.findLastHousings(pageable);
        return new HashSet<>(housingEntities);
    }

}