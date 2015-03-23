package com.mgu.jogo.parser;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Breaks up a given <code>String</code> of characters into named
 * tokens.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Lexer {

    private static final char EOF = (char) -1;

    private static final List<Character> WHITESPACE = Arrays.asList(' ', '\t', '\n', ',');

    private static final List<Character> OPERATORS = Arrays.asList('+', '-', '*', '/', '^', '<', '>', '=');

    private final String input;

    private int index = 0;

    private char currentCharacter;

    public Lexer(final String input) {
        this.input = input.trim();
        if (StringUtils.isEmpty(input)) {
            this.currentCharacter = EOF;
        } else {
            this.index = 1;
            this.currentCharacter = input.charAt(0);
            if (!isValidCharacter()) {
                throw new LexerException("Unable to break stream of characters into separate tokens because of an unrecognized character.");
            }
        }
    }

    private boolean isValidCharacter() {
        return isWhitespace() || isColon() || isAlphanumeric() || isBracketRight() || isBracketLeft() || isOperator() || isBraceLeft() || isBraceRight();
    }

    private boolean isOperator() {
        return OPERATORS.stream().filter(c -> c == this.currentCharacter).findAny().isPresent();
    }

    private boolean isEof() {
        return this.currentCharacter == EOF;
    }

    public Token nextToken() {

        if (isEof()) {
            return Token.TOKEN_EOF;
        }

        while (isWhitespace()) {
            consume();
        }

        if (isEqualsSign()) {
            consume();
            // if we have two '=' in succession, we return the equality
            // operator otherwise the equals sign used for assignments
            if (isEqualsSign()) {
                consume();
                return Token.TOKEN_EQUALITY_OPERATOR;
            } else {
                return Token.TOKEN_EQUALS_SIGN;
            }
        }

        if (isPlusOperator()) {
            consume();
            return Token.TOKEN_ADD_OPERATOR;
        }

        if (isMinusOperator()) {
            consume();
            return Token.TOKEN_MIN_OPERATOR;
        }

        if (isMultiplicationOperator()) {
            consume();
            return Token.TOKEN_MUL_OPERATOR;
        }

        if (isDivisionOperator()) {
            consume();
            return Token.TOKEN_DIV_OPERATOR;
        }

        if (isPowerOperator()) {
            consume();
            return Token.TOKEN_POW_OPERATOR;
        }

        if (isLargerThanOperator()) {
            consume();
            return Token.TOKEN_LARGER_THAN_OPERATOR;
        }

        if (isSmallerThanOperator()) {
            consume();
            return Token.TOKEN_SMALLER_THAN_OPERATOR;
        }

        if (isColon()) {
            if (noMoreCharacters()) {
                throw new LexerException("Expecting more characters after ':'.");
            }
            consume();
            if (!isAlpha()) {
                throw new LexerException("Expecting alpha-numeric characters after ':'.");
            }
            return Token.TOKEN_COLON;
        }

        if (isBraceLeft()) {
            consume();
            return Token.TOKEN_BRACE_LEFT;
        }

        if (isBraceRight()) {
            consume();
            return Token.TOKEN_BRACE_RIGHT;
        }

        if (isBracketLeft()) {
            consume();
            return Token.TOKEN_BRACKET_LEFT;
        }

        if (isBracketRight()) {
            consume();
            return Token.TOKEN_BRACKET_RIGHT;
        }

        if (isNumeric()) {
            return Token.number(readNumber());
        }

        if (isAlpha()) {
            return Token.characters(readCharacters());
        }

        throw new LexerException("Unable to break stream of characters into separate tokens because of an unrecognized character.");
    }

    private boolean noMoreCharacters() {
        return this.index >= this.input.length();
    }

    private boolean isWhitespace() {
        return WHITESPACE
                .stream()
                .anyMatch(c -> c == this.currentCharacter);
    }

    private boolean matchesCharacterToken(final Token token) {
        return token.value().toCharArray()[0] == this.currentCharacter;
    }

    private boolean isColon() {
        return matchesCharacterToken(Token.TOKEN_COLON);
    }

    private boolean isEqualsSign() {
        return matchesCharacterToken(Token.TOKEN_EQUALS_SIGN);
    }

    private boolean isPlusOperator() {
        return matchesCharacterToken(Token.TOKEN_ADD_OPERATOR);
    }

    private boolean isMinusOperator() {
        return matchesCharacterToken(Token.TOKEN_MIN_OPERATOR);
    }

    private boolean isMultiplicationOperator() {
        return matchesCharacterToken(Token.TOKEN_MUL_OPERATOR);
    }

    private boolean isDivisionOperator() {
        return matchesCharacterToken(Token.TOKEN_DIV_OPERATOR);
    }

    private boolean isPowerOperator() {
        return matchesCharacterToken(Token.TOKEN_POW_OPERATOR);
    }

    private boolean isLargerThanOperator() {
        return matchesCharacterToken(Token.TOKEN_LARGER_THAN_OPERATOR);
    }

    private boolean isSmallerThanOperator() {
        return matchesCharacterToken(Token.TOKEN_SMALLER_THAN_OPERATOR);
    }

    private boolean isBraceLeft() {
        return matchesCharacterToken(Token.TOKEN_BRACE_LEFT);
    }

    private boolean isBraceRight() {
        return matchesCharacterToken(Token.TOKEN_BRACE_RIGHT);
    }

    private boolean isBracketLeft() {
        return matchesCharacterToken(Token.TOKEN_BRACKET_LEFT);
    }

    private boolean isBracketRight() {
        return matchesCharacterToken(Token.TOKEN_BRACKET_RIGHT);
    }

    private boolean isNumeric() {
        return CharUtils.isAsciiNumeric(this.currentCharacter);
    }

    private String readNumber() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.currentCharacter);
        boolean detectedTokenBoundary = false;
        while (!noMoreCharacters()) {
            consume();
            if (isWhitespace()) {
                detectedTokenBoundary = true;
                break;
            }
            if (!isNumeric()) {
                detectedTokenBoundary = true;
                break;
            }
            buffer.append(this.currentCharacter);
        }
        if (!detectedTokenBoundary) {
            consume(); // consumes the last character that we have read, before breaking the while
        }
        return buffer.toString();
    }

    private boolean isAlpha() {
        return CharUtils.isAsciiAlpha(this.currentCharacter);
    }

    private boolean isAlphanumeric() {
        return CharUtils.isAsciiAlphanumeric(this.currentCharacter);
    }

    private String readCharacters() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.currentCharacter);
        boolean detectedTokenBoundary = false;
        while (!noMoreCharacters()) {
            consume();
            if (isWhitespace()) {
                detectedTokenBoundary = true;
                break;
            }
            if (!isAlphanumeric()) {
                detectedTokenBoundary = true;
                break;
            }
            buffer.append(this.currentCharacter);
        }
        if (!detectedTokenBoundary) {
            consume(); // consumes the last character that we have read, before breaking the while
        }
        return buffer.toString();
    }

    private void consume() {
        if (!noMoreCharacters()) {
            this.currentCharacter = this.input.charAt(this.index);
        } else {
            this.currentCharacter = EOF;
        }
        this.index++;
    }

    public static void main(String[] args) {
        //final String program = "forward 100\n\n  fd 200 pd penup pendown";
        final String program = "[[\n";
        final Lexer lexer = new Lexer(program);
        Token currentToken = lexer.nextToken();
        while (!(Token.TOKEN_EOF.equals(currentToken))) {
            System.out.println(currentToken);
            currentToken = lexer.nextToken();
        }
    }
}
