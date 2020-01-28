package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableCondition;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableOutcome;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableResult;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class ExecuteAnActionInADecisionTable {
	
	public static void action1() {
		System.out.println("Action 1");
	}

	public static double action2(int age, double income) {
		System.out.println("Age: " + age + ";  Income: " + income);
		return age * income;
	}

    public static void main(String[] args) {
    	Functions.add("action2", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action2");
    	
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("House Owned", true);
        input.setValue("Income", 1000.0);
        
        DecisionTable table = new DecisionTable()
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Age", "? >= 40"))
                        .addCondition(new DecisionTableCondition("House Owned", "? == true"))
                        .addOutcome(new DecisionTableOutcome("Eligible", "true"))
                        .addOutcome(new DecisionTableOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action1()"))
                        .addOutcome(new DecisionTableOutcome("Action2", "action2(Age, Income)"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Age", "? >= 40"))
                        .addCondition(new DecisionTableCondition("House Owned", "? == false"))
                        .addCondition(new DecisionTableCondition("Income", "? >= 2000"))
                        .addOutcome(new DecisionTableOutcome("Eligible", "true"))
                        .addOutcome(new DecisionTableOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action1()"))
                        .addOutcome(new DecisionTableOutcome("Action2", "action2(Age, Income)"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Age", "? >= 40"))
                        .addCondition(new DecisionTableCondition("House Owned", "? == false"))
                        .addCondition(new DecisionTableCondition("Income", "? < 2000")) 
                        .addOutcome(new DecisionTableOutcome("Eligible", "false"))
                        .addOutcome(new DecisionTableOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action1()"))
                        .addOutcome(new DecisionTableOutcome("Action2", "action2(Age, Income)"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Age", "? < 40"))
                        .addCondition(new DecisionTableCondition("Income", "? >= 3000"))
                        .addOutcome(new DecisionTableOutcome("Eligible", "true"))
                        .addOutcome(new DecisionTableOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action1()"))
                        .addOutcome(new DecisionTableOutcome("Action2", "action2(Age, Income)"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Age", "? < 40"))
                        .addCondition(new DecisionTableCondition("Income", "? < 3000"))
                        .addOutcome(new DecisionTableOutcome("Eligible", "false"))
                        .addOutcome(new DecisionTableOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontable.ExecuteAnActionInADecisionTable.action1()"))
                        .addOutcome(new DecisionTableOutcome("Action2", "action2(Age, Income)"))
                        )
                ;
        
        DecisionTableResult result = table.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }

}
