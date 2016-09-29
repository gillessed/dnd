package com.gillessed.dnd.page.token;

import org.immutables.value.Value;

@Value.Immutable
public interface Token {
    TokenType getTokenType();
    String getSource();
    String getValue();
    int getStartLineNumber();
    int getEndLineNumber();
    int getStartColumn();
    int getEndColumn();
}
