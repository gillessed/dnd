package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.WikiPage;

import java.util.List;

public interface PageCompiler {
    public WikiPage compilePage(List<Element> elements) throws ParsingException;
}
