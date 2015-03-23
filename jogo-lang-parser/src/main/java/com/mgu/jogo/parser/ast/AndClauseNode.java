package com.mgu.jogo.parser.ast;

public class AndClauseNode extends ExpressionNode {

    private final ExpressionNode equalityExpression;

    private final ExpressionNode nextAndClause;

    public AndClauseNode(final ExpressionNode equalityExpression, final ExpressionNode nextAndClause) {
        this.equalityExpression = equalityExpression;
        this.nextAndClause = nextAndClause;
    }

    public ExpressionNode equalityExpression() {
        return this.equalityExpression;
    }

    public ExpressionNode nextAndClause() {
        return this.nextAndClause;
    }
}