package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.dao.AddressRepository;
import com.isep.hometrade.dao.HouseRepository;
import com.isep.hometrade.util.HouseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseService {
    private final HouseRepository houseRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository, AddressRepository addressRepository) {
        this.houseRepository = houseRepository;
        this.addressRepository = addressRepository;
    }

    public void saveHouse(HouseDto houseDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setNumber(houseDto.getAddressEntity().getNumber());
        addressEntity.setStreet(houseDto.getAddressEntity().getStreet());
        addressEntity.setCity(houseDto.getAddressEntity().getCity());
        addressEntity.setCode(houseDto.getAddressEntity().getCode());
        addressEntity.setCountry(houseDto.getAddressEntity().getCountry());
        HouseEntity houseEntity = new HouseEntity();
        houseEntity.setTitle(houseDto.getTitle());
        houseEntity.setDescription(houseDto.getDescription());
        houseEntity.setPhoto(houseDto.getPhoto());
        houseEntity.setAddressEntity(addressEntity);
        addressRepository.save(addressEntity);
        houseRepository.save(houseEntity);
    }

}
