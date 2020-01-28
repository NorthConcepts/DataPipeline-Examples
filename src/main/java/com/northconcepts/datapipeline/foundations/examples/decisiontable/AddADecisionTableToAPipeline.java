package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableCondition;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableOutcome;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableReader;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddADecisionTableToAPipeline {

    public static void main(String[] args) {

        DecisionTable table = new DecisionTable()
                .addField(new CalculatedField("Variant Price", "toBigDecimal(${Variant Price})"))
                
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Variant Price", "? == null || ? < 20"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "0.00"))
                        .addOutcome(new DecisionTableOutcome("Total", "${Variant Price} + Shipping"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Variant Price", "? < 50"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "5.00"))
                        .addOutcome(new DecisionTableOutcome("Total", "${Variant Price} + Shipping"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Variant Price", "? < 100"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "7.00"))
                        .addOutcome(new DecisionTableOutcome("Total", "${Variant Price} + Shipping"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Variant Price", "? >= 100"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "${Variant Price} * 0.10"))
                        .addOutcome(new DecisionTableOutcome("Total", "${Variant Price} + Shipping"))
                        )
                ;
        
        
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);
        
        reader = new DecisionTableReader(reader, table);
        
        reader = new TransformingReader(reader).add(new SelectFields("Title", "Handle", "Variant Price", "Shipping", "Total"));
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
        
    }

}
