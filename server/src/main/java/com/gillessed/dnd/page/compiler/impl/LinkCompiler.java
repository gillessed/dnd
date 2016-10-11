package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.WikiObject;
import com.gillessed.dnd.rest.model.page.objects.ImmutableWikiLink;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LinkCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    private final String root;

    public LinkCompiler(String root) {
        this.root = root;
    }

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkThreeTextEnd(elements);
        String target = parseAttribute("t", elements.get(0));
        return ImmutableWikiLink.builder()
                .text(elements.get(1).getValue())
                .target(target)
                .isBroken(!verifyTargetExists(target))
                .build();
    }

    private boolean verifyTargetExists(String target) {
        String targetPath = target.replace("_", File.separator);
        Path filePath = Paths.get(root).resolve(targetPath);
        return Files.exists(filePath);
    }
}
