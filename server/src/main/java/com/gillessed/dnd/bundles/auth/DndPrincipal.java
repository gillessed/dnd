package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.model.auth.Session;

public interface DndPrincipal extends java.security.Principal{
    Session getSession();
}
