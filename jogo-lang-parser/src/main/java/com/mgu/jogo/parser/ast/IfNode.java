package com.mgu.jogo.parser.ast;

import java.util.List;

public class IfNode extends StatementNode {

    private final ExpressionNode condition;

    private final List<StatementNode> thenStatements;

    private final List<StatementNode> elseStatements;

    public IfNode(
            final ExpressionNode condition,
            final List<StatementNode> thenStatements) {
        this(condition, thenStatements, null);
    }

    public IfNode(
            final ExpressionNode condition,
            final List<StatementNode> thenStatements,
            final List<StatementNode> elseStatements) {
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
    }

    public ExpressionNode condition() {
        return this.condition;
    }

    public List<StatementNode> thenStatements() {
        return this.thenStatements;
    }

    public List<StatementNode> elseStatements() {
        return this.elseStatements;
    }
}
