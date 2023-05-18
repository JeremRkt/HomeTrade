package com.isep.hometrade.dao;

import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.MessageEntity;
import com.isep.hometrade.business.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("select u from MessageEntity u where u.housingEntity = :x and u.messageEntity = null")
    List<MessageEntity> findAllWithoutAnswerByHousing(@Param("x") HousingEntity housingEntity);

    @Query("select u from MessageEntity u where u.housingEntity = :x and u.messageEntity != null")
    List<MessageEntity> findAllWithAnswerByHousing(@Param("x") HousingEntity housingEntity);

    @Query("select u from MessageEntity u where u.userEntity = :x and u.messageEntity = null")
    List<MessageEntity> findAllWithoutAnswerByUser(@Param("x") UserEntity userEntity);

    @Query("select u from MessageEntity u where u.userEntity = :x and u.messageEntity != null")
    List<MessageEntity> findAllWithAnswerByUser(@Param("x") UserEntity userEntity);

}
