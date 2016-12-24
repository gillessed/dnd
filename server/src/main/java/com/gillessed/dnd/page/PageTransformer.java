package com.gillessed.dnd.page;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.gillessed.dnd.page.token.Token;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class PageTransformer {

    private final PageTokenizerFactory pageTokenizerFactory;
    private final PageParserFactory pageParserFactory;
    private final PageCompilerFactory pageCompilerFactory;

    @Inject
    public PageTransformer(
            PageTokenizerFactory pageTokenizerFactory,
            PageParserFactory pageParserFactory,
            PageCompilerFactory pageCompilerFactory) {
        this.pageTokenizerFactory = pageTokenizerFactory;
        this.pageParserFactory = pageParserFactory;
        this.pageCompilerFactory = pageCompilerFactory;
    }

    public WikiPage transformPage(String source, Target target, List<WikiLink> linkList) throws ParsingException {
        List<Token> tokens = pageTokenizerFactory.createPageTokenizer().tokenize(source);
        List<Element> elements = pageParserFactory.createPageParser().parseTokens(tokens);
        return pageCompilerFactory.createPageCompiler(linkList).compilePage(elements, target, source);
    }
}
