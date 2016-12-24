package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.ImmutableWikiUnorderedList;
import com.gillessed.dnd.model.page.objects.WikiListItem;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.collect.Lists;

import java.util.List;

public class UnorderedListObjectCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiListItem.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public UnorderedListObjectCompiler(ObjectCompilerFactory objectCompilerFactory) {
        this.objectCompilerFactory = objectCompilerFactory;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkAttributesEmpty(elements);
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements.subList(1, elements.size() - 1));
        List<WikiObject> wikiObjects = SubElementParser.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );
        return ImmutableWikiUnorderedList.builder()
                .addAllListItems(wikiObjects)
                .build();
    }
}
