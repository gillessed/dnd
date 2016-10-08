package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.page.compiler.impl.PageCompilerImpl;
import com.google.inject.Singleton;

@Singleton
public class PageCompilerFactory {
    private final String root;

    public PageCompilerFactory(String root) {
        this.root = root;
    }

    public PageCompiler createPageCompiler() {
        return new PageCompilerImpl(root);
    }
}
