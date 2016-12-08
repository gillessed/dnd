package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.page.compiler.impl.PageCompilerFactoryImpl;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.gillessed.dnd.services.page.PageService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DndPageModule extends AbstractModule {

    @Override
    protected void configure() {
        // NO-OP
    }

    @Provides
    @Singleton
    public PageTokenizerFactory getPageTokenizerFactory() {
        return new PageTokenizerFactory();
    }

    @Provides
    @Singleton
    public PageParserFactory getPageParserFactory() {
        return new PageParserFactory();
    }

    @Provides
    @Singleton
    public PageCompilerFactory getPageParserFactory(PageService pageService, DndConfiguration configuration) {
        return new PageCompilerFactoryImpl(pageService, configuration);
    }
}
