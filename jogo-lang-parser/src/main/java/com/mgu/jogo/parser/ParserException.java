package com.mgu.jogo.parser;

/**
 * Exception type that is raised when the {@link Parser}
 * is unable to match tokens against the implemented grammar.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class ParserException extends RuntimeException {

    public ParserException() {
        super();
    }

    public ParserException(final String message) {
        super(message);
    }
}
