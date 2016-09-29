package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.rest.model.User;
import com.gillessed.dnd.rest.services.AuthService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DndSessionAuthenticator implements Authenticator<DndBearerTokenCredential, DndPrincipal> {

    private final AuthService authService;

    @Inject
    public DndSessionAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<DndPrincipal> authenticate(DndBearerTokenCredential credentials) throws AuthenticationException {
        Optional<User> user = authService.verifyToken(credentials.getBearerToken());
        if (!user.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new DndPrincipalImpl(user.get()));
    }
}