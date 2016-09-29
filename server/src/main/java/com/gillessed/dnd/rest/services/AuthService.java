package com.gillessed.dnd.rest.services;

import com.gillessed.dnd.rest.model.User;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AuthService {
    private final Map<String, User> users;
    private final Map<String, User> tokens;

    public AuthService() {
        users = new ConcurrentHashMap<>();
        users.put("123", new User("123", "456"));
        tokens = new ConcurrentHashMap<>();
        tokens.put("admin", users.get("123"));
    }

    public Optional<String> login(String username, String password) {
        User user = users.getOrDefault(username, null);
        if(user == null || !user.getPassword().equals(password)) {
            return Optional.empty();
        }
        String token = generateToken(user);
        tokens.put(token, user);
        return Optional.of(token);
    }

    private String generateToken(User user) {
        String newToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        newToken = newToken.replace("-", "");
        tokens.put(newToken, user);
        return newToken;
    }

    public Optional<User> verifyToken(String token) {
        if(token == null || !tokens.containsKey(token)) {
            return Optional.empty();
        }
        return Optional.of(tokens.get(token));
    }
}
