package com.mgu.jogo.parser.ast;

import java.util.Collections;
import java.util.List;

public class ProgramNode {

    private final List<StatementNode> statements;

    public ProgramNode(final List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> statementNodes() {
        return Collections.unmodifiableList(this.statements);
    }
}