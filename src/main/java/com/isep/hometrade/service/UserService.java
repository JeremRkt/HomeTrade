package com.isep.hometrade.service;

import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.UserRepository;
import com.isep.hometrade.map.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userEntity.setType(1);
        userRepository.save(userEntity);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<HouseEntity> findHousesByEmail(String email) {
        return findUserByEmail(email).getHouseEntities();
    }

}
