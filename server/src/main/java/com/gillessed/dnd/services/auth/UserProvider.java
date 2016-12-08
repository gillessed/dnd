package com.gillessed.dnd.services.auth;

import com.gillessed.dnd.model.auth.User;

import java.util.Optional;

public interface UserProvider {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByUserId(String userId);
}
