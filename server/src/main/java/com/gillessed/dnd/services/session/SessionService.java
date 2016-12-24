package com.gillessed.dnd.services.session;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.auth.User;

import java.util.Optional;

public interface SessionService {
    Session createSessionForUser(User user);
    Optional<Session> verifyToken(String token);
}
