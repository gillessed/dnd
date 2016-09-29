package com.gillessed.dnd.page.token.exception;

import com.gillessed.dnd.page.exception.ParsingException;

public abstract class TokenizerException extends ParsingException {
    protected final int lineNumber;
    protected final int column;
    protected final String tokenString;

    public TokenizerException(int lineNumber, int column, String tokenString, String message) {
        super(String.format("%s {lineNumber: %d, column: %d, tokenString: %s}",
                message,
                lineNumber,
                column,
                tokenString));
        this.lineNumber = lineNumber;
        this.column = column;
        this.tokenString = tokenString;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    public String getTokenString() {
        return tokenString;
    }
}
