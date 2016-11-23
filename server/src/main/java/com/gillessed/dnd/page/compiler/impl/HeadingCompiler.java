package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.ImmutableWikiHeading;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public class HeadingCompiler extends AbstractObjectCompiler implements ObjectCompiler {
    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkThreeTextEnd(elements);
        int level = parseIntAttribute("level", elements.get(0));
        return ImmutableWikiHeading.builder()
                .text(elements.get(1).getValue().trim())
                .level(level)
                .build();
    }
}
