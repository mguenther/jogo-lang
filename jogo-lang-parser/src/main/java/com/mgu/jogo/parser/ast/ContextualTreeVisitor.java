package com.mgu.jogo.parser.ast;

/**
 * Visitor for node types of the AST that consumes a second argument of
 * parametric type <code>T</code>, thus enabling implementers of this
 * interface to provide some additional form of context, which gets
 * passed along the tree visitation (e.g. tree-based interpreters
 * require some kind of execution context).
 *
 * @param <T> holds additional context while visiting nodes of the AST
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public interface ContextualTreeVisitor<T> {

    void visit(ArgumentDefinitionNode node, T context);

    void visit(FunctionCallNode node, T context);

    void visit(FunctionDefinitionNode node, T context);

    void visit(NumberNode node, T context);

    void visit(BooleanNode node, T context);

    void visit(ProgramNode node, T context);

    void visit(StatementNode node, T context);

    void visit(VariableReferenceNode node, T context);

    void visit(RepeatNode node, T context);

    void visit(IfNode node, T context);

    void visit(AssignmentNode node, T context);

    void visit(OrClauseNode node, T context);

    void visit(AndClauseNode node, T context);

    void visit(EqualityExpressionNode node, T context);

    void visit(EqualityOperandNode node, T context);

    void visit(AdditiveExpressionNode node, T context);

    void visit(MultiplicativeExpressionNode node, T context);

    void visit(UnaryExpressionNode node, T context);

    void visit(SimpleExpressionNode node, T context);
}
