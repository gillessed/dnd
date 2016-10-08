package com.gillessed.dnd.page.parser.impl;

import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.ElementParser;
import com.gillessed.dnd.page.parser.exception.ElementException;
import com.gillessed.dnd.page.token.Token;
import com.gillessed.dnd.page.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElementParserImpl implements ElementParser {

    private enum State {
        START,
        ELEMENT_START,
        ELEMENT_ATTRIBUTE_START,
        ELEMENT_ATTRIBUTE,
        ELEMENT_ATTRIBUTE_EQUALS,
        ELEMENT_ATTRIBUTE_STRING
    }

    private int tokenIndex;
    private List<Token> elementTokens;
    private Element.Type elementType;
    private State state;

    public ElementParserImpl() {
        tokenIndex = 0;
        elementTokens = new ArrayList<>();
        elementType = null;
        state = State.START;
    }

    @Override
    public List<Element> parseTokens(List<Token> tokens) throws ElementException {
        List<Element> elements = new ArrayList<>();
        while (tokenIndex < tokens.size()) {
            Token current = tokens.get(tokenIndex);
            elementTokens.add(current);
            tokenIndex++;
            handleToken(current);
            if (elementType != null) {
                elements.add(buildElement());
                elementType = null;
                elementTokens.clear();
                state = State.START;
            }
        }
        return elements;
    }

    private void handleToken(Token current) throws ElementException {
        switch (state) {
            case START:
                if (current.getTokenType() == TokenType.TEXT) {
                    elementType = Element.Type.TEXT;
                } else if (current.getTokenType() == TokenType.LEFT_CARET) {
                    state = State.ELEMENT_START;
                } else {
                    throw new ElementException(current, elementTokens, "excepted text or <.");
                }
                break;
            case ELEMENT_START:
                if (current.getTokenType() == TokenType.WORD) {
                    state = State.ELEMENT_ATTRIBUTE;
                } else if (current.getTokenType() == TokenType.RIGHT_CARET) {
                    elementType = Element.Type.END;
                } else {
                    throw new ElementException(current, elementTokens, "expected word or >");
                }
                break;
            case ELEMENT_ATTRIBUTE_START:
                if (current.getTokenType() == TokenType.WORD) {
                    state = State.ELEMENT_ATTRIBUTE;
                } else if (current.getTokenType() == TokenType.RIGHT_CARET) {
                    elementType = Element.Type.ELEMENT;
                } else {
                    throw new ElementException(current, elementTokens, "expected word or >");
                }
                break;
            case ELEMENT_ATTRIBUTE:
                if (current.getTokenType() == TokenType.WORD) {
                    //No state change.
                } else if (current.getTokenType() == TokenType.EQUALS) {
                    state = State.ELEMENT_ATTRIBUTE_EQUALS;
                } else if (current.getTokenType() == TokenType.RIGHT_CARET) {
                    elementType = Element.Type.ELEMENT;
                } else {
                    throw new ElementException(current, elementTokens, "expected word or = or >");
                }
                break;
            case ELEMENT_ATTRIBUTE_EQUALS:
                if (current.getTokenType() == TokenType.STRING) {
                    state = State.ELEMENT_ATTRIBUTE_START;
                } else {
                    throw new ElementException(current, elementTokens, "expected string");
                }
                break;
            default:
                throw new IllegalStateException("Got into illegal state: " + state);
        }
    }

    private Element buildElement() throws ElementException {
        switch (elementType) {
            case END:
                if (elementTokens.size() != 2) {
                    throw new IllegalStateException("End element must have exactly 2 tokens, but has " + elementTokens.size());
                }
                return Element.end(
                        elementTokens.get(0).getStartLineNumber(),
                        elementTokens.get(0).getStartColumn(),
                        elementTokens.get(1).getEndLineNumber(),
                        elementTokens.get(1).getEndColumn());
            case TEXT:
                if (elementTokens.size() != 1) {
                    throw new IllegalStateException("Text element must have exactly 1 token, but has " + elementTokens.size());
                }
                return Element.text(
                        elementTokens.get(0).getValue(),
                        elementTokens.get(0).getStartLineNumber(),
                        elementTokens.get(0).getStartColumn(),
                        elementTokens.get(0).getEndLineNumber(),
                        elementTokens.get(0).getEndColumn());
            case ELEMENT:
                return buildElementElement();
            default:
                throw new IllegalStateException("Got into an illegal element type: " + elementType);
        }
    }

    private Element buildElementElement() throws ElementException {
        if (elementTokens.size() < 3) {
            throw new IllegalStateException("An element should never have fewer than three tokens but has: " + elementTokens);
        }
        if (elementTokens.get(0).getTokenType() != TokenType.LEFT_CARET) {
            throw new ElementException(elementTokens.get(0), elementTokens, "element must begin with <");
        }
        if (elementTokens.get(1).getTokenType() != TokenType.WORD) {
            throw new ElementException(elementTokens.get(1), elementTokens, "element must have a word token to describe it but got: " + elementTokens.get(1));
        }
        String name = elementTokens.get(1).getValue();
        Map<String, String> attributeMap = new HashMap<>();
        int attributeIndex = 2;
        while (attributeIndex < elementTokens.size()) {
            Token current = elementTokens.get(attributeIndex);
            Optional<Token> lookahead1 = attributeIndex + 1 < elementTokens.size()
                    ? Optional.of(elementTokens.get(attributeIndex + 1))
                    : Optional.empty();
            Optional<Token> lookahead2 = attributeIndex + 2 < elementTokens.size()
                    ? Optional.of(elementTokens.get(attributeIndex + 2))
                    : Optional.empty();
            attributeIndex++;
            if (current.getTokenType() == TokenType.WORD) {
                if (lookahead1.isPresent()) {
                    if (lookahead1.get().getTokenType() == TokenType.EQUALS) {
                        if (lookahead2.isPresent()) {
                            if (lookahead2.get().getTokenType() == TokenType.STRING) {
                                attributeMap.put(current.getValue().trim(), lookahead2.get().getValue());
                                attributeIndex += 2;
                            } else {
                                throw new ElementException(lookahead2.get(), elementTokens, "excepting a string");
                            }
                        } else {
                            throw new ElementException(lookahead1.get(), elementTokens, "unexpected end of tokens");
                        }
                    } else if (lookahead1.get().getTokenType() == TokenType.RIGHT_CARET
                            || lookahead1.get().getTokenType() == TokenType.WORD) {
                        attributeMap.put(current.getValue().trim(), "true");
                    } else {
                        throw new ElementException(lookahead1.get(), elementTokens, "excepting an = or >");
                    }
                } else {
                    throw new ElementException(current, elementTokens, "unexpected end of tokens");
                }
            } else if (current.getTokenType() == TokenType.RIGHT_CARET) {
                break;
            } else {
                throw new ElementException(current, elementTokens, "expecting word or >");
            }
        }
        return Element.element(
                name,
                attributeMap,
                elementTokens.get(0).getStartLineNumber(),
                elementTokens.get(0).getStartColumn(),
                elementTokens.get(elementTokens.size() - 1).getEndLineNumber(),
                elementTokens.get(elementTokens.size() - 1).getEndColumn());
    }
}
