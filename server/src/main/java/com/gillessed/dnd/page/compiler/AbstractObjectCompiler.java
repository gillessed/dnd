package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.compiler.impl.SubElementParser;
import com.gillessed.dnd.page.compiler.impl.SubElementFinder;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.model.page.objects.WikiText;

import java.util.List;

public abstract class AbstractObjectCompiler implements ObjectCompiler {
    protected void checkElementsNotEmpty(List<Element> elements) {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Internal compiler error: elements cannot be empty.");
        }
    }

    protected void checkElementsEmpty(List<Element> elements) {
        if (!elements.isEmpty()) {
            throw new IllegalStateException("Internal compiler error: elements must be empty.");
        }
    }

    /**
     * This is for elements that follow the {element} text {} pattern.
     * For example, the title or section elements.
     */
    protected void checkThreeTextEnd(List<Element> elements) throws CompilerException {
        if (elements.size() != 3) {
            throw new CompilerException(elements.get(0), "Title object should have exactly 3 elements, but has "
                    + elements.size());
        }
        if (!WikiText.type.equals(elements.get(1).getElementName())) {
            throw new CompilerException(elements.get(1), "Internal title element should be text, but is instead "
                    + elements.get(1).getElementName());
        }
        if (elements.get(2).getElementType() != Element.Type.END) {
            throw new CompilerException(elements.get(2), "Internal title element should be end, but is instead "
                    + elements.get(2).getElementType() + "-" + elements.get(2).getElementName());
        }
    }

    protected void checkAttributesEmpty(List<Element> elements) throws CompilerException {
        if (!elements.get(0).getAttributes().isEmpty()) {
            throw new CompilerException(elements.get(0), "Title element has no attributes, but was given "
                    + elements.get(0).getAttributes());
        }
    }

    private String isAttributePresent(String attribute, Element element) throws CompilerException {
        if (element.getAttributes() == null || !element.getAttributes().containsKey(attribute)) {
            throw new CompilerException(element, "element did not contain attribute " + attribute);
        }
        return element.getAttributes().get(attribute);
    }

    protected String parseAttribute(String attribute, Element element) throws CompilerException {
        return isAttributePresent(attribute, element);
    }

    protected int parseIntAttribute(String attribute, Element element) throws CompilerException {
        String value = isAttributePresent(attribute, element);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CompilerException(element, "error parse int attribute " + value);
        }
    }

    protected List<List<Element>> getSubElements(List<Element> elements) throws CompilerException{
        return SubElementFinder.findSubElements(elements);
    }

    protected List<WikiObject> parseAllSubElements(
            List<Element> elements,
            List<Class<? extends WikiObject>> acceptableClasses,
            ObjectCompilerFactory objectCompilerFactory) throws CompilerException {
        checkElementsNotEmpty(elements);
        checkAttributesEmpty(elements);
        List<List<Element>> subElements = SubElementFinder.findSubElements(elements.subList(1, elements.size() - 1));
        return SubElementParser.parseSubElements(
                elements,
                subElements,
                acceptableClasses,
                objectCompilerFactory
        );
    }
}
