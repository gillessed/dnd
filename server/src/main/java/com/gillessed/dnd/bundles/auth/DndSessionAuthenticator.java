package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.services.SessionService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DndSessionAuthenticator implements Authenticator<DndBearerTokenCredential, DndPrincipal> {

    private final SessionService sessionService;

    @Inject
    public DndSessionAuthenticator(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Optional<DndPrincipal> authenticate(DndBearerTokenCredential credentials) throws AuthenticationException {
        Optional<Session> session = sessionService.verifyToken(credentials.getBearerToken());
        if (!session.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new DndPrincipalImpl(session.get()));
    }
}
