package com.gillessed.dnd.bundles.guice;

import com.gillessed.dnd.page.PageTransformer;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.page.compiler.impl.PageCompilerFactoryImpl;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DndPageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageTransformer.class);
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
    public PageCompilerFactory getPageCompilerFactory() {
        return new PageCompilerFactoryImpl();
    }
}
