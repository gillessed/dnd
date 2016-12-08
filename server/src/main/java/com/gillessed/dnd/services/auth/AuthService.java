package com.gillessed.dnd.services.auth;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.services.SessionService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Optional;

@Singleton
public class AuthService {

    private final SessionService sessionService;
    private final UserProvider userProvider;

    @Inject
    public AuthService(SessionService sessionService) {
        this.sessionService = sessionService;
        userProvider = new FileUserProvider("files/users.yml");
    }

    public Optional<Session> login(String username, String password) {
        Optional<User> user = userProvider.getUserByUsername(username);

        if(!user.isPresent() || !user.get().getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(sessionService.createSessionForUser(user.get()));
    }
}
