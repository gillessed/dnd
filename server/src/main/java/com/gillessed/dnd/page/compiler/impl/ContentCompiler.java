package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.ImmutableWikiSection;
import com.gillessed.dnd.model.page.objects.ImmutableWikiSectionHeader;
import com.gillessed.dnd.model.page.objects.WikiContent;
import com.gillessed.dnd.model.page.objects.WikiHeading;
import com.gillessed.dnd.model.page.objects.WikiIndex;
import com.gillessed.dnd.model.page.objects.WikiParagraph;
import com.gillessed.dnd.model.page.objects.WikiSection;
import com.gillessed.dnd.model.page.objects.WikiSectionHeader;
import com.gillessed.dnd.model.page.objects.WikiUnorderedList;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ContentCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiParagraph.class,
            WikiSectionHeader.class,
            WikiHeading.class,
            WikiUnorderedList.class,
            WikiIndex.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public ContentCompiler(ObjectCompilerFactory objectCompilerFactory) {
        this.objectCompilerFactory = objectCompilerFactory;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements.subList(1, elements.size() - 1));
        List<WikiObject> wikiObjects = SubElementParser.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );

        // Silly placeholder section to make sure the loop exits correctly.
        wikiObjects.add(ImmutableWikiSectionHeader.builder().text("Blank Dummy").build());

        List<WikiSection> wikiSections = new ArrayList<>();
        WikiSectionHeader sectionHeader = null;
        List<WikiObject> sectionObjects = new ArrayList<>();
        for (int i = 0; i < wikiObjects.size(); i++) {
            WikiObject object = wikiObjects.get(i);
            if (WikiSectionHeader.class.isAssignableFrom(object.getClass())) {
                if (sectionHeader != null) {
                    WikiSection section = ImmutableWikiSection.builder()
                            .text(sectionHeader.getText())
                            .wikiObjects(sectionObjects)
                            .index(wikiSections.size() + 1)
                            .build();
                    wikiSections.add(section);
                    sectionObjects.clear();
                }
                sectionHeader = (WikiSectionHeader) object;
            } else {
                sectionObjects.add(object);
            }
        }

        return new WikiContent.Builder()
                .content(wikiSections)
                .contentType(WikiContent.ContentType.fromString(elements.get(0).getElementName()))
                .build();
    }
}