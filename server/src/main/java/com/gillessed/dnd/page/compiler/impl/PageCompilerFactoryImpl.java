package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.services.page.PageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PageCompilerFactoryImpl implements PageCompilerFactory {
    private final PageService pageService;
    private final DndConfiguration configuration;

    @Inject
    public PageCompilerFactoryImpl(PageService pageService, DndConfiguration configuration) {
        this.pageService = pageService;
        this.configuration = configuration;
    }

    public PageCompiler createPageCompiler() {
        return new PageCompilerImpl(pageService, configuration.getRoot());
    }
}
