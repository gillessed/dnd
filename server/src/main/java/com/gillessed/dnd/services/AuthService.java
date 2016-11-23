package com.gillessed.dnd.services;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.auth.User;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AuthService {
    private static final User ADMIN = new User("1", "admin", "palantir");

    private final Map<String, User> users;
    private final Map<String, User> tokens;

    @Inject
    public AuthService() {
        users = new HashMap<>(ImmutableMap.<String, User>builder()
                .put("admin", ADMIN)
                .build());
        tokens = new HashMap<>(ImmutableMap.<String, User>builder()
                .put("token", ADMIN)
                .build());
    }

    public Optional<Session> login(String username, String password) {
        User user = users.getOrDefault(username, null);
        if(user == null || !user.getPassword().equals(password)) {
            return Optional.empty();
        }

        String token = generateToken(user);
        tokens.put(token, user);
        return Optional.of(new Session(token, user));
    }

    private String generateToken(User user) {
        String newToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        newToken = newToken.replace("-", "");
        tokens.put(newToken, user);
        return newToken;
    }

    public Optional<Session> verifyToken(String token) {
        User user = tokens.getOrDefault(token, null);
        if(user == null) {
            return Optional.empty();
        }
        return Optional.of(new Session(token, user));
    }
}
