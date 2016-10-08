package com.gillessed.dnd.page.parser;

import com.gillessed.dnd.page.parser.impl.ElementParserImpl;

public class PageParserFactory {
    public ElementParser createPageParser() {
        return new ElementParserImpl();
    }
}
