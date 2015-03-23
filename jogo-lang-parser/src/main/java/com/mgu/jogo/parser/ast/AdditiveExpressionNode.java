package com.mgu.jogo.parser.ast;

public class AdditiveExpressionNode extends ExpressionNode {

    private final ExpressionNode addend;

    private final ExpressionNode augend;

    private final char operator;

    public AdditiveExpressionNode(final ExpressionNode addend, final ExpressionNode augend, final char operator) {
        this.addend = addend;
        this.augend = augend;
        this.operator = operator;
    }

    public ExpressionNode addend() {
        return this.addend;
    }

    public ExpressionNode augend() {
        return this.augend;
    }

    public char operator() {
        return this.operator;
    }
}