package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.services.auth.AuthService;
import com.gillessed.dnd.services.page.PageCache;
import com.gillessed.dnd.services.page.PageService;
import com.gillessed.dnd.services.page.impl.PageServiceImpl;
import com.gillessed.dnd.services.search.SearchService;
import com.gillessed.dnd.services.search.index.Indexer;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.List;

public class DndServicesModule extends AbstractModule {

    public static List<Class<?>> RESOURCE_CLASSES = ImmutableList.<Class<?>>builder()
            .add(AuthService.class)
            .add(SearchService.class)
            .add(PageCache.class)
            .build();

    @Override
    protected void configure() {
        RESOURCE_CLASSES.forEach(this::bind);
        bind(PageService.class).to(PageServiceImpl.class);
    }

    @Provides
    @Singleton
    public Indexer getIndexer(DndConfiguration configuration, PageService pageService) {
        return configuration.getIndexerFactory().buildIndexer(pageService, configuration.getRoot());
    }
}
