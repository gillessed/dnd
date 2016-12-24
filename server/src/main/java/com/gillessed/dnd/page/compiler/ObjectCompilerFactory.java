package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.model.page.objects.WikiContent;
import com.gillessed.dnd.model.page.objects.WikiHeading;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.model.page.objects.WikiMeta;
import com.gillessed.dnd.model.page.objects.WikiSection;
import com.gillessed.dnd.model.page.objects.WikiText;
import com.gillessed.dnd.model.page.objects.WikiTitle;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.compiler.impl.ContentCompiler;
import com.gillessed.dnd.page.compiler.impl.HeadingCompiler;
import com.gillessed.dnd.page.compiler.impl.LinkCompiler;
import com.gillessed.dnd.page.compiler.impl.ListItemObjectCompiler;
import com.gillessed.dnd.page.compiler.impl.MetaCompiler;
import com.gillessed.dnd.page.compiler.impl.ParagraphCompiler;
import com.gillessed.dnd.page.compiler.impl.SectionCompiler;
import com.gillessed.dnd.page.compiler.impl.TextCompiler;
import com.gillessed.dnd.page.compiler.impl.TitleCompiler;
import com.gillessed.dnd.page.compiler.impl.UnorderedListObjectCompiler;
import com.gillessed.dnd.page.parser.Element;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class ObjectCompilerFactory {
    private static final String PARAGRAPH_SHORTHAND = "p";
    private static final String LINK_SHORTHAND = "a";
    private static final String UNORDERED_LIST_SHORTHAND = "ul";
    private static final String LIST_ITEM_SHORTHAND = "li";
    private static final String DM_CONTENT_SHORTHAND = "dm";


    private final Map<String, ObjectCompiler> compilers;

    public ObjectCompilerFactory(List<WikiLink> linkList) {
        this.compilers = ImmutableMap.<String, ObjectCompiler>builder()
                .put(WikiText.type, new TextCompiler())
                .put(WikiTitle.type, new TitleCompiler())
                .put(WikiSection.type, new SectionCompiler())
                .put(WikiHeading.type, new HeadingCompiler())
                .put(PARAGRAPH_SHORTHAND, new ParagraphCompiler(this))
                .put(LINK_SHORTHAND, new LinkCompiler(linkList))
                .put(UNORDERED_LIST_SHORTHAND, new UnorderedListObjectCompiler(this))
                .put(LIST_ITEM_SHORTHAND, new ListItemObjectCompiler(this))
                .put(WikiContent.type, new ContentCompiler(this))
                .put(DM_CONTENT_SHORTHAND, new ContentCompiler(this))
                .put(WikiMeta.type, new MetaCompiler())
                .build();
    }

    public ObjectCompiler getObjectCompilerForObjectType(Element element) throws CompilerException {
        if (compilers.containsKey(element.getElementName())) {
            return compilers.get(element.getElementName());
        }
        throw new CompilerException(element, "No compiler for object " + element.getElementName());
    }
}
