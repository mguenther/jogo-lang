package com.mgu.jogo.parser.ast;

import java.util.Collections;
import java.util.List;

public class FunctionDefinitionNode extends StatementNode {

    private final String functionName;

    private final List<ArgumentDefinitionNode> argumentDefinitions;

    private final List<StatementNode> statements;

    public FunctionDefinitionNode(final String functionName, final List<ArgumentDefinitionNode> argumentDefinitions, final List<StatementNode> statements) {
        this.functionName = functionName;
        this.argumentDefinitions = argumentDefinitions;
        this.statements = statements;
    }

    public String functionName() {
        return this.functionName;
    }

    public int arity() {
        return this.argumentDefinitions.size();
    }

    public List<ArgumentDefinitionNode> argumentDefinitions() {
        return Collections.unmodifiableList(this.argumentDefinitions);
    }

    public List<StatementNode> statements() {
        return Collections.unmodifiableList(this.statements);
    }
}