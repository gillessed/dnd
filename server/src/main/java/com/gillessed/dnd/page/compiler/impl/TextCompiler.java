package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.ImmutableWikiText;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public class TextCompiler implements ObjectCompiler {
    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        if (elements.size() != 1) {
            throw new IllegalStateException("Should have exactly one element in a text object.");
        }
        return ImmutableWikiText.builder()
                .value(elements.get(0).getValue())
                .build();
    }
}
