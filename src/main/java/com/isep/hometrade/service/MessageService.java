package com.isep.hometrade.service;

import com.isep.hometrade.business.BookingEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.MessageEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.BookingRepository;
import com.isep.hometrade.dao.MessageRepository;
import com.isep.hometrade.map.BookingDto;
import com.isep.hometrade.map.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(MessageDto messageDto, UserEntity userEntity, HousingEntity housingEntity) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(messageDto.getContent());
        messageEntity.setUserEntity(userEntity);
        messageEntity.setHousingEntity(housingEntity);
        messageRepository.save(messageEntity);
    }

    public MessageEntity saveAnswerMessage(MessageDto messageDto, UserEntity userEntity) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(messageDto.getContent());
        messageEntity.setUserEntity(userEntity);
        return messageRepository.save(messageEntity);
    }

    public void saveAnswer(MessageDto messageDto, UserEntity userEntity, MessageEntity messageEntity) {
        MessageEntity newMessageEntity = saveAnswerMessage(messageDto, userEntity);
        messageEntity.setMessageEntity(newMessageEntity);
        messageRepository.save(messageEntity);
    }

    public Set<MessageEntity> findWithoutAnswerMessagesBySuper(UserEntity userEntity) {
        Set<MessageEntity> messageEntities = new HashSet<>();
        Set<HousingEntity> housingEntities = userEntity.getHousingEntities();
        for (HousingEntity housingEntity : housingEntities) {
            List<MessageEntity> messageEntitiesByHousing = findWithoutAnswerMessagesByHousing(housingEntity);
            messageEntities.addAll(messageEntitiesByHousing);
        }
        return messageEntities;
    }

    public List<MessageEntity> findWithoutAnswerMessagesByHousing(HousingEntity housingEntity) {
        return messageRepository.findAllWithoutAnswerByHousing(housingEntity);
    }

    public Set<MessageEntity> findWithAnswerMessagesBySuper(UserEntity userEntity) {
        Set<MessageEntity> messageEntities = new HashSet<>();
        Set<HousingEntity> housingEntities = userEntity.getHousingEntities();
        for (HousingEntity housingEntity : housingEntities) {
            List<MessageEntity> messageEntitiesByHousing = findWithAnswerMessagesByHousing(housingEntity);
            messageEntities.addAll(messageEntitiesByHousing);
        }
        return messageEntities;
    }

    public List<MessageEntity> findWithAnswerMessagesByHousing(HousingEntity housingEntity) {
        return messageRepository.findAllWithAnswerByHousing(housingEntity);
    }

    public Set<MessageEntity> findWithoutAnswerMessagesByUser(UserEntity userEntity) {
        List<MessageEntity> messageEntities = messageRepository.findAllWithoutAnswerByUser(userEntity);
        return new HashSet<>(messageEntities);
    }

    public Set<MessageEntity> findWithAnswerMessagesByUser(UserEntity userEntity) {
        List<MessageEntity> messageEntities = messageRepository.findAllWithAnswerByUser(userEntity);
        return new HashSet<>(messageEntities);
    }

    public MessageEntity findMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

}
