package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.ImmutableWikiPage;
import com.gillessed.dnd.rest.model.page.WikiObject;
import com.gillessed.dnd.rest.model.page.WikiPage;
import com.gillessed.dnd.rest.model.page.objects.WikiHeading;
import com.gillessed.dnd.rest.model.page.objects.WikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;
import com.gillessed.dnd.rest.model.page.objects.WikiTitle;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class PageCompilerImpl implements PageCompiler {

    private static final List<Class<? extends WikiObject>> acceptableClasses = Lists.newArrayList(
            WikiParagraph.class,
            WikiTitle.class,
            WikiSection.class,
            WikiHeading.class
    );

    @Override
    public WikiPage compilePage(List<Element> elements) throws ParsingException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements);
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
        return ImmutableWikiPage.builder()
                .addAllWikiObjects(wikiObjects)
                .build();
    }

    public static WikiPage getPage(List<Element> elements) throws ParsingException {
        return new PageCompilerImpl().compilePage(elements);
    }
}
