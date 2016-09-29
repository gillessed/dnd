package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.rest.services.AuthService;
import com.gillessed.dnd.rest.services.PageService;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;

import java.util.List;

/**
 * Created by gcole on 8/16/16.
 */
public class DndServicesModule extends AbstractModule {

    public static List<Class<?>> RESOURCE_CLASSES = ImmutableList.<Class<?>>builder()
            .add(AuthService.class)
            .add(PageService.class)
            .build();

    @Override
    protected void configure() {
        RESOURCE_CLASSES.forEach((Class<?> resourceClass) -> bind(resourceClass));
    }
}
