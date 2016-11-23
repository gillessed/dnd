package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.services.AuthService;
import com.gillessed.dnd.services.PageService;
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
            .add(PageService.class)
            .add(SearchService.class)
            .build();

    @Override
    protected void configure() {
        RESOURCE_CLASSES.forEach(this::bind);
    }

    @Provides
    @Singleton
    public Indexer searchServiceProvider(DndConfiguration configuration, PageService pageService) {
        return configuration.getIndexerFactory().buildIndexer(pageService, configuration.getRoot());
    }
}
