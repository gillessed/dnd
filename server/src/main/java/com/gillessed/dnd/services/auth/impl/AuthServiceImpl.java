package com.gillessed.dnd.services.auth.impl;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.services.auth.AuthService;
import com.gillessed.dnd.services.session.SessionServiceImpl;
import com.gillessed.dnd.services.user.UserProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Optional;

@Singleton
public class AuthServiceImpl implements AuthService {

    private final SessionServiceImpl sessionService;
    private final UserProvider userProvider;

    @Inject
    public AuthServiceImpl(
            SessionServiceImpl sessionService,
            UserProvider userProvider) {
        this.sessionService = sessionService;
        this.userProvider = userProvider;
    }

    public Optional<Session> login(String username, String password) {
        Optional<User> user = userProvider.getUserByUsername(username);

        if(!user.isPresent() || !user.get().getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(sessionService.createSessionForUser(user.get()));
    }
}
