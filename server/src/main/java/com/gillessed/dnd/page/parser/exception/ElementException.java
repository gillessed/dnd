package com.gillessed.dnd.page.parser.exception;

import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.token.Token;

import java.util.List;

public class ElementException extends ParsingException {
    private final Token token;
    private final List<Token> currentTokens;

    public ElementException(Token token, List<Token> currentTokens, String message) {
        super(String.format("Error while parsing [%s]: %s\n%s", token.toString(), message, currentTokens));
        this.token = token;
        this.currentTokens = currentTokens;
    }

    public Token getToken() {
        return token;
    }
}
