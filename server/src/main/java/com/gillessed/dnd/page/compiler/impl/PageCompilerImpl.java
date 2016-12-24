package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiContent;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.model.page.objects.WikiMeta;
import com.gillessed.dnd.model.page.objects.WikiTitle;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.PageCompiler;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

public class PageCompilerImpl implements PageCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiMeta.class,
            WikiTitle.class,
            WikiContent.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public PageCompilerImpl(List<WikiLink> linkList) {
        this.objectCompilerFactory = new ObjectCompilerFactory(linkList);
    }

    @Override
    public WikiPage compilePage(List<Element> elements, Target target, String source) throws ParsingException {
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements);
        List<WikiObject> wikiObjects = SubElementParser.parseSubElements(
                elements,
                subElements,
                ACCEPTABLE_CLASSES,
                objectCompilerFactory
        );

        Preconditions.checkState(wikiObjects.size() == 3 || wikiObjects.size() == 4,
                "A page should have exactly 3 or 4 elements.");

        Preconditions.checkState(WikiMeta.class.isAssignableFrom(wikiObjects.get(0).getClass()),
                "A page's first element should be a meta element.");

        Preconditions.checkState(WikiTitle.class.isAssignableFrom(wikiObjects.get(1).getClass()),
                "A page's second element should be a title element.");

        Preconditions.checkState(WikiContent.class.isAssignableFrom(wikiObjects.get(2).getClass()),
                "A page's third element should be a content element.");
        if (wikiObjects.size() == 4) {
            Preconditions.checkState(WikiContent.class.isAssignableFrom(wikiObjects.get(3).getClass()),
                    "A page's fourth element should be a meta element.");
            WikiContent first = (WikiContent) wikiObjects.get(2);
            WikiContent second = (WikiContent) wikiObjects.get(3);
            Preconditions.checkState(first.getContentType() != second.getContentType(),
                    "A page should have no more than one content and one dm part.");
        }

        WikiMeta metadata = (WikiMeta) wikiObjects.get(0);
        WikiTitle titleObject = (WikiTitle) wikiObjects.get(1);
        WikiContent content = null;
        WikiContent dm = null;
        WikiContent first = (WikiContent) wikiObjects.get(2);
        if (first.getContentType() == WikiContent.ContentType.PAGE) {
            content = first;
        } else {
            dm = first;
        }
        if (wikiObjects.size() == 4) {
            WikiContent second = (WikiContent) wikiObjects.get(3);
            if (second.getContentType() == WikiContent.ContentType.PAGE) {
                content = second;
            } else {
                dm = second;
            }
        }

        return new WikiPage.Builder()
                .metadata(metadata)
                .title(titleObject.getText())
                .pageContent(content)
                .dmContent(dm)
                .target(target)
                .pageSource(source)
                .build();
    }
}
