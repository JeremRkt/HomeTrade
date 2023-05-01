package com.isep.hometrade.service;

import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.UserRepository;
import com.isep.hometrade.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        UserEntity userEntity = new UserEntity(userDto.getIdUser(), userDto.getFirstname(), userDto.getLastname(),userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), 1);
        userRepository.save(userEntity);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
