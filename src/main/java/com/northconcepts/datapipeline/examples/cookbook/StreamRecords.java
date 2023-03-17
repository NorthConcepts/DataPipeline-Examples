package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.Date;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.internal.lang.Util;

public class StreamRecords {
    public static void main(String[] args) {
        RecordList recordList = new RecordList();

        Record record1 = new Record();
        record1.setField("Account", "JW19850512AST");
        record1.setField("Name", "John Wayne");
        record1.setField("Balance", 156.35);
        record1.setField("LastPaymentDate", new Date(2007-1900, 1, 13));
        recordList.add(record1);

        Record record2 = new Record();
        record2.setField("Account", "PP20010204PIJ");
        record2.setField("Name", "Peter Parker");
        record2.setField("Balance", -120.85);
        record2.setField("LastPaymentDate", FieldType.DATE);
        recordList.add(record2);

        System.out.println("================================Using forEach================================");
        recordList.forEach(System.out::println);

        System.out.println("\n\n================================Using stream================================");
        recordList.stream()
                .map(record -> Util.formatXml(record.toXml()))
                .forEach(System.out::println);

        System.out.println("\n\n================================Using parallelStream================================");
        recordList.parallelStream()
                .map(record -> Util.formatXml(record.toXml()))
                .forEach(System.out::println);
    }
}
