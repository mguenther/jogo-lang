package com.mgu.jogo.parser.ast;

public class BooleanNode extends ExpressionNode {

    private final boolean value;

    public BooleanNode(final boolean value) {
        this.value = value;
    }

    public boolean value() {
        return this.value;
    }
}