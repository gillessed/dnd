package com.gillessed.dnd.page.compiler.exception;

import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;

public class CompilerException extends ParsingException {
    private final Element element;

    public CompilerException(Element element, String message) {
        super(String.format("Error compiling element [%d:%d]: %s",
                element.getStartLineNumber(),
                element.getStartColumnNumber(),
                message));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
