package com.mgu.jogo.parser.ast;

public class VariableReferenceNode extends ExpressionNode {

    private final String variableName;

    public VariableReferenceNode(final String variableName) {
        this.variableName = variableName;
    }

    public String variableName() {
        return this.variableName;
    }
}