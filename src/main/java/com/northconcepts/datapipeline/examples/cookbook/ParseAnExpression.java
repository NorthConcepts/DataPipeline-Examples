package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class ParseAnExpression {

    public static void main(String[] args) {
        Expression expression = Parser.parseExpression("2 + 2");
                
        DefaultExpressionContext expressionContext = new DefaultExpressionContext();
        Long evaluatedValue = (Long) expression.evaluate(expressionContext);
        System.out.println("2 + 2 = " + evaluatedValue);
        
        // Learn more about expression language at: https://northconcepts.com/docs/expression-language
    }
}
