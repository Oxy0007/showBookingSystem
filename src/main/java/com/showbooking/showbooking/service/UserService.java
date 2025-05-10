package com.showbooking.showbooking.service;

import com.showbooking.showbooking.model.User;

public interface UserService {

    User getOrCreateUser(String name);

    User getUserById(String id);

    User getUserByName(String name);
}
