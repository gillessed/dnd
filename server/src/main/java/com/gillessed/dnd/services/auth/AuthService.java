package com.gillessed.dnd.services.auth;

import com.gillessed.dnd.model.auth.Session;

import java.util.Optional;

public interface AuthService {
    Optional<Session> login(String username, String password);
}
