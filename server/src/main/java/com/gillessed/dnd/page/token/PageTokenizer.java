package com.gillessed.dnd.page.token;

import com.gillessed.dnd.page.token.exception.TokenizerException;

import java.util.List;

/**
 * Created by gcole on 9/27/16.
 */
public interface PageTokenizer {
    List<Token> tokenize(String input) throws TokenizerException;
}
