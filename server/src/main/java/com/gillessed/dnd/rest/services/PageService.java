package com.gillessed.dnd.rest.services;

import com.gillessed.dnd.page.compiler.impl.PageCompilerImpl;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.impl.ElementParserImpl;
import com.gillessed.dnd.page.token.Token;
import com.gillessed.dnd.page.token.impl.PageTokenizerImpl;
import com.gillessed.dnd.rest.model.page.WikiPage;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Singleton
public class PageService {

    @Inject
    public PageService() {
    }

    public WikiPage getPage(String path) throws IOException, ParsingException {
        Path markdownFile = Paths.get("pages/karyus");
        String text = new String(Files.readAllBytes(markdownFile));
        List<Token> tokens = PageTokenizerImpl.getTokens(text);
        List<Element> elements = ElementParserImpl.getElements(tokens);
        WikiPage page = PageCompilerImpl.getPage(elements);
        return page;
    }
}
