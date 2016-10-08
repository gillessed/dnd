package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.WikiObject;
import com.gillessed.dnd.rest.model.page.objects.ImmutableWikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiLink;
import com.gillessed.dnd.rest.model.page.objects.WikiText;
import com.google.common.collect.Lists;

import java.util.List;

public class ParagraphCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiText.class,
            WikiLink.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public ParagraphCompiler(ObjectCompilerFactory objectCompilerFactory) {
        this.objectCompilerFactory = objectCompilerFactory;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkAttributesEmpty(elements);
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements.subList(1, elements.size() - 1));
        List<WikiObject> wikiObjects = PageCompiler.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );
        return ImmutableWikiParagraph.builder()
                .addAllWikiObjects(wikiObjects)
                .build();
    }
}
