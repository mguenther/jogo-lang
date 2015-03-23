package com.mgu.jogo.parser.ast;

public class ArgumentDefinitionNode {

    private final String argumentName;

    public ArgumentDefinitionNode(final String argumentName) {
        this.argumentName = argumentName;
    }

    public String argumentName() {
        return this.argumentName;
    }
}