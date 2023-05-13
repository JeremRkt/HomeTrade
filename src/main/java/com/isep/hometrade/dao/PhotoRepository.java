package com.isep.hometrade.dao;

import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    @Query("select u from PhotoEntity u where u.housingEntity = :x")
    List<PhotoEntity> findAllByHousing(@Param("x") HousingEntity housingEntity);

}
