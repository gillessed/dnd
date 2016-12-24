package com.gillessed.dnd.services.user;

import com.gillessed.dnd.model.auth.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUserId(String userId);
    Collection<String> getRolesForUser(User user);
}
