package com.mgu.jogo.parser.ast;

/**
 * Visitor for node types of the AST.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public interface TreeVisitor {

    void visit(ArgumentDefinitionNode node);

    void visit(FunctionCallNode node);

    void visit(FunctionDefinitionNode node);

    void visit(NumberNode node);

    void visit(BooleanNode node);

    void visit(ProgramNode node);

    void visit(StatementNode node);

    void visit(VariableReferenceNode node);

    void visit(RepeatNode node);

    void visit(IfNode node);

    void visit(AssignmentNode node);

    void visit(OrClauseNode node);

    void visit(AndClauseNode node);

    void visit(EqualityExpressionNode node);

    void visit(EqualityOperandNode node);

    void visit(AdditiveExpressionNode node);

    void visit(MultiplicativeExpressionNode node);

    void visit(UnaryExpressionNode node);

    void visit(SimpleExpressionNode node);
}
