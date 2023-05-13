package com.isep.hometrade.service;

import com.isep.hometrade.business.ConstraintEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.dao.ConstraintRepository;
import com.isep.hometrade.map.ConstraintDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConstraintService {

    private final ConstraintRepository constraintRepository;

    @Autowired
    public ConstraintService(ConstraintRepository constraintRepository) {
        this.constraintRepository = constraintRepository;
    }

    public void saveConstraint(ConstraintDto constraintDto, HousingEntity housingEntity) {
        ConstraintEntity constraintEntity = new ConstraintEntity();
        constraintEntity.setName(constraintDto.getName());
        constraintEntity.setHousingEntity(housingEntity);
        constraintRepository.save(constraintEntity);
    }

    public Set<ConstraintEntity> findConstraintsByHousing(HousingEntity housingEntity) {
        List<ConstraintEntity> constraints = constraintRepository.findAllByHousing(housingEntity);
        return new HashSet<>(constraints);
    }

}
