package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.dao.AddressRepository;
import com.isep.hometrade.map.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressEntity saveAddress(AddressDto addressDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setNumber(addressDto.getNumber());
        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setCode(addressDto.getCode());
        addressEntity.setCountry(addressDto.getCountry());
        addressRepository.save(addressEntity);
        return addressEntity;
    }

    public void updateAddress(AddressEntity addressEntity, AddressDto addressDto) {
        addressEntity.setNumber(addressDto.getNumber());
        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setCode(addressDto.getCode());
        addressEntity.setCountry(addressDto.getCountry());
        addressRepository.save(addressEntity);
    }

    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }

}
