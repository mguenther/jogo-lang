package com.mgu.jogo.interpreter;

/**
 * Exception type that is raised when anything goes wrong while the program code
 * is being interpreted.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class InterpreterException extends RuntimeException {

    public InterpreterException() {
        super();
    }

    public InterpreterException(final String message) {
        super(message);
    }
}
