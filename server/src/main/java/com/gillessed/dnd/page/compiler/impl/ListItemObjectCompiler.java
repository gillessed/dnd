package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.ImmutableWikiListItem;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.model.page.objects.WikiText;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompilerFactory;
import com.gillessed.dnd.page.compiler.SubElementFinder;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.collect.Lists;

import java.util.List;

public class ListItemObjectCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private static final List<Class<? extends WikiObject>> ACCEPTABLE_CLASSES = Lists.newArrayList(
            WikiText.class,
            WikiLink.class
    );

    private final ObjectCompilerFactory objectCompilerFactory;

    public ListItemObjectCompiler(ObjectCompilerFactory objectCompilerFactory) {
        this.objectCompilerFactory = objectCompilerFactory;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        List<WikiObject> wikiObjects = parseAllSubElements(elements, ACCEPTABLE_CLASSES, objectCompilerFactory);
        return ImmutableWikiListItem.builder()
                .addAllWikiObjects(wikiObjects)
                .build();
    }
}
