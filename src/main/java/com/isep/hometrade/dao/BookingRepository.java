package com.isep.hometrade.dao;

import com.isep.hometrade.business.BookingEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select u from BookingEntity u where u.housingEntity = :x and u.status = 'Pending'")
    List<BookingEntity> findAllPendingByHousing(@Param("x") HousingEntity housingEntity);

    @Query("select u from BookingEntity u where u.housingEntity = :x and u.status = 'Accepted'")
    List<BookingEntity> findAllAcceptedByHousing(@Param("x") HousingEntity housingEntity);

    @Query("select u from BookingEntity u where u.housingEntity = :x and u.status = 'Declined'")
    List<BookingEntity> findAllDeclinedByHousing(@Param("x") HousingEntity housingEntity);

    @Query("select u from BookingEntity u where u.userEntity = :x and u.status = 'Pending'")
    List<BookingEntity> findAllPendingByUser(@Param("x") UserEntity userEntity);

    @Query("select u from BookingEntity u where u.userEntity = :x and u.status = 'Accepted'")
    List<BookingEntity> findAllAcceptedByUser(@Param("x") UserEntity userEntity);

    @Query("select u from BookingEntity u where u.userEntity = :x and u.status = 'Declined'")
    List<BookingEntity> findAllDeclinedByUser(@Param("x") UserEntity userEntity);

    @Query("select u from BookingEntity u where u.userEntity = :x")
    List<BookingEntity> findByUser(@Param("x") UserEntity userEntity);

    @Query("select u from BookingEntity u where u.housingEntity = :x and u.userEntity = :y and u.status = 'Accepted'")
    BookingEntity findAcceptedByHousingAndUser(@Param("x") HousingEntity housingEntity, @Param("y") UserEntity userEntity);

    @Query("select u from BookingEntity u where u.housingEntity = :x and u.userEntity = :y and u.status = 'Pending'")
    BookingEntity findPendingByHousingAndUser(@Param("x") HousingEntity housingEntity, @Param("y") UserEntity userEntity);

}
