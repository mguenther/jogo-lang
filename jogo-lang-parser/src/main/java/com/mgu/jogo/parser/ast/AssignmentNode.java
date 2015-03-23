package com.mgu.jogo.parser.ast;

public class AssignmentNode extends StatementNode {

    private final String variable;

    private final ExpressionNode expression;

    public AssignmentNode(final String variable, final ExpressionNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public String variable() {
        return this.variable;
    }

    public ExpressionNode expression() {
        return this.expression;
    }
}