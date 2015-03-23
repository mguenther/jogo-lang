package com.mgu.jogo.interpreter;

import com.mgu.jogo.parser.Builtins;
import com.mgu.jogo.parser.ast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Container for built-in procedures which provides the means to execute these
 * procedures within the context of an <code>Environment</code>.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Procedures {

    private static final Map<String, Consumer<Turtle>> NO_ARGS = new HashMap<>();

    private static final Map<String, BiConsumer<Turtle, Integer>> SINGLE_ARG = new HashMap<>();

    static {
        install(Builtins.HOME, turtle -> turtle.move(0, 0));
        install(Builtins.PENUP, turtle -> turtle.penUp());
        install(Builtins.PENDOWN, turtle -> turtle.penDown());
        install(Builtins.HIDE, turtle -> turtle.hide());
        install(Builtins.SHOW, turtle -> turtle.show());
        install(Builtins.SETCOLOR, (turtle, color) -> turtle.setPenColor(color));
        install(Builtins.FORWARD, (turtle, steps) -> turtle.forward(steps));
        install(Builtins.BACK, (turtle, steps) -> turtle.back(steps));
        install(Builtins.LEFT, (turtle, degrees) -> turtle.left(degrees));
        install(Builtins.RIGHT, (turtle, degrees) -> turtle.right(degrees));
    }

    private static void install(final Builtins builtin, Consumer<Turtle> noArgProcedure) {
        builtin.aliases().forEach(functionAlias -> NO_ARGS.put(functionAlias, noArgProcedure));
    }

    private static void install(final Builtins builtin, BiConsumer<Turtle, Integer> singleArgProcedure) {
        builtin.aliases().forEach(functionAlias -> SINGLE_ARG.put(functionAlias, singleArgProcedure));
    }

    /**
     * Calls the builtin-procedure reference by the given <code>FunctionCallNode</code> within
     * the context of the given <code>Environment</code>. <code>FunctionCallNode</code> contains
     * all data required to resolve the parameters to that function (if any).
     *
     * @param functionCallNode
     *      AST type which represents the function call of the builtin-procedure along
     *      with its arguments
     * @throws InterpreterException
     *      in case the <code>FunctionCallNode/code> references a function that is not
     *      a builtin
     * @param environment
     *      current execution context
     */
    public static void call(final FunctionCallNode functionCallNode, final Environment environment) {
        if (functionCallNode.arity() == 0) {
            callWithNoArgs(functionCallNode, environment);
        } else if (functionCallNode.arity() == 1) {
            callWithSingleArg(functionCallNode, environment);
        } else {
            throw new InterpreterException("Unable to dispatch to the correct built-in procedure.");
        }
    }

    private static void callWithNoArgs(final FunctionCallNode functionCallNode, final Environment environment) {
        final Turtle turtle = environment.getTurtle();
        final String functionName = functionCallNode.functionName();
        final Consumer<Turtle> function = NO_ARGS.get(functionName);
        function.accept(turtle);
    }

    private static void callWithSingleArg(final FunctionCallNode functionCallNode, final Environment environment) {
        final Turtle turtle = environment.getTurtle();
        final String functionName = functionCallNode.functionName();
        final BiConsumer<Turtle, Integer> function = SINGLE_ARG.get(functionName);
        final Integer argumentValue = ExpressionResolver.resolve(functionCallNode.arguments().get(0), environment);
        function.accept(turtle, argumentValue);
    }
}