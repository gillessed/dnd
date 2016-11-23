package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.ImmutableWikiPage;
import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.ImmutableWikiSection;
import com.gillessed.dnd.model.page.objects.ImmutableWikiSectionHeader;
import com.gillessed.dnd.model.page.objects.WikiHeading;
import com.gillessed.dnd.model.page.objects.WikiParagraph;
import com.gillessed.dnd.model.page.objects.WikiSection;
import com.gillessed.dnd.model.page.objects.WikiSectionHeader;
import com.gillessed.dnd.model.page.objects.WikiTitle;
import com.gillessed.dnd.model.page.objects.WikiUnorderedList;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageCompilerImpl implements PageCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiParagraph.class,
            WikiTitle.class,
            WikiSectionHeader.class,
            WikiHeading.class,
            WikiUnorderedList.class
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

        List<WikiTitle> titleObjects = wikiObjects.stream()
                .filter((WikiObject object) -> WikiTitle.class.isAssignableFrom(object.getClass()))
                .map((WikiObject object) -> (WikiTitle) object)
                .collect(Collectors.toList());
        Preconditions.checkState(titleObjects.size() == 1,
                "A page should only have a single title object.");
        Preconditions.checkState(WikiTitle.class.isAssignableFrom(wikiObjects.get(0).getClass()),
                "A page's first element should be a title object.");
        Preconditions.checkState(WikiSectionHeader.class.isAssignableFrom(wikiObjects.get(1).getClass()),
                "A page's second element should be a section header.");

        wikiObjects.add(ImmutableWikiSectionHeader.builder().text("Blank Dummy").build());
        WikiTitle titleObject = (WikiTitle) wikiObjects.get(0);
        String title = titleObject.getText();

        List<WikiSection> wikiSections = new ArrayList<>();
        WikiSectionHeader sectionHeader = null;
        List<WikiObject> sectionObjects = new ArrayList<>();
        for (int i = 1; i < wikiObjects.size(); i++) {
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

        return ImmutableWikiPage.builder()
                .title(title)
                .description(titleObject.getDescription())
                .titleObject(titleObject)
                .wikiSections(wikiSections)
                .build();
    }
}
