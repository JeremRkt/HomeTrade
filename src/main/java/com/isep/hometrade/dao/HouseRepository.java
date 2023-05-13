package com.isep.hometrade.dao;

import com.isep.hometrade.business.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HouseRepository extends JpaRepository<HouseEntity, Long> {

    @Query("select u from HouseEntity u order by u.idHouse desc ")
    List<HouseEntity> findLatestHouses(Pageable pageable);

}
