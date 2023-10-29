package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

import java.io.IOException;
import java.math.BigInteger;

public class SerializeRecordListToFromXml {
    public static void main(String[] args) throws IOException {
        Record record1 = new Record();
        record1.setField("name", "John Wayne");
        record1.setField("balance", new BigInteger("1234567894"));

        Record record2 = new Record();
        record2.setField("name", "Peter Parker");
        record2.setField("balance", new BigInteger("9876543210"));

        RecordList recordList = new RecordList(record1, record2);

        String xml = recordList.toRecord().toXml();
        System.out.println("Serialization of record list to XML completed");
        System.out.println(xml);

        Record record = Record.fromXml(xml);
        Record document = record.getFieldValueAsRecord("document", new Record());
        RecordList recordList1 = new RecordList();
        recordList1.fromRecord(document);
        System.out.println("\nDeserialization of record list from XML completed");
        System.out.println(recordList1);
    }
}
