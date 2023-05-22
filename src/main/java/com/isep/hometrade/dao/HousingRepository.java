package com.isep.hometrade.dao;

import com.isep.hometrade.business.HousingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HousingRepository extends JpaRepository<HousingEntity, Long> {

    @Query("select u from HousingEntity u order by u.modification desc")
    List<HousingEntity> findLastHousings(Pageable pageable);

    @Query("select u from HousingEntity u where u.addressEntity.city = :x ")
    List<HousingEntity> findHousingsByCity(Pageable pageable, @Param("x")String city);

    @Query("select u from HousingEntity u where u.addressEntity.country = :x ")
    List<HousingEntity> findHousingsByCountry(Pageable pageable, @Param("x")String country);

    @Query("select u from HousingEntity u where u.addressEntity.city = :city and u.addressEntity.country = :country")
    List<HousingEntity> findHousingsByCityAndCountry(Pageable pageable, @Param("city") String city, @Param("country")String country);

}
