package com.isep.hometrade.dao;

import com.isep.hometrade.business.HousingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HousingRepository extends JpaRepository<HousingEntity, Long> {

    @Query("select u from HousingEntity u order by u.createdDate desc")
    List<HousingEntity> findLastHousings(Pageable pageable);

}
