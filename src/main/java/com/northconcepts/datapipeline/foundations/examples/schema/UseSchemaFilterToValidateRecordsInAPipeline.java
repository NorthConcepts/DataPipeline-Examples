package com.northconcepts.datapipeline.foundations.examples.schema;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FieldFilter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.filter.rule.IsJavaType;
import com.northconcepts.datapipeline.filter.rule.IsNotNull;
import com.northconcepts.datapipeline.filter.rule.ValueMatch;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaFilter;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class UseSchemaFilterToValidateRecordsInAPipeline {

    public static void main(String[] args) {
        
        EntityDef entityDef = new EntityDef().setName("Jewelry")
                .addField(new NumericFieldDef("Variant Price", FieldType.DOUBLE))
                .addField(new BooleanFieldDef("Variant Taxable", FieldType.BOOLEAN))
                .addField(new TextFieldDef("Title", FieldType.STRING))
                .addField(new TextFieldDef("Option1 Value", FieldType.STRING));
        
        SchemaFilter schemaFilter = new SchemaFilter(entityDef);
                
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("Variant Price").stringToDouble(),
                    new BasicFieldTransformer("Variant Taxable").stringToBoolean())
                .add(new SelectFields("Variant Price", "Variant Taxable", "Title", "Option1 Value"));
        
        reader = new FilteringReader(reader)
                
                .add(new FieldFilter("Variant Price")
                    .addRule(new IsNotNull())
                    .addRule(new IsJavaType(Double.class)))
                
                .add(new FilterExpression("${Variant Price} >= 5 && ${Variant Price} <= 50"))
        
                .add(new FieldFilter("Option1 Value")
                        .addRule(new IsJavaType(String.class))
                        .addRule(new ValueMatch<String>("Blue", "Black", "Gold", "Silver", "Purple")))
                
                .add(schemaFilter);
        
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
    
}
