package com.mgu.jogo.parser.ast;

public class EqualityExpressionNode extends ExpressionNode {

    private final ExpressionNode equalityOperand;

    private final ExpressionNode nextEqualityOperand;

    public EqualityExpressionNode(final ExpressionNode equalityOperand, final ExpressionNode nextEqualityOperand) {
        this.equalityOperand = equalityOperand;
        this.nextEqualityOperand = nextEqualityOperand;
    }

    public ExpressionNode equalityOperand() {
        return this.equalityOperand;
    }

    public ExpressionNode nextEqualityOperand() {
        return this.nextEqualityOperand;
    }
}