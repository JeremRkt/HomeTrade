package com.isep.hometrade.service;

import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserEntity user) {
        UserEntity u = new UserEntity(user.getFirstname(), user.getLastname(),user.getEmail(), passwordEncoder.encode(user.getPassword()));
        userRepository.save(u);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}