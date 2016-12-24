package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public class LinkCompiler extends AbstractObjectCompiler implements ObjectCompiler {
    private final List<WikiLink> linkList;

    public LinkCompiler(List<WikiLink> linkList) {
        this.linkList = linkList;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkThreeTextEnd(elements);
        String target = parseAttribute("t", elements.get(0));
        String linkText = elements.get(1).getValue();
        WikiLink link = new WikiLink(linkText, target, null);
        linkList.add(link);
        return link;
    }
}
