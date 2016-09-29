package com.gillessed.dnd.page.token.exception;

public class IllegalEscapeException extends TokenizerException {
    public IllegalEscapeException(int lineNumber, int column, String tokenString, Character offendingCharater) {
        super(lineNumber, column, tokenString, String.format("Illegal escape character: '%c'", offendingCharater));
    }
}
