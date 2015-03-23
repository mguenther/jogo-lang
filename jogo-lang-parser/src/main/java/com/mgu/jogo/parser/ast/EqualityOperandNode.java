package com.mgu.jogo.parser.ast;

public class EqualityOperandNode extends ExpressionNode {

    private final ExpressionNode additiveExpression;

    private final ExpressionNode nextAdditiveExpression;

    private final char operator;

    public EqualityOperandNode(final ExpressionNode additiveExpression, final ExpressionNode nextAdditiveExpression, final char operator) {
        this.additiveExpression = additiveExpression;
        this.nextAdditiveExpression = nextAdditiveExpression;
        this.operator = operator;
    }

    public ExpressionNode additiveExpression() {
        return this.additiveExpression;
    }

    public ExpressionNode nextAdditiveExpression() {
        return this.nextAdditiveExpression;
    }

    public char operator() {
        return this.operator;
    }
}