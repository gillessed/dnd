package com.gillessed.dnd.rest.services;

import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.gillessed.dnd.page.token.Token;
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

    private final PageTokenizerFactory pageTokenizerFactory;
    private final PageParserFactory pageParserFactory;
    private final PageCompilerFactory pageCompilerFactory;

    @Inject
    public PageService(
            PageTokenizerFactory pageTokenizerFactory,
            PageParserFactory pageParserFactory,
            PageCompilerFactory pageCompilerFactory) {
        this.pageTokenizerFactory = pageTokenizerFactory;
        this.pageParserFactory = pageParserFactory;
        this.pageCompilerFactory = pageCompilerFactory;
    }

    public WikiPage getPage(String path) throws IOException, ParsingException {
        Path markdownFile = Paths.get(path);
        String text = new String(Files.readAllBytes(markdownFile));
        List<Token> tokens = pageTokenizerFactory.createPageTokenizer().tokenize(text);
        List<Element> elements = pageParserFactory.createPageParser().parseTokens(tokens);
        WikiPage page = pageCompilerFactory.createPageCompiler().compilePage(elements);
        return page;
    }
}
