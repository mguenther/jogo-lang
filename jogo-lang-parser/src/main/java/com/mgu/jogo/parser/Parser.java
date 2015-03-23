package com.mgu.jogo.parser;

import com.mgu.jogo.parser.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for a simple LOGO-like language. The parser implements the grammar shown
 * by the EBNF underneath. Due to the simplicity of the forms to parse, all parsing logic is
 * centralized within this class.
 *
 * PROGRAM             := STATEMENTS
 * STATEMENTS          := STATEMENT
 *                      | STATEMENT STATEMENTS
 * STATEMENT           := BUILT-IN <VARARGS>
 *                      | SPECIAL-FORM
 *                      | FUNCTION-CALL <VARARGS>
 *                      | ASSIGNMENT
 * BUILT-IN            := forward EXPRESSION
 *                      | fd EXPRESSION
 *                      | back EXPRESSION
 *                      | bk EXPRESSION
 *                      | left EXPRESSION
 *                      | lt EXPRESSION
 *                      | right EXPRESSION
 *                      | rt EXPRESSION
 *                      | home
 *                      | penup
 *                      | pu
 *                      | pendown
 *                      | pd
 *                      | clean
 *                      | cg
 *                      | setc EXPRESSION
 *                      | setpc EXPRESSION
 *                      | setpencolor EXPRESSION
 *                      | hideturtle
 *                      | ht
 *                      | showturtle
 *                      | st
 * SPECIAL-FORM        := FUNCTION-DEFINITION
 *                      | CONTROL-STRUCTURE
 * FUNCTION-DEFINITION := to ARG-DEF STATEMENTS end
 * ARG-DEF             := :ARG-NAME
 *                      | :ARG-NAME ARG-DEF
 * CONTROL-STRUCTURE   := repeat EXPRESSION [ STATEMENTS ]
 *                      | if EXPRESSION [ STATEMENTS ]
 *                      | ifelse EXPRESSION [ STATEMENTS.true ] [ STATEMENTS.false ]
 * FUNCTION-CALL       := FUNCTION-NAME <VARARGS>
 * FUNCTION-NAME       := LITERAL
 * EXPRESSION          := OR-CLAUSE
 * OR-CLAUSE           := AND-CLAUSE
 *                      | AND-CLAUSE or OR-CLAUSE
 * AND-CLAUSE          := EQUALITY-EXPRESSION
 *                      | EQUALITY-EXPRESSION and AND-CLAUSE
 * EQUALITY-EXPRESSION := EQUALITY-OPERAND
 *                      | EQUALITY-OPERAND == EQUALITY-OPERAND
 * EQUALITY-OPERAND    := ADDITIVE-EXPRESSION
 *                      | ADDITIVE-EXPRESSION < ADDITIVE-EXPRESSION
 *                      | ADDITIVE-EXPRESSION > ADDITIVE-EXPRESSION
 * ADDITIVE-EXPRESSION := MULTIPLICATIVE-EXPRESSION
 *                      | MULTIPLICATIVE-EXPRESSION + ADDITIVE-EXPRESSION
 *                      | MULTIPLICATIVE-EXPRESSION - ADDITIVE-EXPRESSION
 * MULTIPLICATIVE-EXPR := UNARY-EXPRESSION * MULTIPLICATIVE-EXPRESSION
 *                      | UNARY-EXPRESSION / MULTIPLICATIVE-EXPRESSION
 *                      | UNARY-EXPRESSION ^ MULTIPLICATIVE-EXPRESSION
 *                      | UNARY-EXPRESSION
 * UNARY-EXPRESSION    := SIMPLE-EXPRESSION
 *                      | - SIMPLE-EXPRESSION
 * SIMPLE-EXPRESSION   := NUMBER
 *                      | VARIABLE
 *                      | BOOLEAN
 *                      | ( EXPRESSION )
 * NUMBER              := [0-9]+
 * VARIABLE            := LITERAL
 * LITERAL             := [A-Za-z][A-Za-z0-9]*
 * BOOLEAN             := true
 *                      | false
 * ASSIGNMENT          := VARIABLE = EXPRESSION
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public class Parser {

    /**
     * Provides individual tokens.
     */
    private final Lexer lexer;

    /**
     * User-defined functions will be collected during program parsing using this
     * map.
     */
    private final Map<String, Integer> userDefinedFunctions = new HashMap<>();

    /**
     * 2-token-wide look-ahead
     */
    private final Token[] lookahead = new Token[2];

    public Parser(final String program) {
        this.lexer = new Lexer(program);
        this.lookahead[0] = this.lexer.nextToken();
        this.lookahead[1] = this.lexer.nextToken();
    }

    private void consume() {
        this.lookahead[0] = this.lookahead[1];
        this.lookahead[1] = this.lexer.nextToken();
    }

    private Token lookahead(final int index) {
        if (index > this.lookahead.length) {
            throw new IllegalArgumentException("Only " + this.lookahead.length + "-wide lookaheads are supported.");
        }
        return this.lookahead[index-1];
    }

    /**
     * Parses the given LOGO program into an abstract syntax tree rooted
     * at <code>ProgramNode</code>.
     *
     * @throws ParserException
     *      if the given LOGO program is not parseable due to the following
     *      conditions:
     *      <ul>
     *          <li>The parser is given an empty program listing.</li>
     *          <li>The parser is unable to parse the whole program (there are
     *              remaining tokens).</li>
     *          <li>The parser encounters tokens that do not comply with
     *              the defined grammar.</li>
     *      </ul>
     * @return
     *      <code>ProgramNode</code> which represents the root node of the
     *      parsed program
     */
    public ProgramNode parse() {
        if (isEof()) {
           throw new ParserException("Program does not have any statements.");
        }

        final List<StatementNode> statements = parseStatements();

        if (tokensLeft()) {
            throw new ParserException("Unable to parse the whole program.");
        }

        return new ProgramNode(statements);
    }

    private boolean isEof() {
        return this.lookahead(1).equals(Token.TOKEN_EOF);
    }

    private List<StatementNode> parseStatements() {
        final List<StatementNode> statementNodes = new ArrayList<>();
        while (isCharacters()) {
            final StatementNode statementNode = parseStatement();
            statementNodes.add(statementNode);
        }
        return statementNodes;
    }

    private boolean isCharacters() {
        return this.lookahead(1).type().equals(Token.TokenType.CHARACTERS);
    }

    private boolean isNumber() {
        return this.lookahead(1).type().equals(Token.TokenType.NUMBER);
    }

    private StatementNode parseStatement() {
        if (isBuiltin()) {
            return parseBuiltin();
        } else if (isSpecialForm()) {
            return parseSpecialForm();
        } else if (isUserDefinedFunction()) {
            return parseFunctionCall();
        } else if (isAssignment()) {
            return parseAssignment();
        } else {
            throw new ParserException("Unable to parse statement.");
        }
    }

    private boolean isBuiltin() {
        final String functionName = lookahead(1).value();
        return Builtins.isBuiltin(functionName);
    }

    private StatementNode parseBuiltin() {
        final int arity = Builtins.arity(lookahead(1).value());
        final Token tokenFunction = match(Token.TokenType.CHARACTERS);
        final List<ExpressionNode> argumentNodes = parseArguments(arity);
        return new FunctionCallNode(tokenFunction.value(), arity, true, argumentNodes);
    }

    private List<ExpressionNode> parseArguments(final int arity) {
        final List<ExpressionNode> arguments = new ArrayList<>();
        for (int i = 0; i < arity; i++) {
            arguments.add(parseExpression());
        }
        return arguments;
    }

    private ExpressionNode parseVariableReference() {
        final Token matchedToken = match(Token.TokenType.CHARACTERS);
        return new VariableReferenceNode(matchedToken.value());
    }

    private ExpressionNode parseNumber() {
        final Token matchedToken = match(Token.TokenType.NUMBER);
        final int number = Integer.valueOf(matchedToken.value());
        return new NumberNode(number);
    }

    private Token match(final Token.TokenType expectedTokenType) {
        if (lookahead(1).matchesType(expectedTokenType)) {
            final Token matchedToken = lookahead(1);
            consume();
            return matchedToken;
        } else {
            throw new ParserException("Expected token of type " + expectedTokenType + " but got " + lookahead(1));
        }
    }

    private Token match(final Token.TokenType expectedTokenType, final String expectedValue) {
        if (lookahead(1).matches(expectedTokenType, expectedValue)) {
            final Token matchedToken = this.lookahead(1);
            consume();
            return matchedToken;
        } else {
            throw new ParserException("Expected token of type " + expectedTokenType + " and value \"" + expectedValue + "\" but got " + this.lookahead(1));
        }
    }

    private boolean isSpecialForm() {
        return isFunctionDefinition() || isRepeat() || isIf() || isIfElse();
    }

    private StatementNode parseSpecialForm() {
        if (isFunctionDefinition()) {
            return parseFunctionDefinition();
        } else if (isRepeat()) {
            return parseRepeat();
        } else if (isIf()) {
            return parseIf();
        } else if (isIfElse()) {
            return parseIfElse();
        } else {
            throw new ParserException("Expected either special form \"to\" or \"repeat\"");
        }
    }

    private boolean isFunctionDefinition() {
        return lookahead(1).value().equalsIgnoreCase("to");
    }

    private boolean isRepeat() {
        return lookahead(1).value().equalsIgnoreCase("repeat");
    }

    private boolean isIf() {
        return lookahead(1).value().equalsIgnoreCase("if");
    }

    private boolean isIfElse() {
        return lookahead(1).value().equalsIgnoreCase("ifelse");
    }

    private StatementNode parseRepeat() {
        match(Token.TokenType.CHARACTERS, "repeat");
        final ExpressionNode times = isCharacters() ? parseVariableReference() : parseNumber();
        match(Token.TokenType.BRACKET_LEFT);
        final List<StatementNode> statements = parseStatements();
        match(Token.TokenType.BRACKET_RIGHT);
        return new RepeatNode(times, statements);
    }

    private StatementNode parseIf() {
        match(Token.TokenType.CHARACTERS, "if");
        final ExpressionNode condition = parseExpression();
        match(Token.TokenType.BRACKET_LEFT);
        final List<StatementNode> thenStatements = parseStatements();
        match(Token.TokenType.BRACKET_RIGHT);
        return new IfNode(condition, thenStatements);
    }

    private StatementNode parseIfElse() {
        match(Token.TokenType.CHARACTERS, "ifelse");
        final ExpressionNode condition = parseExpression();
        match(Token.TokenType.BRACKET_LEFT);
        final List<StatementNode> thenStatements = parseStatements();
        match(Token.TokenType.BRACKET_RIGHT);
        match(Token.TokenType.BRACKET_LEFT);
        final List<StatementNode> elseStatements = parseStatements();
        match(Token.TokenType.BRACKET_RIGHT);
        return new IfNode(condition, thenStatements, elseStatements);
    }

    private StatementNode parseFunctionDefinition() {
        match(Token.TokenType.CHARACTERS, "to");
        final Token functionNameToken = match(Token.TokenType.CHARACTERS);
        final String functionName = functionNameToken.value();
        if (this.userDefinedFunctions.containsKey(functionName)) {
            throw new ParserException("Function " + functionName + " has ambiguous definitions.");
        }
        final List<ArgumentDefinitionNode> argumentDefinitionNodes = parseArgumentDefinitions();
        this.userDefinedFunctions.put(functionName, Integer.valueOf(argumentDefinitionNodes.size()));
        final List<StatementNode> statementNodes = parseStatementsUntilEndToken();
        match(Token.TokenType.CHARACTERS, "end");
        return new FunctionDefinitionNode(functionName, argumentDefinitionNodes, statementNodes);
    }

    private List<ArgumentDefinitionNode> parseArgumentDefinitions() {
        final List<ArgumentDefinitionNode> argumentDefinitionNodes = new ArrayList<>();
        while (isColon()) {
            match(Token.TokenType.COLON);
            final Token argumentToken = match(Token.TokenType.CHARACTERS);
            argumentDefinitionNodes.add(new ArgumentDefinitionNode(argumentToken.value()));
        }
        return argumentDefinitionNodes;
    }

    private List<StatementNode> parseStatementsUntilEndToken() {
        final List<StatementNode> statementNodes = new ArrayList<>();
        while (isCharacters() && !isFunctionDefinitionClosing()) {
            statementNodes.add(parseStatement());
        }
        return statementNodes;
    }

    private boolean isFunctionDefinitionClosing() {
        return lookahead(1).matches(Token.TokenType.CHARACTERS, "end");
    }

    private boolean isColon() {
        return lookahead(1).matchesType(Token.TokenType.COLON);
    }

    private boolean isUserDefinedFunction() {
        final String functionName = lookahead(1).value();
        return this.userDefinedFunctions.containsKey(functionName);
    }

    private StatementNode parseFunctionCall() {
        final Token functionToken = match(Token.TokenType.CHARACTERS);
        final String functionName = functionToken.value();
        final int expectedNumberOfArguments = this.userDefinedFunctions.get(functionName);
        final List<ExpressionNode> expressionNodes = new ArrayList<>();
        for (int i = 0; i < expectedNumberOfArguments; i++) {
            expressionNodes.add(parseExpression());
        }
        return new FunctionCallNode(functionName, expectedNumberOfArguments, false, expressionNodes);
    }

    private boolean isAssignment() {
        return lookahead(2).matchesType(Token.TokenType.EQUALS_SIGN);
    }

    private StatementNode parseAssignment() {
        final Token variableToken = match(Token.TokenType.CHARACTERS);
        match(Token.TokenType.EQUALS_SIGN);
        final ExpressionNode expression = parseExpression();
        return new AssignmentNode(variableToken.value(), expression);
    }

    private ExpressionNode parseExpression() {
        return parseOrClause();
    }

    private ExpressionNode parseOrClause() {
        ExpressionNode expression;
        final ExpressionNode andClause = parseAndClause();
        if (lookahead(1).matches(Token.TokenType.CHARACTERS, "or")) {
            match(Token.TokenType.CHARACTERS, "or");
            final ExpressionNode nextOrClause = parseOrClause();
            expression = new OrClauseNode(andClause, nextOrClause);
        } else {
            expression = andClause;
        }
        return expression;
    }

    private ExpressionNode parseAndClause() {
        ExpressionNode expression;
        final ExpressionNode equalityExpression = parseEqualityExpression();
        if (lookahead(1).matches(Token.TokenType.CHARACTERS, "and")) {
            match(Token.TokenType.CHARACTERS, "and");
            final ExpressionNode nextAndClause = parseAndClause();
            expression = new AndClauseNode(equalityExpression, nextAndClause);
        } else {
            expression = equalityExpression;
        }
        return expression;
    }

    private ExpressionNode parseEqualityExpression() {
        ExpressionNode expression;
        final ExpressionNode equalityOperand = parseEqualityOperand();
        if (lookahead(1).matchesType(Token.TokenType.EQUALITY_OPERATOR)) {
            match(Token.TokenType.EQUALITY_OPERATOR);
            final ExpressionNode nextEqualityOperand = parseEqualityOperand();
            expression = new EqualityExpressionNode(equalityOperand, nextEqualityOperand);
        } else {
            expression = equalityOperand;
        }
        return expression;
    }

    private ExpressionNode parseEqualityOperand() {
        ExpressionNode expression;
        final ExpressionNode additiveExpression = parseAdditiveExpression();
        if (lookahead(1).matchesType(Token.TokenType.LARGER_THAN_OPERATOR)) {
            match(Token.TokenType.LARGER_THAN_OPERATOR);
            final ExpressionNode nextAdditiveExpression = parseAdditiveExpression();
            expression = new EqualityOperandNode(additiveExpression, nextAdditiveExpression, '>');
        } else if (lookahead(1).matchesType(Token.TokenType.SMALLER_THAN_OPERATOR)) {
            match(Token.TokenType.SMALLER_THAN_OPERATOR);
            final ExpressionNode nextAdditiveExpression = parseAdditiveExpression();
            expression = new EqualityOperandNode(additiveExpression, nextAdditiveExpression, '<');
        } else {
            expression = additiveExpression;
        }
        return expression;
    }

    private ExpressionNode parseAdditiveExpression() {
        ExpressionNode expression;
        final ExpressionNode multiplicativeExpression = parseMultiplicativeExpression();
        if (lookahead(1).matchesType(Token.TokenType.ADD_OPERATOR)) {
            match(Token.TokenType.ADD_OPERATOR);
            final ExpressionNode nextAdditiveExpression = parseAdditiveExpression();
            expression = new AdditiveExpressionNode(multiplicativeExpression, nextAdditiveExpression, '+');
        } else if (lookahead(1).matchesType(Token.TokenType.MIN_OPERATOR)) {
            match(Token.TokenType.MIN_OPERATOR);
            final ExpressionNode nextAdditiveExpression = parseAdditiveExpression();
            expression = new AdditiveExpressionNode(multiplicativeExpression, nextAdditiveExpression, '-');
        } else {
            expression = multiplicativeExpression;
        }
        return expression;
    }

    private ExpressionNode parseMultiplicativeExpression() {
        ExpressionNode expression;
        final ExpressionNode unaryExpressionNode = parseUnaryExpression();
        if (lookahead(1).matchesType(Token.TokenType.MUL_OPERATOR)) {
            match(Token.TokenType.MUL_OPERATOR);
            final ExpressionNode nextMultiplicativeExpression = parseMultiplicativeExpression();
            expression = new MultiplicativeExpressionNode(unaryExpressionNode, nextMultiplicativeExpression, '*');
        } else if (lookahead(1).matchesType(Token.TokenType.DIV_OPERATOR)) {
            match(Token.TokenType.DIV_OPERATOR);
            final ExpressionNode nextMultiplicativeExpression = parseMultiplicativeExpression();
            expression = new MultiplicativeExpressionNode(unaryExpressionNode, nextMultiplicativeExpression, '/');
        } else if (lookahead(1).matchesType(Token.TokenType.POW_OPERATOR)) {
            match(Token.TokenType.POW_OPERATOR);
            final ExpressionNode nextMultiplicativeExpression = parseMultiplicativeExpression();
            expression = new MultiplicativeExpressionNode(unaryExpressionNode, nextMultiplicativeExpression, '^');
        } else {
            expression = unaryExpressionNode;
        }
        return expression;
    }

    private ExpressionNode parseUnaryExpression() {
        boolean negate = false;
        if (lookahead(1).matchesType(Token.TokenType.MIN_OPERATOR)) {
            match(Token.TokenType.MIN_OPERATOR);
            negate = true;
        }
        final ExpressionNode simpleExpression = parseSimpleExpression();
        return new UnaryExpressionNode(simpleExpression, negate);
    }

    private ExpressionNode parseSimpleExpression() {
        ExpressionNode expression;
        if (isBoolean()) {
            expression = parseBoolean();
        } else if (isCharacters()) {
            expression = parseVariableReference();
        } else if (isNumber()) {
            expression = parseNumber();
        } else if (isOpeningBrace()) {
            match(Token.TokenType.BRACE_LEFT);
            expression = parseExpression();
            match(Token.TokenType.BRACE_RIGHT);
        } else {
            throw new ParserException("Unable to parse expression");
        }
        return expression;
    }

    private boolean isBoolean() {
        return isCharacters() && (lookahead(1).value().equals("true") || lookahead(1).value().equals("false"));
    }

    private ExpressionNode parseBoolean() {
        final Token booleanToken = match(Token.TokenType.CHARACTERS);
        final boolean value = booleanToken.value().equals("true");
        return new BooleanNode(value);
    }

    private boolean isOpeningBrace() {
        return lookahead(1).matchesType(Token.TokenType.BRACE_LEFT);
    }

    private boolean tokensLeft() {
        return !isEof();
    }
}
