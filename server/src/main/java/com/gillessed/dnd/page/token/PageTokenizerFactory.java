package com.gillessed.dnd.page.token;

import com.gillessed.dnd.page.token.impl.PageTokenizerImpl;
import com.google.inject.Singleton;

@Singleton
public class PageTokenizerFactory {
    public PageTokenizer createPageTokenizer() {
        return new PageTokenizerImpl();
    }
}
