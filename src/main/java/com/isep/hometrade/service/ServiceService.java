package com.isep.hometrade.service;

import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.ServiceEntity;
import com.isep.hometrade.dao.ServiceRepository;
import com.isep.hometrade.map.ServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void saveService(ServiceDto serviceDto, HousingEntity housingEntity) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(serviceDto.getName());
        serviceEntity.setHousingEntity(housingEntity);
        serviceRepository.save(serviceEntity);
    }

    public Set<ServiceEntity> findServicesByHousing(HousingEntity housingEntity) {
        List<ServiceEntity> services = serviceRepository.findAllByHousing(housingEntity);
        return new HashSet<>(services);
    }

    public void deleteServiceById(Long id) {
        serviceRepository.deleteById(id);
    }

}
