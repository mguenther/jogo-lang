package com.mgu.jogo.interpreter;

import com.mgu.jogo.parser.Parser;
import com.mgu.jogo.parser.ast.ProgramNode;

/**
 * Tree-based interpreter which utilizes <code>Environment</code> as parametric type
 * for <code>ContextualTreeVisitor</code> (cf. <code>EvaluatingTreeVisitor</code>) to
 * provide the root execution context (global space) for the interpreter.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Interpreter {

    /**
     * Parses the given program and interprets it using a tree-based interpreter.
     *
     * @param turtle
     *      Implementation of <code>Turtle</code>
     * @param program
     *      The LOGO program to interpret in <code>String</code> representation
     */
    public void run(final Turtle turtle, final String program) {
        final Parser parser = new Parser(program);
        final ProgramNode programNode = parser.parse();
        final EvaluatingTreeVisitor visitor = new EvaluatingTreeVisitor();
        final Environment rootEnvironment = new Environment(turtle);
        visitor.visit(programNode, rootEnvironment);
    }
}