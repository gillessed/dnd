package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class PageCompilerFactoryImpl implements PageCompilerFactory {

    @Inject
    public PageCompilerFactoryImpl() {
    }

    @Override
    public PageCompiler createPageCompiler(List<WikiLink> linkList) {
        return new PageCompilerImpl(linkList);
    }
}
