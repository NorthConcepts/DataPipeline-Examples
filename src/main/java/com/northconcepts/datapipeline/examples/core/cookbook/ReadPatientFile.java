/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.RemoveFields;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.DataReaderLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;
import com.northconcepts.datapipeline.validate.ValidatingReader;

public class ReadPatientFile {

    private static final File inputFile = new File("example/data/input/patient-visits-raw-500000.csv");
    private static final File outputFile = new File("example/data/output/patient-visits-"+System.currentTimeMillis()+".csv");

    private static final File hospitalLookupFile = new File("example/data/input/hospital.csv");
    private static final File procedureLookupFile = new File("example/data/input/procedure.xlsx");

    public static void main(String[] args) {
        
        DataReader reader = new CSVReader(inputFile)
                .setFieldNamesInFirstRow(true);
        
        reader = new ValidatingReader(reader)
                .add(new FilterExpression("patient_gender == 'female' || patient_gender == 'male'"));

        reader = new TransformingReader(reader)
                .add(new RemoveFields("Id"));
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("patient_birthday").stringToDate("MM/dd/yyyy"))
                .add(new BasicFieldTransformer("procedure_date").stringToDate("EEE MMM dd HH:mm:ss z yyyy"));
        
//        reader = new FilteringReader(reader)
//                .add(new FilterExpression("patient_gender == 'female'"));
        
//        reader = new LimitReader(reader, 2);
        
        // Lookup hospitals
        Lookup hospitalLookup = new DataReaderLookup(
                getHospitals(),
                new FieldList("hospital_name", "address1", "city", "state", "zip_code"), 
                new FieldList("hospital_id"));

//        reader = new AsyncReader(reader);
        reader = new TransformingReader(reader,  new NullWriter(), "error_message")
                .add(new LookupTransformer(new FieldList("hospital_name", "hospital_address1", "hospital_city", "hospital_state", "hospital_zip_code"), hospitalLookup));

        // Lookup procedures
        Lookup procedureLookup = new DataReaderLookup(
                getMedicalProcedures(),
                new FieldList("procedure_name"), 
                new FieldList("procedure_id"));

        reader = new TransformingReader(reader,  new NullWriter(), "error_message")
                .add(new LookupTransformer(new FieldList("procedure_name"), procedureLookup));
        
        // Exclude redundant fields -- due to lookup mapping to IDs
        reader = new TransformingReader(reader,  new NullWriter(), "error_message")
                .add(new RemoveFields("hospital_name", "hospital_address1", "hospital_city", "hospital_state", "hospital_zip_code"))
                .add(new RemoveFields("procedure_name"));
        
//        DataWriter writer = StreamWriter.newSystemOutWriter();
        DataWriter writer = new CSVWriter(outputFile);

        Job job = Job.run(reader, writer);
        
        System.out.println(job.getRecordsTransferred() + " ==> " + job.getRunningTimeAsString());
        System.out.println(reader.getRecordCount());
        System.out.println(writer.getRecordCount());
    }
    
    // can return any type of DataReader for lookup, including JdbdReader
    private static DataReader getHospitals() {
        DataWriter discardWriter = new NullWriter();
        
        DataReader reader = new CSVReader(hospitalLookupFile)
                .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader, discardWriter, "error_message")
                .add(new RenameField("Provider Number", "hospital_id"))
                .add(new BasicFieldTransformer("hospital_id").stringToLong())
                .add(new RenameField("Hospital Name", "hospital_name"))
                .add(new RenameField("Address 1", "address1"))
                .add(new RenameField("Address 2", "address2"))
                .add(new RenameField("Address 3", "address3"))
                .add(new RenameField("City", "city"))
                .add(new RenameField("State", "state"))
                .add(new RenameField("ZIP Code", "zip_code"))
                .add(new RenameField("County", "county"))
                .add(new RenameField("Phone Number", "phone_number"))
                .add(new RenameField("Hospital Type", "hospital_type"))
                .add(new RenameField("Hospital Ownership", "hospital_ownership"))
                .add(new RenameField("Emergency Services", "emergency_services"))
                .add(new SetCalculatedField("emergency_services", "decode(emergency_services, 'Yes', true, 'No', false)"))
                ;
        
        return reader;
    }
    
    // can return any type of DataReader for lookup, including JdbdReader
    private static DataReader getMedicalProcedures() {
        DataWriter discardWriter = new NullWriter();
        
        ExcelDocument document = new ExcelDocument()
                .open(procedureLookupFile);
        
        DataReader reader = new ExcelReader(document)
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader, discardWriter, "error_message")
                .add(new RenameField("PROCEDURE CODE", "procedure_id"))
                .add(new RenameField("LONG DESCRIPTION", "procedure_name"))
                .add(new RenameField("SHORT DESCRIPTION", "procedure_name_short"))
                .add(new SelectFields("procedure_id", "procedure_name", "procedure_name_short"))
                ;
        
        return reader;
    }
    
}
