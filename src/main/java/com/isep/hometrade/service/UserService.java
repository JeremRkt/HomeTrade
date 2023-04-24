package com.isep.hometrade.service;

import com.isep.hometrade.business.User;
import com.isep.hometrade.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void saveUser(User user) {
        User u = new User(user.getFirstname(), user.getLastname(),user.getEmail(), passwordEncoder.encode(user.getPassword()));
        userRepository.save(u);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
