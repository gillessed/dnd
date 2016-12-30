package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.services.user.UserService;
import io.dropwizard.auth.Authorizer;

import java.util.Collection;

public class DndAuthorizer implements Authorizer<DndPrincipal> {
    private final UserService userService;

    @javax.inject.Inject
    public DndAuthorizer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authorize(DndPrincipal principal, String role) {
        User user = principal.getSession().getUser();
        Collection<String> roles = userService.getRolesForUser(user);
        return roles.contains(role);
    }
}
