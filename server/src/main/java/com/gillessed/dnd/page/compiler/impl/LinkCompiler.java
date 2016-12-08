package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.ImmutableWikiLink;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.services.page.PageService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LinkCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private final PageService pageService;
    private final String root;

    public LinkCompiler(PageService pageService, String root) {
        this.pageService = pageService;
        this.root = root;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkThreeTextEnd(elements);
        String target = parseAttribute("t", elements.get(0));
        String linkText = elements.get(1).getValue();
        if (target.startsWith("#") || "#".equals(linkText.trim())) {
            LinkData data = getLinkData(target, linkText);
            target = data.target;
            linkText = data.linkText;
        }
        return ImmutableWikiLink.builder()
                .text(linkText)
                .target(target)
                .isBroken(!verifyTargetExists(target))
                .build();
    }

    private boolean verifyTargetExists(String target) {
        String targetPath = target.replace("_", File.separator);
        Path filePath = Paths.get(root).resolve(targetPath);
        return pageService.pathIsValid(filePath);
    }

    private LinkData getLinkData(String target, String linkText) {
        String filename = target.substring(1);
        List<WikiPage> matchingPages = pageService.optional().getPagesByFilename(filename.replace("_", File.separator));
        if (matchingPages.size() != 1) {
            if ("#".equals(linkText.trim())) {
                return new LinkData(target, target);
            } else {
                return new LinkData(target, linkText);
            }
        }
        WikiPage matchingPage = matchingPages.get(0);
        String newTarget = matchingPage.getTarget();
        String newLink = linkText;
        if ("#".equals(newLink.trim())) {
            newLink = matchingPage.getTitle();
        }
        return new LinkData(newTarget, newLink);
    }

    private class LinkData {
        public String target;
        public String linkText;

        public LinkData(String target, String linkText) {
            this.target = target;
            this.linkText = linkText;
        }
    }
}
