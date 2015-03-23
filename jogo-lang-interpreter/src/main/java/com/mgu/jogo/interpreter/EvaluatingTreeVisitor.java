package com.mgu.jogo.interpreter;

import com.mgu.jogo.parser.ast.AdditiveExpressionNode;
import com.mgu.jogo.parser.ast.AndClauseNode;
import com.mgu.jogo.parser.ast.ArgumentDefinitionNode;
import com.mgu.jogo.parser.ast.AssignmentNode;
import com.mgu.jogo.parser.ast.BooleanNode;
import com.mgu.jogo.parser.ast.ContextualTreeVisitor;
import com.mgu.jogo.parser.ast.EqualityExpressionNode;
import com.mgu.jogo.parser.ast.EqualityOperandNode;
import com.mgu.jogo.parser.ast.FunctionCallNode;
import com.mgu.jogo.parser.ast.FunctionDefinitionNode;
import com.mgu.jogo.parser.ast.IfNode;
import com.mgu.jogo.parser.ast.MultiplicativeExpressionNode;
import com.mgu.jogo.parser.ast.NumberNode;
import com.mgu.jogo.parser.ast.OrClauseNode;
import com.mgu.jogo.parser.ast.ProgramNode;
import com.mgu.jogo.parser.ast.RepeatNode;
import com.mgu.jogo.parser.ast.SimpleExpressionNode;
import com.mgu.jogo.parser.ast.StatementNode;
import com.mgu.jogo.parser.ast.UnaryExpressionNode;
import com.mgu.jogo.parser.ast.VariableReferenceNode;

import java.util.List;

/**
 * Tree-based interpreter which uses the hierarchical data structure
 * <code>Environment</code> for implementing scoping while walking
 * along the AST.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class EvaluatingTreeVisitor implements ContextualTreeVisitor<Environment> {

    @Override
    public void visit(final ArgumentDefinitionNode node, final Environment environment) {
        // NO-OP (ArgumentDefinitionNode is a base class)
    }

    @Override
    public void visit(final FunctionCallNode node, final Environment environment) {
        if (node.isBuiltin()) {
            invokeBuiltin(node, environment);
        } else {
            invokeUserDefinedFunction(node, environment);
        }
    }

    private void invokeBuiltin(final FunctionCallNode node, final Environment environment) {
        Procedures.call(node, environment);
    }

    private void invokeUserDefinedFunction(final FunctionCallNode node, final Environment environment) {
        final FunctionDefinitionNode function = environment.lookupFunction(node.functionName());

        if (function.arity() != node.arity()) {
            throw new InterpreterException("Unable to invoke user-defined function because expression lists do not match. Expected " + function.arity() + " expressions, but found " + node.arity() + " expressions.");
        }

        final Environment functionEnvironment = environment.createChildEnvironment();
        for (int i = 0; i < function.arity(); i++) {
            final String argumentName = function.argumentDefinitions().get(i).argumentName();
            final Integer argumentValue = ExpressionResolver.resolve(node.arguments().get(i), environment);
            functionEnvironment.install(argumentName, argumentValue);
        }
        function.statements().forEach(statement -> visit(statement, functionEnvironment));
    }

    @Override
    public void visit(final FunctionDefinitionNode node, final Environment environment) {
        environment.install(node);
    }

    @Override
    public void visit(final NumberNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final ProgramNode node, final Environment environment) {
        node.statementNodes().forEach(statementNode -> visit(statementNode, environment));
    }

    @Override
    public void visit(final StatementNode node, final Environment environment) {
        if (node instanceof FunctionCallNode) {
            visit((FunctionCallNode) node, environment);
        } else if (node instanceof FunctionDefinitionNode) {
            visit((FunctionDefinitionNode) node, environment);
        } else if (node instanceof RepeatNode) {
            visit((RepeatNode) node, environment);
        } else if (node instanceof AssignmentNode) {
            visit((AssignmentNode) node, environment);
        } else if (node instanceof IfNode) {
            visit((IfNode) node, environment);
        } else {
            throw new InterpreterException("StatementNode is not in list of admissible types: [FunctionCallNode, FunctionDefinitionNode, RepeatNode, AssignmentNode]");
        }
    }

    @Override
    public void visit(final VariableReferenceNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final RepeatNode node, final Environment environment) {
        final int times = ExpressionResolver.resolve(node.times(), environment);
        for (int i = 0; i < times; i++) {
            node.statements().forEach(statementNode -> visit(statementNode, environment));
        }
    }

    @Override
    public void visit(final AssignmentNode node, final Environment environment) {
        final int result = ExpressionResolver.resolve(node.expression(), environment);
        environment.install(node.variable(), result);
    }

    @Override
    public void visit(final AdditiveExpressionNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final MultiplicativeExpressionNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final UnaryExpressionNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final SimpleExpressionNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final BooleanNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final OrClauseNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final AndClauseNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final EqualityExpressionNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final EqualityOperandNode node, final Environment environment) {
        // NO-OP
    }

    @Override
    public void visit(final IfNode node, final Environment environment) {
        final int condition = ExpressionResolver.resolve(node.condition(), environment);
        final List<StatementNode> statements = condition == 1 ? node.thenStatements() : node.elseStatements();
        statements.stream().forEach(statement -> visit(statement, environment));
    }
}