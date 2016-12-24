package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.rest.resources.AuthResource;
import com.gillessed.dnd.rest.resources.PageResource;
import com.gillessed.dnd.rest.resources.SearchResource;
import com.gillessed.dnd.rest.resources.UserResource;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;

import java.util.List;

public class DndResourcesModule extends AbstractModule {

    public static List<Class<?>> RESOURCE_CLASSES = ImmutableList.<Class<?>>builder()
            .add(AuthResource.class)
            .add(PageResource.class)
            .add(SearchResource.class)
            .add(UserResource.class)
            .build();

    @Override
    protected void configure() {
        RESOURCE_CLASSES.forEach(this::bind);
    }
}
