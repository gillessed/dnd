package com.gillessed.dnd.page.parser;

import com.gillessed.dnd.model.page.objects.WikiText;

import java.util.Map;

public class Element {
    private final String elementName;
    private final Map<String, String> attributes;
    private final String value;
    private final Type elementType;
    private final int startLineNumber;
    private final int startColumnNumber;
    private final int endLineNumber;
    private final int endColumnNumber;

    public Element(
            String elementName,
            Map<String, String> attributes,
            String value, Type elementType,
            int startLineNumber,
            int startColumnNumber,
            int endLineNumber,
            int endColumnNumber) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.value = value;
        this.elementType = elementType;
        this.startLineNumber = startLineNumber;
        this.startColumnNumber = startColumnNumber;
        this.endLineNumber = endLineNumber;
        this.endColumnNumber = endColumnNumber;
    }

    public static Element element(
            String elementName,
            Map<String, String> attributes,
            int startLineNumber,
            int startColumnNumber,
            int endLineNumber,
            int endColumnNumber) {
        return new Element(
                elementName,
                attributes,
                null,
                Type.ELEMENT,
                startLineNumber,
                startColumnNumber,
                endLineNumber,
                endColumnNumber);
    }

    public static Element text(
            String value,
            int startLineNumber,
            int startColumnNumber,
            int endLineNumber,
            int endColumnNumber) {
        return new Element(
                WikiText.type,
                null,
                value,
                Type.TEXT,
                startLineNumber,
                startColumnNumber,
                endLineNumber,
                endColumnNumber);
    }

    public static Element end(
            int startLineNumber,
            int startColumnNumber,
            int endLineNumber,
            int endColumnNumber) {
        return new Element(
                null,
                null,
                null,
                Type.END,
                startLineNumber,
                startColumnNumber,
                endLineNumber,
                endColumnNumber);
    }

    public String getElementName() {
        return elementName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getValue() {
        return value;
    }

    public Type getElementType() {
        return elementType;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public int getStartColumnNumber() {
        return startColumnNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    public enum Type {
        ELEMENT,
        TEXT,
        END
    }

    @Override
    public String toString() {
        return "Element{" +
                "elementType=" + elementType +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                ", elementName='" + elementName + '\'' +
                '}';
    }
}
