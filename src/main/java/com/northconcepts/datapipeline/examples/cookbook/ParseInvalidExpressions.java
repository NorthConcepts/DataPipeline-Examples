package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.InvalidSyntax;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class ParseInvalidExpressions {

    public static void main(String[] args) {
        parseAnInvalidExpression();
        parseInvalidExpressions();
    }

    private static void parseAnInvalidExpression() {
        System.out.println("=========================== Parse An Invalid Expression =======================================");
        // Parse an invalid expression which is missing closing parenthesis

        Expression expression = Parser.parseExpression("toInt('123'", true);

        InvalidSyntax invalidSyntax = (InvalidSyntax) expression;
        System.out.println("Source Expression: " + invalidSyntax.getSourceString());
        System.out.println("Expression Parse Exception: " + invalidSyntax.getParseException().getMessage());

        try {
            System.out.println(expression.evaluate(null));
        } catch (Throwable e) {
            System.err.println("Exception while evaluating invalid expression: " + e.getMessage());
        }
    }

    private static void parseInvalidExpressions() {
        System.out.println("=========================== Parse Multiple Invalid Expression =================================");

        Parser.doWithoutParseException(() -> {
            Expression expression1 = Parser.parseExpression("toInt('123'");
            InvalidSyntax invalidSyntax1 = (InvalidSyntax) expression1;
            System.out.println("Source Expression - 1: " + invalidSyntax1.getSourceString());
            System.out.println("Expression Parse Exception - 1: " + invalidSyntax1.getParseException().getMessage());
            
            try {
                System.out.println(expression1.evaluate(null));
            } catch (Throwable e) {
                System.err.println("Exception while evaluating invalid expression: " + e.getMessage());
            }
            
            System.out.println("=============================================================================");
            
            Expression expression2 = Parser.parseExpression("toInt('345'");
            InvalidSyntax invalidSyntax2 = (InvalidSyntax) expression2;
            System.out.println("Source Expression - 2: " + invalidSyntax2.getSourceString());
            System.out.println("Expression Parse Exception - 2: " + invalidSyntax2.getParseException().getMessage());
            
            try {
                System.out.println(expression2.evaluate(null));
            } catch (Throwable e) {
                System.err.println("Exception while evaluating invalid expression: " + e.getMessage());
            }
        });
    }
}
