package com.showbooking.showbooking.service.impl;

import com.showbooking.showbooking.exception.CommonCustomException;
import com.showbooking.showbooking.model.User;
import com.showbooking.showbooking.repository.UserRepository;
import com.showbooking.showbooking.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getOrCreateUser(String name) {
        return userRepository.getOrCreateUser(name);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CommonCustomException("User not found with id: " + id));
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElse(null);
    }
}
