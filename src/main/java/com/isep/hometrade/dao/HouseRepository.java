package com.isep.hometrade.dao;

import com.isep.hometrade.business.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<HouseEntity, Long> {
}
