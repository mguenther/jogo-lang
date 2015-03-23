package com.mgu.jogo.interpreter;

import com.mgu.jogo.parser.ast.*;

/**
 * Resolves an expression of abstract type <code>ExpressionNode</code> to its
 * <code>Integer</code>-based value using scoping and function / value binding
 * information provided by an <code>Environment</code>.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class ExpressionResolver {

    /**
     * Consumes an arbitrary expression of abstract type <code>ExpressionNode</code>
     * and resolves it using the data provided by the given <code>Environment/code>.
     *
     * @param expressionNode
     *      abstract type representing an expression
     * @param environment
     *      holds scoping and function / value binding information
     * @throws InterpreterException
     *      in case the given dynamic type of <code>ExpressionNode</code> is
     *      not admissible
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final ExpressionNode expressionNode, final Environment environment) {
        int result;
        if (expressionNode instanceof VariableReferenceNode) {
            result = resolve((VariableReferenceNode) expressionNode, environment);
        } else if (expressionNode instanceof NumberNode) {
            result = resolve((NumberNode) expressionNode);
        } else if (expressionNode instanceof BooleanNode) {
            result = resolve((BooleanNode) expressionNode);
        } else if (expressionNode instanceof OrClauseNode) {
            result = resolve((OrClauseNode) expressionNode, environment);
        } else if (expressionNode instanceof AndClauseNode) {
            result = resolve((AndClauseNode) expressionNode, environment);
        } else if (expressionNode instanceof EqualityExpressionNode) {
            result = resolve((EqualityExpressionNode) expressionNode, environment);
        } else if (expressionNode instanceof EqualityOperandNode) {
            result = resolve((EqualityOperandNode) expressionNode, environment);
        } else if (expressionNode instanceof AdditiveExpressionNode) {
            result = resolve((AdditiveExpressionNode) expressionNode, environment);
        } else if (expressionNode instanceof MultiplicativeExpressionNode) {
            result = resolve((MultiplicativeExpressionNode) expressionNode, environment);
        } else if (expressionNode instanceof UnaryExpressionNode) {
            result = resolve((UnaryExpressionNode) expressionNode, environment);
        } else {
            throw new InterpreterException("Type of ExpressionNode does not adhere to valid types [VariableReferenceNode, NumberNode, AdditiveExpressionNode, MultiplicativeExpressionNode, UnaryExpressionNode]");
        }
        return result;
    }

    /**
     * Resolves an or-expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the or-expression represented using the AST type <code>OrClauseNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final OrClauseNode node, final Environment environment) {
        int result = resolve(node.andClause(), environment);
        if (result != 1) { // only evaluate nextOrClause if we have to
            result = resolve(node.nextOrClause(), environment);
        }
        return result;
    }

    /**
     * Resolves and and-expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the and-expression represented using the AST type <code>AndClauseNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final AndClauseNode node, final Environment environment) {
        int result = resolve(node.equalityExpression(), environment);
        if (result != 0) { // only evaluate nextAndClause if we have to
            result = resolve(node.nextAndClause(), environment);
        }
        return result;
    }

    /**
     * Resolves an equality expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the equality expression represented using the AST type <code>EqualityExpressionNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final EqualityExpressionNode node, final Environment environment) {
        final int leftHandSide = resolve(node.equalityOperand(), environment);
        final int rightHandSide = resolve(node.nextEqualityOperand(), environment);
        return leftHandSide == rightHandSide ? 1 : 0;
    }

    /**
     * Resolves an equality operand to its <code>Integer</code>-based result.
     *
     * @param node
     *      the equality operand represented using the AST type <code>EqualityOperandNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @throws InterpreterException
     *      in case the equality operand is neither '<' nor '>'
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final EqualityOperandNode node, final Environment environment) {
        int result;
        final int leftHandSide = resolve(node.additiveExpression(), environment);
        final int rightHandSide = resolve(node.nextAdditiveExpression(), environment);
        if (node.operator() == '>') {
            result = leftHandSide > rightHandSide ? 1 : 0;
        } else if (node.operator() == '<') {
            result = leftHandSide < rightHandSide ? 1 : 0;
        } else {
            throw new InterpreterException("Expected a '<' or '>' operator in EqualityOperandNode");
        }
        return result;
    }

    /**
     * Resolves an additive expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the additive expression represented using the AST type
     *      <code>AdditiveExpressionNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @throws InterpreterException
     *      in case the additive expression uses neither '+' or '-' operator
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final AdditiveExpressionNode node, final Environment environment) {
        int result;
        if (node.operator() == '+') {
            result = resolve(node.addend(), environment) + resolve(node.augend(), environment);
        } else if (node.operator() == '-') {
            result = resolve(node.addend(), environment) - resolve(node.augend(), environment);
        } else {
            throw new InterpreterException("Expected a '+' or '-' operator in AdditiveNode");
        }
        return result;
    }

    /**
     * Resolves a multiplicative expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the multiplicative expression represented using the AST type
     *      <code>MultiplicativeExpressionNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @throws InterpreterException
     *      in case the multiplicative expression does not use any of the following operators:
     *      '*', '/', '^'
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final MultiplicativeExpressionNode node, final Environment environment) {
        int result;
        if (node.operator() == '*') {
            result = resolve(node.leftFactor(), environment) * resolve(node.rightFactor(), environment);
        } else if (node.operator() == '/') {
            result = resolve(node.leftFactor(), environment) / resolve(node.rightFactor(), environment);
        } else if (node.operator() == '^') {
            result = (int) Math.pow(resolve(node.leftFactor(), environment), resolve(node.rightFactor(), environment));
        } else {
            throw new InterpreterException("Expected a '*', '/' or '^' operator in MultiplicativeNode");
        }
        return result;
    }

    /**
     * Resolves an unary expression to its <code>Integer</code>-based result.
     *
     * @param node
     *      the unary expression represented using the AST type
     *      <code>UnaryExpressionNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final UnaryExpressionNode node, final Environment environment) {
        int result;
        if (node.negate()) {
            result = -resolve(node.expression(), environment);
        } else {
            result = resolve(node.expression(), environment);
        }
        return result;
    }

    /**
     * Resolves a variable reference to its variable value with the respect to the
     * scoping information provided by <code>Environment</code>.
     *
     * @param node
     *      the variable reference represented using the AST type <code>VariableReferenceNode</code>
     * @param environment
     *      holds scoping and function / value binding information
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final VariableReferenceNode node, final Environment environment) {
        return environment.lookupVariable(node.variableName());
    }

    /**
     * Resolves a number to its value.
     *
     * @param node
     *      the number represented using the AST type <code>NumberNode</code>
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final NumberNode node) {
        return node.value();
    }

    /**
     * Resolves a boolean to its internal <code>Integer</code>-based value.
     *
     * @param node
     *      the boolean represented using the AST type <code>BooleanNode</code>
     * @return
     *      <code>Integer</code>-based value which represents the
     *      result of the expression
     */
    public static Integer resolve(final BooleanNode node) {
        return node.value() ? 1 : 0;
    }
}