package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.compiler.impl.HeadingCompiler;
import com.gillessed.dnd.page.compiler.impl.LinkCompiler;
import com.gillessed.dnd.page.compiler.impl.ParagraphCompiler;
import com.gillessed.dnd.page.compiler.impl.SectionCompiler;
import com.gillessed.dnd.page.compiler.impl.TextCompiler;
import com.gillessed.dnd.page.compiler.impl.TitleCompiler;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.objects.WikiHeading;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;
import com.gillessed.dnd.rest.model.page.objects.WikiText;
import com.gillessed.dnd.rest.model.page.objects.WikiTitle;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ObjectCompilerFactory {
    private static final String PARAGRAPH_SHORTHAND = "p";
    private static final String LINK_SHORTHAND = "a";

    private final Map<String, ObjectCompiler> compilers;

    public ObjectCompilerFactory(String root) {
        this.compilers = ImmutableMap.<String, ObjectCompiler>builder()
                .put(WikiText.type, new TextCompiler())
                .put(WikiTitle.type, new TitleCompiler())
                .put(WikiSection.type, new SectionCompiler())
                .put(WikiHeading.type, new HeadingCompiler())
                .put(PARAGRAPH_SHORTHAND, new ParagraphCompiler(this))
                .put(LINK_SHORTHAND, new LinkCompiler(root))
                .build();
    }

    public ObjectCompiler getObjectCompilerForObjectType(Element element) throws CompilerException {
        if (compilers.containsKey(element.getElementName())) {
            return compilers.get(element.getElementName());
        }
        throw new CompilerException(element, "No compiler for object " + element.getElementName());
    }
}
