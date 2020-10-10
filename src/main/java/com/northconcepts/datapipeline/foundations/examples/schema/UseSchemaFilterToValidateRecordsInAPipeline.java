package com.northconcepts.datapipeline.foundations.examples.schema;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaFilter;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.validate.ValidatingReader;

public class UseSchemaFilterToValidateRecordsInAPipeline {

    public static void main(String[] args) {

        EntityDef entityDef = new EntityDef().setName("Jewelry")
            .addField(new NumericFieldDef("Variant Price", FieldType.DOUBLE).setMaximum(5).setMaximum(500).setRequired(true))
            .addField(new BooleanFieldDef("Variant Taxable", FieldType.BOOLEAN))
            .addField(new TextFieldDef("Title", FieldType.STRING).setMaximumLength(256))
            .addField(new TextFieldDef("Option1 Value", FieldType.STRING));

        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
            .setAllowMultiLineText(true)
            .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
            .add(
                new BasicFieldTransformer("Variant Price").nullToValue(100d).stringToDouble(), 
                new BasicFieldTransformer("Variant Taxable").stringToBoolean())
            .add(new SelectFields("Title", "Variant Price", "Variant Taxable", "Option1 Value"));

        reader = new ValidatingReader(reader).add(new SchemaFilter(entityDef));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }

}
