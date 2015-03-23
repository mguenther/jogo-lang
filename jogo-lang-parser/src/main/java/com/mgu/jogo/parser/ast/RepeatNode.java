package com.mgu.jogo.parser.ast;

import java.util.Collections;
import java.util.List;

public class RepeatNode extends StatementNode {

    private final List<StatementNode> statements;

    private final ExpressionNode times;

    public RepeatNode(final ExpressionNode times, final List<StatementNode> statements) {
        this.times = times;
        this.statements = statements;
    }

    public List<StatementNode> statements() {
        return Collections.unmodifiableList(this.statements);
    }

    public ExpressionNode times() {
        return this.times;
    }
}