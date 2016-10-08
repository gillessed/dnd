package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.ImmutableWikiPage;
import com.gillessed.dnd.rest.model.page.WikiObject;
import com.gillessed.dnd.rest.model.page.WikiPage;
import com.gillessed.dnd.rest.model.page.objects.WikiHeading;
import com.gillessed.dnd.rest.model.page.objects.WikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;
import com.gillessed.dnd.rest.model.page.objects.WikiTitle;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class PageCompilerImpl implements PageCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiParagraph.class,
            WikiTitle.class,
            WikiSection.class,
            WikiHeading.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public PageCompilerImpl(String root) {
        this.objectCompilerFactory = new ObjectCompilerFactory(root);
    }

    @Override
    public WikiPage compilePage(List<Element> elements) throws ParsingException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements);
        List<WikiObject> wikiObjects = PageCompiler.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );

        String title = getTitle(wikiObjects);

        return ImmutableWikiPage.builder()
                .addAllWikiObjects(wikiObjects)
                .title(title)
                .build();
    }

    private String getTitle(List<WikiObject> wikiObjects) {
        List<WikiTitle> titleObjects = wikiObjects.stream()
                .filter((WikiObject object) -> WikiTitle.class.isAssignableFrom(object.getClass()))
                .map((WikiObject object) -> (WikiTitle) object)
                .collect(Collectors.toList());
        Preconditions.checkState(titleObjects.size() == 1, "A page should only have a single title object.");
        return titleObjects.get(0).getText();
    }
}
