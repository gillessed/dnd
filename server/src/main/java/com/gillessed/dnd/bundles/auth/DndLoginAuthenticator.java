package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.services.AuthService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DndLoginAuthenticator implements Authenticator<BasicCredentials, DndPrincipal> {
    private final AuthService authService;

    @Inject
    public DndLoginAuthenticator(AuthService authService) {
        this.authService = authService;
    }


    @Override
    public Optional<DndPrincipal> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Optional<Session> session = authService.login(credentials.getUsername(), credentials.getPassword());
        if (!session.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new DndPrincipalImpl(session.get()));
    }
}
