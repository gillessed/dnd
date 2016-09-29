package com.gillessed.dnd.page.token.exception;

public class IllegalCharacterException extends TokenizerException {
    public IllegalCharacterException(int lineNumber, int column, String tokenString, Character offendingCharater) {
        super(lineNumber, column, tokenString, String.format("Illegal character: '%c'", offendingCharater));
    }
}
