package com.mgu.jogo.interpreter;

import com.mgu.jogo.parser.ast.FunctionDefinitionNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Hierarchical data structure which represents the current execution context. Provides
 * means to lookup variable bindings and function definitions in a recursive manner
 * (until it hits the so called global space, which is the root environment any interpreter
 * starts off with).
 *
 * The typing system only allows integer-based variables.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Environment {

    private final Map<String, FunctionDefinitionNode> userDefinedFunctions = new HashMap<>();

    private final Map<String, Integer> variables = new HashMap<>();

    private final Turtle turtle;

    private Environment parent = null;

    /**
     * Constructor which builds the root environment (no parent).
     *
     * @param turtle
     *      Instance of <code>Turtle</code> which is the target for built-in Turtle operations
     */
    public Environment(final Turtle turtle) {
        this(turtle, null);
    }

    /**
     * Constructor which builds a child environment with reference to its parent.
     *
     * @param turtle
     *      Instance of <code>Turtle</code> which is the target for built-in Turtle operations
     * @param parent
     *      The parent environment for this <code>Environment</code>
     */
    public Environment(final Turtle turtle, final Environment parent) {
        this.turtle = turtle;
        this.parent = parent;
    }

    /**
     * @return
     *      Yields the <code>Turtle</code> instance
     */
    public Turtle getTurtle() {
        return this.turtle;
    }

    /**
     * Looks up a function by its function name. The lookup mechanism works recursively,
     * meaning that the lookup will walk consecutively along the hierarchy until it hits
     * the global space (root environment) if the function is not defined in the current,
     * local environment.
     *
     * @param functionName
     *      <code>String</code> referring to a function
     * @throws InterpreterException
     *      in case there is no function defined for the given function name
     * @return
     *      the <code>FunctionDefinitionNode</code> which holds all metadata
     *      of the function
     */
    public FunctionDefinitionNode lookupFunction(final String functionName) {
        if (isLocalFunction(functionName)) {
            return this.userDefinedFunctions.get(functionName);
        } else {
            if (this.parent != null) {
                return this.parent.lookupFunction(functionName);
            } else {
                throw new InterpreterException("Function " + functionName + " is not defined.");
            }
        }
    }

    private boolean isLocalFunction(final String functionName) {
        return this.userDefinedFunctions.containsKey(functionName);
    }

    /**
     * Looks up the variable value bound the given variable name. The lookup mechanism works
     * recursively, meaning that the lookup will walk consecutively along the hierarchy
     * until it hits the global space (root environment) if the function is not defined in
     * the current, local environment.
     *
     * @param variableName
     *      <code>String</code> referring to a variable
     * @throws InterpreterException
     *      in case there is no value binding for the given variable name
     * @return
     *      Integer-value representing the value bound to the variable with respect
     *      to its current environment
     */
    public Integer lookupVariable(final String variableName) {
        if (isLocalVariable(variableName)) {
            return this.variables.get(variableName);
        } else {
            if (this.parent != null) {
                return this.parent.lookupVariable(variableName);
            } else {
                throw new InterpreterException("Variable " + variableName + " is not defined.");
            }
        }
    }

    private boolean isLocalVariable(final String variableName) {
        return this.variables.containsKey(variableName);
    }

    /**
     * Defines the function represented by the given <code>FunctionDefinitionNode</code>
     * and binds it to the current environment.
     *
     * @param function
     *      AST representation of the function that ought to be defined within the
     *      current environment
     * @throw InterpreterException
     *      if a function with the same function name is already bound to the
     *      current environment
     */
    public void install(final FunctionDefinitionNode function) {
        final String functionName = function.functionName();
        if (isLocalFunction(functionName)) {
            throw new InterpreterException("Unable to override already existing function definition for function " + functionName);
        }
        this.userDefinedFunctions.put(functionName, function);
    }

    /**
     * Binds the <code>Integer</code>-based value to the given variable name with
     * respect to the current environment.
     *
     * @param variableName
     *      Name of the variable
     * @param variableValue
     *      <code>Integer</code>-based value associated with this variable with
     *      respect to the current environment
     */
    public void install(final String variableName, final Integer variableValue) {
        this.variables.put(variableName, variableValue);
    }

    /**
     * @return
     *      Yields the child <code>Environment</code> based off of the current
     *      <code>Environment</code>
     */
    public Environment createChildEnvironment() {
        return new Environment(this.turtle, this);
    }
}