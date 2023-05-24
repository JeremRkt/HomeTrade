package com.isep.hometrade.service;

import com.isep.hometrade.business.*;
import com.isep.hometrade.dao.MessageRepository;
import com.isep.hometrade.dao.NoteRepository;
import com.isep.hometrade.map.HousingDto;
import com.isep.hometrade.map.MessageDto;
import com.isep.hometrade.map.NoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void saveNote(NoteDto noteDto, UserEntity userEntity, HousingEntity housingEntity) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setNote(Integer.parseInt(noteDto.getNote()));
        noteEntity.setUserEntity(userEntity);
        noteEntity.setHousingEntity(housingEntity);
        noteRepository.save(noteEntity);
    }

    public void updateNote(NoteEntity noteEntity, NoteDto noteDto) {
        noteEntity.setNote(Integer.parseInt(noteDto.getNote()));
        noteRepository.save(noteEntity);
    }

    public NoteEntity findNoteByUserAndHousing(UserEntity userEntity, HousingEntity housingEntity) {
        return noteRepository.findNoteByUserAndHousing(userEntity, housingEntity);
    }

    public Set<NoteEntity> findAllNotesByHousing(HousingEntity housingEntity) {
        List<NoteEntity> noteEntities = noteRepository.findAllByHousing(housingEntity);
        return new HashSet<>(noteEntities);
    }

    public String calculateStarMean(Set<NoteEntity> noteEntities) {
        double average = noteEntities.stream()
                .mapToDouble(NoteEntity::getNote)
                .average()
                .orElse(0.0);
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) average;
        stars.append("★".repeat(Math.max(0, fullStars)));
        int emptyStars = 5 - stars.length();
        stars.append("☆".repeat(Math.max(0, emptyStars)));
        return stars.toString();
    }

}
