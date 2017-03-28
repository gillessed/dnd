package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.objects.WikiIndex;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public class IndexCompiler extends AbstractObjectCompiler implements ObjectCompiler {

    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        String title = null;
        try {
            title = parseAttribute("title", elements.get(0));
        } catch (CompilerException e) {}

        return new WikiIndex.Builder()
                .title(title != null ? title : "Index")
                .build();
    }
}
