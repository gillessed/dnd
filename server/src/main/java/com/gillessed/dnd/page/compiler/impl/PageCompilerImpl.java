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
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.services.page.PageService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    private final Path pagesDir;

    public PageCompilerImpl(PageService pageService, String root) {
        this.objectCompilerFactory = new ObjectCompilerFactory(pageService, root);
        pagesDir = Paths.get(root).toAbsolutePath().normalize();
    }

    @Override
    public WikiPage compilePage(List<Element> elements, Path path) throws ParsingException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements);
        List<WikiObject> wikiObjects = PageCompiler.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );

        WikiTitle titleObject = getTitleObjects(wikiObjects);
        String title = titleObject.getText();

        // Placeholder section to make sure the loop exits correctly.
        wikiObjects.add(ImmutableWikiSectionHeader.builder().text("Blank Dummy").build());

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
                .target(getTarget(path))
                .build();
    }

    @Override
    public WikiPage compileTitle(List<Element> elements, Path path) throws ParsingException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements);
        ObjectCompiler compiler = objectCompilerFactory.getObjectCompilerForObjectType(subElements.get(0).get(0));
        WikiTitle titleObject = (WikiTitle) compiler.compileObject(subElements.get(0));
        String title = titleObject.getText();

        return ImmutableWikiPage.builder()
                .title(title)
                .description(titleObject.getDescription())
                .titleObject(titleObject)
                .wikiSections(Collections.emptyList())
                .target(getTarget(path))
                .build();
    }

    private WikiTitle getTitleObjects(List<WikiObject> wikiObjects) {
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
        return titleObjects.get(0);
    }

    private String getTarget(Path path) {
        return pagesDir.relativize(path.toAbsolutePath().normalize()).toString().replace(File.separator, "_");
    }
}
