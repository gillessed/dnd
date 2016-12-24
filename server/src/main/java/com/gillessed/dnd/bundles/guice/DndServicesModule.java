package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.services.auth.AuthService;
import com.gillessed.dnd.services.auth.impl.AuthServiceImpl;
import com.gillessed.dnd.services.page.PageProvider;
import com.gillessed.dnd.services.page.PageService;
import com.gillessed.dnd.services.page.impl.InMemoryStorePageProviderImpl;
import com.gillessed.dnd.services.page.impl.PageFileCrawler;
import com.gillessed.dnd.services.page.impl.PageServiceImpl;
import com.gillessed.dnd.services.search.SearchService;
import com.gillessed.dnd.services.session.SessionService;
import com.gillessed.dnd.services.session.SessionServiceImpl;
import com.gillessed.dnd.services.user.RoleProvider;
import com.gillessed.dnd.services.user.UserProvider;
import com.gillessed.dnd.services.user.UserService;
import com.gillessed.dnd.services.user.impl.FileRoleProvider;
import com.gillessed.dnd.services.user.impl.FileUserProvider;
import com.gillessed.dnd.services.user.impl.UserServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DndServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageService.class).to(PageServiceImpl.class);
        bind(SessionService.class).to(SessionServiceImpl.class);
        bind(AuthService.class).to(AuthServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(PageProvider.class).to(InMemoryStorePageProviderImpl.class);
        bind(PageFileCrawler.class);
        bind(SearchService.class);
    }

    @Provides
    @Singleton
    public UserProvider getUserProvider(DndConfiguration configuration) {
        return new FileUserProvider();
    }

    @Provides
    @Singleton
    public RoleProvider getRoleProvider(DndConfiguration configuration) {
        return new FileRoleProvider();
    }
}
