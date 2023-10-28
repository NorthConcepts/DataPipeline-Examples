package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;

public class SerializeRecordListFromToJson {
    public static void main(String[] args) throws IOException {
        Record record1 = new Record();
        record1.setField("name", "John Wayne");
        record1.setField("balance", new BigInteger("1234567894"));

        Record record2 = new Record();
        record2.setField("name", "Peter Parker");
        record2.setField("balance", new BigInteger("9876543210"));

        RecordList recordList = new RecordList(record1, record2);
        recordList.toJson(new FileWriter("example/data/output/name-balance.json"));
        System.out.println("Serialization of record list to JSON file completed");

        RecordList recordList1 = new RecordList();
        recordList1.fromJson(Files.newInputStream(new File("example/data/output/name-balance.json").toPath()));
        System.out.println("Deserialization of record list from JSON file completed");
        System.out.println(recordList1);
    }
}
