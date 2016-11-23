package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.Session;

/**
 * Created by gcole on 8/16/16.
 */
public class DndPrincipalImpl implements DndPrincipal {

    private final Session session;

    public DndPrincipalImpl(Session session) {
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getName() {
        return session.getUser().getUserId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DndPrincipalImpl that = (DndPrincipalImpl) o;

        return session != null ? session.equals(that.session) : that.session == null;

    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }
}
