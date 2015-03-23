package com.mgu.jogo.parser;

/**
 * Exception type that is raised when the {@link Lexer} is
 * unable to recognize symbols of the implemented grammar / alphabet and
 * thus unable to transform a stream of incoming characters into a stream
 * of outgoing tokens.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class LexerException extends RuntimeException {

    public LexerException() {
        super();
    }

    public LexerException(final String message) {
        super(message);
    }
}
