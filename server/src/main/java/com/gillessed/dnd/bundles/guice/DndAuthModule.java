package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.bundles.auth.DndPrincipal;
import com.gillessed.dnd.bundles.auth.RequestPrincipalFilter;
import com.gillessed.dnd.model.auth.Session;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;

import javax.servlet.http.HttpServletRequest;

public class DndAuthModule extends AbstractModule {
    @Override
    protected void configure() {
        //NO-OP
    }

    @Provides
    @RequestScoped
    public DndPrincipal getMangaReaderPrincipal(HttpServletRequest request) {
        Object principal = request.getAttribute(RequestPrincipalFilter.PRINCIPAL_ATTRIBUTE_KEY);
        Preconditions.checkState(principal instanceof DndPrincipal, "Cannot find auth principle.");
        return (DndPrincipal) principal;
    }

    @Provides
    @RequestScoped
    public Session getUser(DndPrincipal principal) {
        return principal.getSession();
    }
}
