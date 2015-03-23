package com.mgu.jogo.parser.ast;

import java.util.List;

public class FunctionCallNode extends StatementNode {

    private final boolean isBuiltin;

    private final String functionName;

    private final int arity;

    private final List<ExpressionNode> expressions;

    public FunctionCallNode(final String functionName, final int arity, final boolean isBuiltin, final List<ExpressionNode> expressions) {
        this.functionName = functionName;
        this.arity = arity;
        this.isBuiltin = isBuiltin;
        this.expressions = expressions;
    }

    public List<ExpressionNode> arguments() {
        return this.expressions;
    }

    public int arity() {
        return this.arity;
    }

    public String functionName() {
        return this.functionName;
    }

    public boolean isBuiltin() {
        return this.isBuiltin;
    }
}
