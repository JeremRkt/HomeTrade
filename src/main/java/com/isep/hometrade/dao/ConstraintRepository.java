package com.isep.hometrade.dao;

import com.isep.hometrade.business.ConstraintEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstraintRepository extends JpaRepository<ConstraintEntity, Long> {

    @Query("select u from ConstraintEntity u where u.housingEntity = :x")
    List<ConstraintEntity> findAllByHousing(@Param("x") HousingEntity housingEntity);

}
