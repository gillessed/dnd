package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.WikiPage;

import java.util.ArrayList;
import java.util.List;

public interface PageCompiler {
    WikiPage compilePage(List<Element> elements) throws ParsingException;

    static List<WikiObject> parseSubElements(
            List<Element> elements,
            List<List<Element>> subElements,
            List<Class<? extends WikiObject>> acceptableClasses,
            ObjectCompilerFactory objectCompilerFactory) throws CompilerException {
        List<WikiObject> wikiObjects = new ArrayList<>();
        for (List<Element> subElement : subElements) {
            if (subElement.size() == 0) {
                throw new IllegalStateException("Cannot have empty sub element");
            }
            ObjectCompiler compiler = objectCompilerFactory.getObjectCompilerForObjectType(subElement.get(0));
            wikiObjects.add(compiler.compileObject(subElement));
        }
        for (WikiObject object : wikiObjects) {
            boolean acceptable = false;
            for (Class<? extends WikiObject> clazz : acceptableClasses) {
                if (clazz.isAssignableFrom(object.getClass())) {
                    acceptable = true;
                    break;
                }
            }
            if (!acceptable) {
                throw new CompilerException(elements.get(0), "cannot have sub element of type " + object.getClass());
            }
        }
        return wikiObjects;
    }
}
