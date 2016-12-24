package com.gillessed.dnd.services.user;

import java.util.Collection;

public interface RoleProvider {
    Collection<String> getRolesForUser(String user);
    boolean doesUserHaveRole(String user, String role);
    void addRolesToUser(String user, Collection<String> roles);
    void removeRolesFromUser(String user, Collection<String> roles);
    void removeRolesFromAllUsers(Collection<String> roles);
}
