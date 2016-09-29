package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.WikiObject;
import com.gillessed.dnd.rest.model.page.objects.ImmutableWikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiText;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ParagraphCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private static final List<Class<? extends WikiObject>> acceptableClasses = Lists.newArrayList(
            WikiText.class
    );

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkAttributesEmpty(elements);
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements.subList(1, elements.size() - 1));
        List<WikiObject> wikiObjects = new ArrayList<>();
        for (List<Element> subElement : subElements) {
            if (subElement.size() == 0) {
                throw new IllegalStateException("Cannot have empty sub element");
            }
            ObjectCompiler compiler = ObjectCompilerFactory.getObjectCompilerForObjectType(subElement.get(0));
            wikiObjects.add(compiler.compileObject(subElement));
        }
        for (WikiObject object : wikiObjects) {
            boolean acceptable = false;
            for (Class<? extends WikiObject> clazz : acceptableClasses) {
                if (clazz.isAssignableFrom(object.getClass())) {
                    acceptable = true;
                    break;
                }
            }
            if (!acceptable) {
                throw new CompilerException(elements.get(0), "cannot have sub element of type " + object.getClass());
            }
        }
        return ImmutableWikiParagraph.builder()
                .addAllWikiObjects(wikiObjects)
                .build();
    }
}
