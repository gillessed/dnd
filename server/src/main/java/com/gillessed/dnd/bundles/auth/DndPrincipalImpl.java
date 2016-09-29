package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.rest.model.User;

/**
 * Created by gcole on 8/16/16.
 */
public class DndPrincipalImpl implements DndPrincipal {

    private final User user;

    public DndPrincipalImpl(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DndPrincipalImpl principal = (DndPrincipalImpl) o;

        return user != null ? user.equals(principal.user) : principal.user == null;

    }

    @Override
    public int hashCode() {
        return user != null ? user.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DndPrincipalImpl{" +
                "user=" + user.getUsername() +
                '}';
    }
}
