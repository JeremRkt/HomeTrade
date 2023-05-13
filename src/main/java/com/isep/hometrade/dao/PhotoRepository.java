package com.isep.hometrade.dao;

import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    @Query("select u from PhotoEntity u where u.path like :x")
    PhotoEntity findByPath(@Param("x")String path);

    @Query("select u from PhotoEntity u where u.houseEntity = :x")
    List<PhotoEntity> findAllByHouse(@Param("x") HouseEntity houseEntity);

}
