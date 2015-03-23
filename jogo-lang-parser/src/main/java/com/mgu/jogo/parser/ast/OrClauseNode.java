package com.mgu.jogo.parser.ast;

public class OrClauseNode extends ExpressionNode {

    private final ExpressionNode andClause;

    private final ExpressionNode nextOrClause;

    public OrClauseNode(final ExpressionNode andClause, final ExpressionNode nextOrClause) {
        this.andClause = andClause;
        this.nextOrClause = nextOrClause;
    }

    public ExpressionNode andClause() {
        return this.andClause;
    }

    public ExpressionNode nextOrClause() {
        return this.nextOrClause;
    }
}