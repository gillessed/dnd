package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.ArrayList;
import java.util.List;

public class SubElementFinder {

    private int index;
    private int openCount;
    private List<Element> elements;
    private List<Element> currentElements;
    private boolean cut;

    private SubElementFinder(List<Element> elements) {
        index = 0;
        openCount = 0;
        this.elements = new ArrayList<>(elements);
        currentElements = new ArrayList<>();
        cut = false;
    }

    public static List<List<Element>> findSubElements(List<Element> elements) throws CompilerException {
        return new SubElementFinder(elements).findSubElementsInternal();
    }

    private List<List<Element>> findSubElementsInternal() throws CompilerException {
        List<List<Element>> subElements = new ArrayList<>();
        while (index < elements.size()) {
            Element current = elements.get(index);
            currentElements.add(current);
            index++;
            switch (current.getElementType()) {
                case ELEMENT:
                    openCount++;
                    break;
                case TEXT:
                    if (openCount == 0) {
                        cut = true;
                    }
                    break;
                case END:
                    openCount--;
                    if (openCount == 0) {
                        cut = true;
                    } else if (openCount < 0) {
                        throw new CompilerException(current, "could not find matching start element.");
                    }
                    break;
            }
            if (cut) {
                subElements.add(currentElements);
                currentElements = new ArrayList<>();
                cut = false;
            }
        }
        if (currentElements.size() != 0) {
            throw new CompilerException(elements.get(0), "could not find matching end element.");
        }
        return subElements;
    }
}
