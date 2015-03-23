package com.mgu.jogo.parser.ast;

public class UnaryExpressionNode extends ExpressionNode {

    private final boolean negate;

    private final ExpressionNode expression;

    public UnaryExpressionNode(final ExpressionNode expression, final boolean negate) {
        this.expression = expression;
        this.negate = negate;
    }

    public boolean negate() {
        return this.negate;
    }

    public ExpressionNode expression() {
        return this.expression;
    }
}