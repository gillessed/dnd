package com.gillessed.dnd.bundles.auth;

import com.gillessed.dnd.rest.model.User;

/**
 * Created by gcole on 8/16/16.
 */
public interface DndPrincipal extends java.security.Principal{
    User getUser();
}
