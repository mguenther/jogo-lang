package com.mgu.jogo.parser.ast;

public class MultiplicativeExpressionNode extends ExpressionNode {

    private final ExpressionNode leftFactor;

    private final ExpressionNode rightFactor;

    private final char operator;

    public MultiplicativeExpressionNode(final ExpressionNode leftFactor, final ExpressionNode rightFactor, final char operator) {
        this.leftFactor = leftFactor;
        this.rightFactor = rightFactor;
        this.operator = operator;
    }

    public ExpressionNode leftFactor() {
        return this.leftFactor;
    }

    public ExpressionNode rightFactor() {
        return this.rightFactor;
    }

    public char operator() {
        return this.operator;
    }
}