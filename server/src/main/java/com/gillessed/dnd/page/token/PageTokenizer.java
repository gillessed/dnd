package com.gillessed.dnd.page.token;

import com.gillessed.dnd.page.token.exception.TokenizerException;

import java.util.List;

public interface PageTokenizer {
    List<Token> tokenize(String input) throws TokenizerException;
}
