package com.showbooking.showbooking.repository;

import com.showbooking.showbooking.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByName(String name) {
        return users.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    public User getOrCreateUser(String name) {
        Optional<User> existingUser = findByName(name);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User(name);
            return save(newUser);
        }
    }
}
