package com.gillessed.dnd.services.user.impl;

import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.services.user.RoleProvider;
import com.gillessed.dnd.services.user.UserProvider;
import com.gillessed.dnd.services.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collection;
import java.util.Optional;

@Singleton
public class UserServiceImpl implements UserService {
    private final UserProvider userProvider;
    private final RoleProvider roleProvider;

    @Inject
    public UserServiceImpl(
            UserProvider userProvider,
            RoleProvider roleProvider) {
        this.userProvider = userProvider;
        this.roleProvider = roleProvider;
    }

    @Override
    public Optional<User> getUserByUserId(String userId) {
        return userProvider.getUserByUserId(userId);
    }

    @Override
    public Collection<String> getRolesForUser(User user) {
        return roleProvider.getRolesForUser(user.getUsername());
    }
}
