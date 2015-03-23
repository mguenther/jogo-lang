package com.mgu.jogo.parser.ast;

public class NumberNode extends ExpressionNode {

    private final int number;

    public NumberNode(final int number) {
        this.number = number;
    }

    public int value() {
        return this.number;
    }
}