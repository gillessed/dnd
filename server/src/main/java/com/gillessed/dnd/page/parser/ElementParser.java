package com.gillessed.dnd.page.parser;

import com.gillessed.dnd.page.parser.exception.ElementException;
import com.gillessed.dnd.page.token.Token;

import java.util.List;

public interface ElementParser {
    List<Element> parseTokens(List<Token> tokens) throws ElementException;
}
