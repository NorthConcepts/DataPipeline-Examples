package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class ParseAnExpression {

    public static void main(String[] args) {
        Expression expression = Parser.parseExpression("(a + b) * 2");
                
        DefaultExpressionContext expressionContext = new DefaultExpressionContext();
        expressionContext.setValue("a", 2);
        expressionContext.setValue("b", 2);
        
        System.out.println(expression.getSourceString() + " = " + expression.evaluate(expressionContext));
    }
}
