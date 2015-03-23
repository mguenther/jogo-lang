package com.mgu.jogo.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.mgu.jogo.parser.ast.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;

/**
 * Set of unit tests for {@link com.mgu.jogo.parser.Parser}.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class ParserTest {

    @Test(expected = ParserException.class)
    public void parseShouldThrowParserExceptionIfProgramIsEmpty() {
        final Parser parser = new Parser(StringUtils.EMPTY);
        parser.parse();
    }

    @Test
    public void parseShouldMatchBuiltinFunctionWithSingleArgument() {
        final String program = "forward 100";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldNotMatchBuiltinFunctionWithSingleArgumentAndNoneProvided() {
        final String program = "forward";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchBuiltinFunctionWithNoArgument() {
        final String program = "showturtle";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchFunctionDefinitionWithNoArguments() {
        final String program = "to userfunction forward 100 end";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchFunctionDefinitionWithArguments() {
        final String program = "to userfunction :arg1 :arg2 forward 100 end";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldNotMatchAmbiguousFunctionDefinitions() {
        final String program = "to userfunction forward 100 end\n" +
                               "to userfunction back 100 end";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldNotMatchCallToUndefinedUserFunction() {
        final String program = "userfunction";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchCallToPreviouslyDefinedFunctionWithNoArguments() {
        final String program = "to userfunction forward 100 end\n" +
                               "userfunction";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchCallToPreviouslyDefinedFunctionWithArguments() {
        final String program = "to userfunction :arg1 :arg2 forward 100 end\n" +
                               "userfunction 100 200";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldNotMatchCallToPreviouslyDefinedFunctionWithWrongArgumentNumber() {
        final String program = "to userfunction :arg1 :arg2 forward 100 end\n" +
                               "userfunction 100";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldThrowParserExceptionIfFunctionIsCalledBeforeDefined() {
        final String program = "userfunction 100\n" +
                               "to userfunction :arg1 forward arg1 end";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchFunctionLocalArguments() {
        final String program = "to userfunction :arg1 forward arg1 end";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchLocalArgumentsForUserFunctionInFunction() {
        final String program = "to userfunction1 :arg1 forward arg1 end\n" +
                               "to userfunction2 :arg1 userfunction1 arg1 end\n" +
                               "userfunction2 100";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchStandaloneRepeat() {
        final String program = "repeat 5 [ fd 1 ]";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchNestedRepeat() {
        final String program = "to circle\n" +
                               "  repeat 360 [\n" +
                               "    forward 1\n" +
                               "    right 1\n" +
                               "  ]\n" +
                               "end\n";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchNestedRepeatWithOuterVariableReference() {
        final String program = "to arc :times\n" +
                               "  repeat times [\n" +
                               "    forward 1\n" +
                               "    right 1\n" +
                               "  ]\n" +
                               "end\n";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchNestedRepeatWithInnerVariableReference() {
        final String program = "to arc :times :angle\n" +
                               "  repeat times [\n" +
                               "    forward 1\n" +
                               "    right angle\n" +
                               "  ]\n" +
                               "end\n";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchIf() {
        final String program = "if 3 < 5 [ forward 1 ]";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchIfElse() {
        final String program = "ifelse 3 < 5 [ forward 1 ] [ right 1 ]";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test(expected = ParserException.class)
    public void parseShouldNotMatchIfWithBothThenAndElseStatements() {
        final String program = "if 3 < 5 [ forward 1 ] [ right 1 ]";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchNegatedUnaryExpressionWithNumber() {
        final String program = "to f :arg1\n" +
                               "  forward arg1\n" +
                               "end\n" +
                               "f -3\n";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchNegatedUnaryExpressionWithVariable() {
        final String program = "to f :arg1 fd arg1 end f -a";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchExpressionInBraces() {
        final String program = "to f :arg1 fd arg1 end f (3)";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchMultiplication() {
        final String program = "to f :arg1 fd arg1 end f 3*4";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchDivision() {
        final String program = "to f :arg1 fd arg1 end f 4/2";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchPower() {
        final String program = "to f :arg1 fd arg1 end f 2^3";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchUnaryExpressionMultipliedWithMultiplicativeExpression() {
        final String program = "to f :arg1 fd arg1 end f -1*3*4";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchAddition() {
        final String program = "to f :arg1 fd arg1 end f 3+4";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchSubtraction() {
        final String program = "to f :arg1 fd arg1 end f 4-3";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchMultiplicativeExpressionsCombinedWithAdditiveExpressions() {
        final String program = "to f :arg1 fd arg1 end f 3*4+5*-5";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldAdhereToMulOverAddOperatorPreference() {
        final String program = "to f :arg1 fd arg1 end f 3*4+5/-5";
        final Parser parser = new Parser(program);
        final ProgramNode programNode = parser.parse();
        final FunctionCallNode functionCallNode = (FunctionCallNode) programNode.statementNodes().get(1);
        final List<ExpressionNode> expressionNodes = functionCallNode.arguments();
        assertThat(expressionNodes.size(), is(1));
        final AdditiveExpressionNode additiveExpression = (AdditiveExpressionNode) expressionNodes.get(0);
        assertThat(additiveExpression.operator(), is('+'));
        final MultiplicativeExpressionNode leftExpression = (MultiplicativeExpressionNode) additiveExpression.addend();
        final MultiplicativeExpressionNode rightExpression = (MultiplicativeExpressionNode) additiveExpression.augend();
        assertThat(leftExpression.operator(), is('*'));
        assertThat(rightExpression.operator(), is('/'));
    }

    @Test
    public void parseShouldMatchVariableAssignmentToNumber() {
        final String program = "a = 3";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchVariableAssignmentToVariable() {
        final String program = "b = 3\n" +
                               "a = b";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchFunctionCallWithExpression() {
        final String program = "forward 3+4";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchBooleanFalse() {
        final String program = "a = false";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchBooleanTrue() {
        final String program = "a = true";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchSimpleOrClause() {
        final String program = "a = true or false";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchSimpleAndClause() {
        final String program = "a = true and false";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchMatchSimpleEqualityExpression() {
        final String program = "a = 3 == 5";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchLargerThanExpression() {
        final String program = "a = 5 > 2";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchSmallerThanExpression() {
        final String program = "a = 3 < 7";
        final Parser parser = new Parser(program);
        parser.parse();
    }

    @Test
    public void parseShouldMatchComplexRelationalExpression() {
        final String program = "a = 3 < 5 and true or 5 > 3 and true and 1 == 1";
        final Parser parser = new Parser(program);
        parser.parse();
    }
}