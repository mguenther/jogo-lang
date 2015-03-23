package com.mgu.jogo.parser;

/**
 * Representation of a token charaterized by its token type and token value.
 * The {@link Lexer} uses this class to break a LOGO program
 * represented as a stream of characters into a separate tokens that can
 * be digested by the LOGO parser.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Token {

    public static final Token TOKEN_COLON = new Token(TokenType.COLON, ":");

    public static final Token TOKEN_BRACKET_LEFT = new Token(TokenType.BRACKET_LEFT, "[");

    public static final Token TOKEN_BRACKET_RIGHT = new Token(TokenType.BRACKET_RIGHT, "]");

    public static final Token TOKEN_EOF = new Token(TokenType.EOF, String.valueOf((char) -1));

    public static final Token TOKEN_EQUALS_SIGN = new Token(TokenType.EQUALS_SIGN, "=");

    public static final Token TOKEN_ADD_OPERATOR = new Token(TokenType.ADD_OPERATOR, "+");

    public static final Token TOKEN_MIN_OPERATOR = new Token(TokenType.MIN_OPERATOR, "-");

    public static final Token TOKEN_MUL_OPERATOR = new Token(TokenType.MUL_OPERATOR, "*");

    public static final Token TOKEN_DIV_OPERATOR = new Token(TokenType.DIV_OPERATOR, "/");

    public static final Token TOKEN_POW_OPERATOR = new Token(TokenType.POW_OPERATOR, "^");

    public static final Token TOKEN_LARGER_THAN_OPERATOR = new Token(TokenType.LARGER_THAN_OPERATOR, ">");

    public static final Token TOKEN_SMALLER_THAN_OPERATOR = new Token(TokenType.SMALLER_THAN_OPERATOR, "<");

    public static final Token TOKEN_EQUALITY_OPERATOR = new Token(TokenType.EQUALITY_OPERATOR, "==");

    public static final Token TOKEN_BRACE_LEFT = new Token(TokenType.BRACE_LEFT, "(");

    public static final Token TOKEN_BRACE_RIGHT = new Token(TokenType.BRACE_RIGHT, ")");

    public enum TokenType {

        EOF("END_OF_FILE"),
        CHARACTERS("CHARACTERS"),
        NUMBER("NUMBER"),
        COLON("COLON"),
        EQUALS_SIGN("EQUALS_SIGN"),
        ADD_OPERATOR("ADD_OPERATOR"),
        MIN_OPERATOR("MINUS_OPERATOR"),
        MUL_OPERATOR("MUL_OPERATOR"),
        DIV_OPERATOR("DIV_OPERATOR"),
        POW_OPERATOR("POW_OPERATOR"),
        LARGER_THAN_OPERATOR("LARGER_THAN_OPERATOR"),
        SMALLER_THAN_OPERATOR("SMALLER_THAN_OPERATOR"),
        EQUALITY_OPERATOR("EQUALITY_OPERATOR"),
        BRACKET_LEFT("BRACKET_LEFT"),
        BRACKET_RIGHT("BRACKET_RIGHT"),
        BRACE_LEFT("BRACE_LEFT"),
        BRACE_RIGHT("BRACE_RIGHT");

        private final String tokenName;

        private TokenType(final String tokenName) {
            this.tokenName = tokenName;
        }

        @Override
        public String toString() {
            return this.tokenName;
        }
    }

    private final String value;

    private final TokenType type;

    private Token(final TokenType type, final String value) {
        this.type = type;
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public TokenType type() {
        return this.type;
    }

    public boolean matchesType(final TokenType otherTokenType) {
        return this.type.equals(otherTokenType);
    }

    public boolean matchesValue(final String otherTokenValue) {
        return this.value.equalsIgnoreCase(otherTokenValue);
    }

    public boolean matches(final TokenType otherTokenType, final String otherTokenValue) {
        return matchesType(otherTokenType) && matchesValue(otherTokenValue);
    }

    @Override
    public String toString() {
        return this.type + "[value=\"" + this.value + "\"]";
    }

    public static Token number(final String number) {
        return new Token(TokenType.NUMBER, number);
    }

    public static Token characters(final String characters) {
        return new Token(TokenType.CHARACTERS, characters);
    }
}
