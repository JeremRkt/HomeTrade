package com.isep.hometrade.service;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.UserRepository;
import com.isep.hometrade.map.HousingDto;
import com.isep.hometrade.map.SpecialUserDto;
import com.isep.hometrade.map.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(userDto.getFirstname());
        userEntity.setLastname(userDto.getLastname());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setType("User");
        userRepository.save(userEntity);
    }

    public void updateUser(UserEntity userEntity, SpecialUserDto specialUserDto) {
        userEntity.setLastname(specialUserDto.getLastname());
        userEntity.setFirstname(specialUserDto.getFirstname());
        userEntity.setEmail(specialUserDto.getEmail());
        userEntity.setType(specialUserDto.getType());
        userRepository.save(userEntity);
    }

    public UserEntity deleteUserById(Long id) {
        UserEntity userEntity = findUserById(id);
        userRepository.delete(userEntity);
        return userEntity;
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<UserEntity> findUsersByUser(UserEntity userEntity) {
        List<UserEntity> userEntities = userRepository.findAll();
        userEntities.remove(userEntity);
        return new HashSet<>(userEntities);
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
