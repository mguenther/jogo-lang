package com.mgu.jogo.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.mgu.jogo.parser.Lexer;
import com.mgu.jogo.parser.LexerException;
import com.mgu.jogo.parser.Token;
import org.junit.Test;

/**
 * Set of tests for {@link com.mgu.jogo.parser.Lexer}.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class LexerTest {

    @Test
    public void nextTokenShouldImmediatelyReturnIfProgramIsEmptyString() {
        final Lexer lexer = new Lexer("");
        assertThat(lexer.nextToken(), is(Token.TOKEN_EOF));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForColon() {
        final Lexer lexer = new Lexer(":arg1");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TOKEN_COLON.type()));
    }

    @Test(expected = LexerException.class)
    public void nextTokenShouldThrowLexerExceptionIfColonIsNotFollowedByAnyCharacters() {
        final Lexer lexer = new Lexer(":");
        lexer.nextToken();
    }

    @Test(expected = LexerException.class)
    public void nextTokenShouldThrowLexerExceptionIfColonIsNotFollowedByAlphaCharacters() {
        final Lexer lexer = new Lexer(":1");
        lexer.nextToken();
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForBracketLeft() {
        final Lexer lexer = new Lexer("[");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TOKEN_BRACKET_LEFT.type()));
    }

    @Test
    public void nextTokenShouldConsumeLeftBracket() {
        final Lexer lexer = new Lexer("[[\n");
        final Token token1 = lexer.nextToken();
        final Token token2 = lexer.nextToken();
        final Token token3 = lexer.nextToken();
        assertThat(token1.type(), is(Token.TOKEN_BRACKET_LEFT.type()));
        assertThat(token2.type(), is(Token.TOKEN_BRACKET_LEFT.type()));
        assertThat(token3.type(), is(Token.TOKEN_EOF.type()));
    }

    @Test
    public void nextTokenShouldConsumeRightBracket() {
        final Lexer lexer = new Lexer("]]\n");
        final Token token1 = lexer.nextToken();
        final Token token2 = lexer.nextToken();
        final Token token3 = lexer.nextToken();
        assertThat(token1.type(), is(Token.TOKEN_BRACKET_RIGHT.type()));
        assertThat(token2.type(), is(Token.TOKEN_BRACKET_RIGHT.type()));
        assertThat(token3.type(), is(Token.TOKEN_EOF.type()));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForBracketRight() {
        final Lexer lexer = new Lexer("]");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TOKEN_BRACKET_RIGHT.type()));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForBraceLeft() {
        final Lexer lexer = new Lexer("(");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.BRACE_LEFT));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForBraceRight() {
        final Lexer lexer = new Lexer(")");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.BRACE_RIGHT));
    }

    @Test
    public void nextTokenShouldConsumeLeftBrace() {
        final Lexer lexer = new Lexer("((\n");
        final Token token1 = lexer.nextToken();
        final Token token2 = lexer.nextToken();
        final Token token3 = lexer.nextToken();
        assertThat(token1.type(), is(Token.TokenType.BRACE_LEFT));
        assertThat(token2.type(), is(Token.TokenType.BRACE_LEFT));
        assertThat(token3.type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldConsumeRightBrace() {
        final Lexer lexer = new Lexer("))\n");
        final Token token1 = lexer.nextToken();
        final Token token2 = lexer.nextToken();
        final Token token3 = lexer.nextToken();
        assertThat(token1.type(), is(Token.TokenType.BRACE_RIGHT));
        assertThat(token2.type(), is(Token.TokenType.BRACE_RIGHT));
        assertThat(token3.type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeAndValueForNumber() {
        final Lexer lexer = new Lexer("978");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TokenType.NUMBER));
        assertThat(token.value(), is("978"));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeAndValueForCharacters() {
        final Lexer lexer = new Lexer("forward");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TokenType.CHARACTERS));
        assertThat(token.value(), is("forward"));
    }

    @Test
    public void nextTokenShouldYieldEofTokenAsLastToken() {
        final Lexer lexer = new Lexer("forward 20");
        lexer.nextToken();
        lexer.nextToken();
        final Token eofToken = lexer.nextToken();
        assertThat(eofToken.type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokensShouldYieldCorrectTokensIfTextHasMoreThanOne() {
        final Lexer lexer = new Lexer("forward 20");
        final Token charactersToken = lexer.nextToken();
        final Token numberToken = lexer.nextToken();
        final Token eofToken = lexer.nextToken();
        assertThat(charactersToken.type(), is(Token.TokenType.CHARACTERS));
        assertThat(numberToken.type(), is(Token.TokenType.NUMBER));
        assertThat(eofToken.type(), is(Token.TokenType.EOF));
    }

    @Test(expected = LexerException.class)
    public void nextTokenShouldThrowLexerExceptionOnUnrecognizableCharacter() {
        final Lexer lexer = new Lexer("?");
        lexer.nextToken();
    }

    @Test
    public void nextTokenShouldIgnoreWhitespaceCharacters() {
        final Lexer lexer = new Lexer(" \t\n,forward");
        final Token token = lexer.nextToken();
        assertThat(token.type(), is(Token.TokenType.CHARACTERS));
        assertThat(token.value(), is("forward"));
    }

    @Test
    public void nextTokenShouldSeparateColonFromArgumentName() {
        final Lexer lexer = new Lexer(":arg1");
        final Token colonToken = lexer.nextToken();
        final Token argumentToken = lexer.nextToken();
        assertThat(colonToken.type(), is(Token.TokenType.COLON));
        assertThat(argumentToken.type(), is(Token.TokenType.CHARACTERS));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForEqualsSign() {
        final Lexer lexer = new Lexer("a= =");
        final Token charactersToken = lexer.nextToken();
        final Token equalsToken1 = lexer.nextToken();
        final Token equalsToken2 = lexer.nextToken();
        assertThat(charactersToken.type(), is(Token.TokenType.CHARACTERS));
        assertThat(equalsToken1.type(), is(Token.TokenType.EQUALS_SIGN));
        assertThat(equalsToken2.type(), is(Token.TokenType.EQUALS_SIGN));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForPlusOperator() {
        final Lexer lexer = new Lexer("+");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForMinusOperator() {
        final Lexer lexer = new Lexer("-");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.MIN_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForMultiplicationOperator() {
        final Lexer lexer = new Lexer("*");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.MUL_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForDivisionOperator() {
        final Lexer lexer = new Lexer("/");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.DIV_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForPowerOperator() {
        final Lexer lexer = new Lexer("^");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.POW_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForLargerThanOperator() {
        final Lexer lexer = new Lexer(">");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.LARGER_THAN_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForSmallerThanOperator() {
        final Lexer lexer = new Lexer("<");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.SMALLER_THAN_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnTokenWithCorrectTypeForEqualityOperator() {
        final Lexer lexer = new Lexer("==");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EQUALITY_OPERATOR));
    }

    @Test
    public void nextTokenShouldReturnCorrectTokensOnSuccessiveEqualsSigns() {
        final Lexer lexer = new Lexer("=====");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EQUALITY_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EQUALITY_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EQUALS_SIGN));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForOperators() {
        final Lexer lexer = new Lexer("+-*/^<>");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.MIN_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.MUL_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.DIV_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.POW_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.SMALLER_THAN_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.LARGER_THAN_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForBinaryOperation() {
        final Lexer lexer = new Lexer("a+b");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.CHARACTERS));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.CHARACTERS));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForBinaryOperationWithWhitespace() {
        final Lexer lexer = new Lexer("a + b");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.CHARACTERS));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.CHARACTERS));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForBinaryOperationWithNumbers() {
        final Lexer lexer = new Lexer("5+3");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForBinaryOperationWithNumbersAndWhitespace() {
        final Lexer lexer = new Lexer("5 + 3");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.ADD_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EOF));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForUnaryNegation() {
        final Lexer lexer = new Lexer("-3");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.MIN_OPERATOR));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
    }

    @Test
    public void nextTokenShouldDeliverSeparatedTokensForAssignment() {
        final Lexer lexer = new Lexer("a = 3");
        assertThat(lexer.nextToken().type(), is(Token.TokenType.CHARACTERS));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.EQUALS_SIGN));
        assertThat(lexer.nextToken().type(), is(Token.TokenType.NUMBER));
    }
}
