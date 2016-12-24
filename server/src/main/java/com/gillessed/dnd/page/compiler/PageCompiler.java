package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public interface PageCompiler {
    WikiPage compilePage(List<Element> elements, Target target, String source) throws ParsingException;
}
