package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaFilter;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.validate.ValidatingReader;

public class ValidateRootRecordUsingSchema {

    public static void main(String[] args) {
        EntityDef entityDef =  new EntityDef("customers")
                .addField(new NumericFieldDef("customer_id", FieldType.LONG))
                .addField(new TextFieldDef("customer_name", FieldType.STRING))
                .addField(new TextFieldDef("address", FieldType.STRING))
                .addField(new TextFieldDef("city", FieldType.STRING))
                .addField(new TextFieldDef("country", FieldType.STRING));

        entityDef.addValidation(new FilterExpression("recordContainsNonNullValue(this, 'customer_id')"))
                .addValidation(new FilterExpression("recordContainsNonNullField(this, 'customer_name')"))
                .addValidation(new FilterExpression("getValue(this, 'customer_name', 'no name') != 'Lord Voldemort'"))
                .addValidation(new FilterExpression("recordContainsValue(this, 'address')"))
                .addValidation(new FilterExpression("!recordContainsField(this, 'zipcode')"));

        RecordList records = new RecordList()
                .add(new Record()
                        .setField("customer_id", 1000L)
                        .setField("customer_name", "Harry Potter")
                        .setField("address", "4 Privet Drive")
                        .setField("city", "Hogwarts")
                        .setField("country", "Scotland"))
                .add(new Record()
                        .setField("customer_id", null)
                        .setField("customer_name", "Lord Voldemort")
                        .setField("city", "Burrow")
                        .setField("country", "England")
                        .setField("zipcode", "00000"));


        DataReader reader = new MemoryReader(records);
        reader = new ValidatingReader(reader).add(new SchemaFilter(entityDef));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }

}
