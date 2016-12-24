package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.model.page.objects.WikiLink;

import java.util.List;

public interface PageCompilerFactory {
    PageCompiler createPageCompiler(List<WikiLink> linkList);
}
