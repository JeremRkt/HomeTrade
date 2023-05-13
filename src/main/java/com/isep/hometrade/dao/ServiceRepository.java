package com.isep.hometrade.dao;

import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("select u from ServiceEntity u where u.housingEntity = :x")
    List<ServiceEntity> findAllByHousing(@Param("x") HousingEntity housingEntity);

}
