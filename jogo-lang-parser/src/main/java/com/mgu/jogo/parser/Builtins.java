package com.mgu.jogo.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

/**
 * Defines all built-in procedures this LOGO implementation
 * is able to understand.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public enum Builtins {

    HOME(Arrays.asList("home"), 0),

    PENUP(Arrays.asList("penup", "pu"), 0),

    PENDOWN(Arrays.asList("pendown", "pd"), 0),

    CLEAN(Arrays.asList("clean", "cg"), 0),

    HIDE(Arrays.asList("hideturtle", "ht"), 0),

    SHOW(Arrays.asList("showturtle", "st"), 0),

    SETCOLOR(Arrays.asList("setc", "setpc", "setpencolor"), 1),

    FORWARD(Arrays.asList("forward", "fd"), 1),

    BACK(Arrays.asList("back", "bk"), 1),

    LEFT(Arrays.asList("left", "lt"), 1),

    RIGHT(Arrays.asList("right", "rt"), 1);

    private final List<String> aliases;

    private final int numberOfArguments;

    private Builtins(final List<String> aliases, final int numberOfArguments) {
        this.aliases = aliases;
        this.numberOfArguments = numberOfArguments;
    }

    public List<String> aliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    private boolean knownAs(final String functionName) {
        return this.aliases.contains(functionName);
    }

    public static boolean isBuiltin(final String functionName) {
        return Arrays
                .stream(Builtins.values())
                .anyMatch(builtin -> builtin.knownAs(functionName));
    }

    public static int arity(final String functionName) {
        final OptionalInt arity = Arrays
                .stream(Builtins.values())
                .filter(builtin -> builtin.knownAs(functionName))
                .mapToInt(builtin -> builtin.numberOfArguments)
                .findFirst();
        if (!arity.isPresent()) {
            throw new ParserException("Unable to identify arity of built-in function " + functionName + ".");
        }
        return arity.getAsInt();
    }
}
