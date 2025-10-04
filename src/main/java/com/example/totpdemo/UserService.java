package com.example.totpdemo;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    // In-memory user store for demo purposes
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void registerUser(String username, String password, String secret) {
        users.put(username, new User(username, password, secret));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }
}
