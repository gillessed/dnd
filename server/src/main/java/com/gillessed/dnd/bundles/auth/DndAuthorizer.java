package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.services.auth.AuthService;
import io.dropwizard.auth.Authorizer;

public class DndAuthorizer implements Authorizer<DndPrincipal> {
    private final AuthService authService;

    @javax.inject.Inject
    public DndAuthorizer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean authorize(DndPrincipal principal, String role) {
        User user = principal.getSession().getUser();
        return false;
    }
}
