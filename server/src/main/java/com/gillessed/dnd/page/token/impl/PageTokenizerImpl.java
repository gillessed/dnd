package com.gillessed.dnd.page.token.impl;

import com.gillessed.dnd.page.token.PageTokenizer;
import com.gillessed.dnd.page.token.exception.IllegalCharacterException;
import com.gillessed.dnd.page.token.ImmutableToken;
import com.gillessed.dnd.page.token.Token;
import com.gillessed.dnd.page.token.TokenType;
import com.gillessed.dnd.page.token.exception.IllegalEscapeException;
import com.gillessed.dnd.page.token.exception.TokenizerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PageTokenizerImpl implements PageTokenizer {

    private enum State {
        TEXT_TEXT,
        TEXT_ESCAPE,
        MARKDOWN_START,
        MARKDOWN_WORD,
        STRING_TEXT,
        STRING_ESCAPE
    }

    private static enum Context {
        TEXT,
        MARKDOWN,
        STRING
    }

    private int tokenStartLineNumber;
    private int tokenStartColumnNumber;
    private int tokenEndLineNumber;
    private int tokenEndColumnNumber;
    private int tokenStartIndex;
    private int tokenEndIndex;
    private State state;
    private Context context;
    private StringBuilder tokenText;
    private TokenType tokenType;
    private List<Token> tokens;

    private PageTokenizerImpl() {
        tokenStartLineNumber = 1;
        tokenStartColumnNumber = 1;
        tokenEndLineNumber = 1;
        tokenEndColumnNumber = 1;
        tokenStartIndex = 0;
        tokenEndIndex = 0;
        state = State.TEXT_TEXT;
        context = Context.TEXT;
        tokenText = new StringBuilder();
        tokenType = null;
        tokens = new ArrayList<>();
    }

    @Override
    public List<Token> tokenize(String input) throws TokenizerException {
        while (tokenEndIndex < input.length()) {
            Character current = input.charAt(tokenEndIndex);
            Optional<Character> lookahead = getLookahead(input);
            switch (context) {
                case TEXT:
                    handleTextState(current, lookahead);
                    break;
                case MARKDOWN:
                    handleMarkdownState(current, lookahead);
                    break;
                case STRING:
                    handleStringState(current, lookahead);
                    break;
                default:
                    throw new IllegalStateException("Internal parser error. Got into some weird context: " + context);
            }
            tokenEndIndex++;
            if (current.equals('\n')) {
                tokenEndLineNumber++;
            } else {
                tokenEndColumnNumber++;
            }
            if (tokenType != null && tokenText.length() > 0) {
                String tokenString = tokenText.toString();
                boolean isWhitespace = true;
                for (int i = 0; i < tokenString.length(); i++) {
                    if (!Character.isWhitespace(tokenString.charAt(i))) {
                        isWhitespace = false;
                        break;
                    }
                }
                if (!isWhitespace) {
                    Token token = ImmutableToken.builder()
                            .startLineNumber(tokenStartLineNumber)
                            .endLineNumber(tokenEndLineNumber)
                            .startColumn(tokenStartColumnNumber)
                            .endColumn(tokenEndColumnNumber)
                            .source(input.substring(tokenStartIndex, tokenEndIndex))
                            .value(tokenString)
                            .tokenType(tokenType)
                            .build();
                    tokens.add(token);
                }
                tokenType = null;
                tokenStartIndex = tokenEndIndex;
                tokenStartLineNumber = tokenEndLineNumber;
                tokenStartColumnNumber = tokenEndColumnNumber;
                tokenText = new StringBuilder();
                switch (context) {
                    case TEXT: state = State.TEXT_TEXT; break;
                    case MARKDOWN: state = State.MARKDOWN_START; break;
                    case STRING: state = State.STRING_TEXT; break;
                    default:
                        throw new IllegalStateException("Internal parser error. Got into some weird context: " + context);
                }
            }
        }
        return tokens;
    }

    private Optional<Character> getLookahead(String input) {
        if (tokenEndIndex + 1 < input.length()) {
            return Optional.of(input.charAt(tokenEndIndex + 1));
        } else {
            return Optional.empty();
        }
    }

    private final void handleTextState(Character current, Optional<Character> lookahead) throws TokenizerException {
        switch (state) {
            case TEXT_TEXT:
                if (current.equals('\\')) {
                    state = State.TEXT_ESCAPE;
                } else if (current.equals('<')) {
                    tokenText.append(current);
                    tokenType = TokenType.LEFT_CARET;
                    context = Context.MARKDOWN;
                } else if (!lookahead.isPresent() || lookahead.isPresent() && lookahead.get().equals('<')) {
                    tokenText.append(current);
                    tokenType = TokenType.TEXT;
                } else {
                    tokenText.append(current);
                }
                break;
            case TEXT_ESCAPE:
                if (current.equals('\\') || current.equals('<')) {
                    tokenText.append(current);
                    state = State.TEXT_TEXT;
                } else {
                    throw new IllegalEscapeException(
                            tokenEndLineNumber,
                            tokenEndColumnNumber,
                            tokenText.toString(),
                            current);
                }
                break;
            default:
                throw new IllegalStateException("Internal parsing error. Got state of " + state + " in context " + context);
        }
    }

    private final void handleMarkdownState(Character current, Optional<Character> lookahead) throws TokenizerException {
        switch (state) {
            case MARKDOWN_START:
                if (Character.isWhitespace(current)) {
                    //Ignore the whitespace
                } else if (current.equals('"')){
                    state = State.STRING_TEXT;
                    context = Context.STRING;
                } else if (current.equals('=')) {
                    tokenText.append(current);
                    tokenType = TokenType.EQUALS;
                } else if (current.equals('>')) {
                    tokenText.append(current);
                    tokenType = TokenType.RIGHT_CARET;
                    context = Context.TEXT;
                } else if (Character.isAlphabetic(current)) {
                    tokenText.append(current);
                    state = State.MARKDOWN_WORD;
                    if (!lookahead.isPresent() ||
                            !(Character.isAlphabetic(lookahead.get()) || Character.isDigit(lookahead.get()))) {
                        tokenType = TokenType.WORD;
                    }
                } else {
                    throw new IllegalCharacterException(
                            tokenEndLineNumber,
                            tokenEndColumnNumber,
                            tokenText.toString(),
                            current);
                }
                break;
            case MARKDOWN_WORD:
                if (Character.isAlphabetic(current) || Character.isDigit(current)) {
                    tokenText.append(current);
                    if (!lookahead.isPresent() ||
                            !(Character.isAlphabetic(lookahead.get()) || Character.isDigit(lookahead.get()))) {
                        tokenType = TokenType.WORD;
                    }
                } else {
                    throw new IllegalCharacterException(
                            tokenEndLineNumber,
                            tokenEndColumnNumber,
                            tokenText.toString(),
                            current);
                }
                break;
            default:
                throw new IllegalStateException("Internal parsing error. Got state of " + state + " in context " + context);
        }
    }

    private final void handleStringState(Character current, Optional<Character> lookahead) throws TokenizerException {
        switch (state) {
            case STRING_TEXT:
                if (current.equals('\\')) {
                    state = State.STRING_ESCAPE;
                } else if (current.equals('"')) {
                    tokenType = TokenType.STRING;
                    context = Context.MARKDOWN;
                } else {
                    tokenText.append(current);
                }
                break;
            case STRING_ESCAPE:
                if (current.equals('\\') || current.equals('"')) {
                    tokenText.append(current);
                    state = State.STRING_TEXT;
                } else {
                    throw new IllegalEscapeException(
                            tokenEndLineNumber,
                            tokenEndColumnNumber,
                            tokenText.toString(),
                            current);
                }
                break;
            default:
                throw new IllegalStateException("Internal parsing error. Got state of " + state + " in context " + context);
        }
    }

    public static List<Token> getTokens(String input) throws TokenizerException{
        return new PageTokenizerImpl().tokenize(input);
    }
}
