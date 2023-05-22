package com.isep.hometrade.dao;

import com.isep.hometrade.business.BookingEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.NoteEntity;
import com.isep.hometrade.business.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    @Query("select u from NoteEntity u where u.userEntity = :x and u.housingEntity = :y")
    NoteEntity findNoteByUserAndHousing(@Param("x") UserEntity userEntity, @Param("y") HousingEntity housingEntity);

    @Query("select u from NoteEntity u where u.housingEntity = :x")
    List<NoteEntity> findAllByHousing(@Param("x") HousingEntity housingEntity);

}
