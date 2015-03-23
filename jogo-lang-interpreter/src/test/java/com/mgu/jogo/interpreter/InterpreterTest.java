package com.mgu.jogo.interpreter;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Set of unit tests for {@link com.mgu.jogo.interpreter.Interpreter}.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class InterpreterTest {

    @Test
    public void runShouldExecuteBuiltInForwardOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 100 fd 100");
        verify(turtle, times(2)).forward(100);
    }

    @Test
    public void runShouldExecuteBuiltInBackOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "back 100 bk 100");
        verify(turtle, times(2)).back(100);
    }

    @Test
    public void runShouldExecuteBuiltInHomeOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "home");
        verify(turtle, times(1)).move(0, 0);
    }

    @Test
    public void runShouldExecuteBuiltInPenUpOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "penup pu");
        verify(turtle, times(2)).penUp();
    }

    @Test
    public void runShouldExecuteBuiltInPenDownOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "pendown pd");
        verify(turtle, times(2)).penDown();
    }

    @Test
    public void runShouldExecuteBuiltInHideOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "hideturtle ht");
        verify(turtle, times(2)).hide();
    }

    @Test
    public void runShouldExecuteBuiltInShowOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "showturtle st");
        verify(turtle, times(2)).show();
    }

    @Test
    public void runShouldExecuteBuiltInSetColorOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "setc 1 setpc 1 setpencolor 1");
        verify(turtle, times(3)).setPenColor(1);
    }

    @Test
    public void runShouldExecuteBuiltInLeftOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "left 30 lt 30");
        verify(turtle, times(2)).left(30);
    }

    @Test
    public void runShouldExecuteBuiltInRightOpOnTurtle() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "right 30 rt 30");
        verify(turtle, times(2)).right(30);
    }

    @Test
    public void runShouldDefineAndExecuteUserFunction() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "to userfunction :arg1 forward arg1 end\n" +
                                "userfunction 100");
        verify(turtle, times(1)).forward(100);
    }

    @Test
    public void runShouldEvaluateAdditiveExpressionWithNoNestedExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 3 + 4");
        verify(turtle, times(1)).forward(7);
    }

    @Test
    public void runShouldEvaluateAdditiveExpressionWithMinusAndNoNestedExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 6-1");
        verify(turtle, times(1)).forward(5);
    }

    @Test
    public void runShouldEvaluateMultiplicativeExpressionWithNoNestedExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 2 * 4");
        verify(turtle, times(1)).forward(8);
    }

    @Test
    public void runShouldEvaluateMultiplicativeExpressionWithDivisionAndNoNestedExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 4 / 2");
        verify(turtle, times(1)).forward(2);
    }

    @Test
    public void runShouldEvaluateMultiplicativeExpressionWithPowerAndNoNestedExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 2^3");
        verify(turtle, times(1)).forward(8);
    }

    @Test
    public void runShouldEvaluateNegatedUnaryExpression() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward -1");
        verify(turtle, times(1)).forward(-1);
    }

    @Test
    public void runShouldEvaluateMoreThanTwoAdditiveExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 1+1+1+1+1");
        verify(turtle, times(1)).forward(5);
    }

    @Test
    public void runShouldEvaluateMoreThanTwoMultiplicativeExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 2*2*2*2");
        verify(turtle, times(1)).forward(16);
    }

    @Test
    public void runShouldEvaluateAdditiveExpressionWithNestedMultiplicativeExpressions() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 2*3 + 5*-1");
        verify(turtle, times(1)).forward(1);
    }

    @Test
    public void runShouldEvaluateAdditiveExpresssionsWithNestedMultiplicativeExpression() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward 3+4*5+2");
        verify(turtle, times(1)).forward(25);
    }

    @Test
    public void runShouldEvaluateNestedExpressionInBraces() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "forward (3+4) * (5+2)");
        verify(turtle, times(1)).forward(49);
    }

    @Test
    public void runShouldEvaluateVariableAssignmentWithSimpleExpression() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3\nforward a");
        verify(turtle, times(1)).forward(3);
    }

    @Test
    public void runShouldEvaluateVariableAssignmentWithSumOfPreviouslyDefinedVariables() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 2\nb=3\nc=a+b\nforward c");
        verify(turtle, times(1)).forward(5);
    }

    @Test
    public void runShouldEvaluateLargerThanExpressionToTrueIfExpressionIsTrue() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 > 2\nforward a");
        verify(turtle, times(1)).forward(1);
    }

    @Test
    public void runShouldEvaluateLargerThanExpressionToFalseIfExpressionIsFalse() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 > 5\nforward a");
        verify(turtle, times(1)).forward(0);
    }

    @Test
    public void runShouldEvaluateSmallerThanExpressionToTrueIfExpressionIsTrue() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 < 5\nforward a");
        verify(turtle, times(1)).forward(1);
    }

    @Test
    public void runShouldEvaluateSmallerThanExpressionToFalseIfExpressionIsFalse() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 < 1\nforward a");
        verify(turtle, times(1)).forward(0);
    }

    @Test
    public void runShouldEvaluateEqualityOperationToTrueIfLhsMatchesRhs() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 == 3\nforward a");
        verify(turtle, times(1)).forward(1);
    }

    @Test
    public void runShouldEvaluateEqualityOperationToFalseIfLhsDoesNotMatchRhs() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "a = 3 == 2\nforward a");
        verify(turtle, times(1)).forward(0);
    }

    @Test
    public void runShouldEvaluateThenStatementsIfConditionIsEvaluatesToTrue() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "if 3 < 5 [ forward 1 ]");
        verify(turtle, times(1)).forward(1);
    }

    @Test
    public void runShouldEvaluateThenStatementsIfElseConditionEvaluatesToTrue() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "ifelse 3 < 5 [ forward 1 ] [ right 1 ]");
        verify(turtle, times(1)).forward(1);
        verify(turtle, times(0)).right(1);
    }

    @Test
    public void runShouldEvaluateElseStatementsIfElseConditionEvaluatesToFalse() {
        final Turtle turtle = mock(Turtle.class);
        final Interpreter interpreter = new Interpreter();
        interpreter.run(turtle, "ifelse 5 < 3 [ forward 1 ] [ right 1 ]");
        verify(turtle, times(0)).forward(1);
        verify(turtle, times(1)).right(1);
    }
}