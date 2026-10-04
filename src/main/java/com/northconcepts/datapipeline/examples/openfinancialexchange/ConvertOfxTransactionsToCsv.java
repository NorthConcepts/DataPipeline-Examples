package com.northconcepts.datapipeline.examples.openfinancialexchange;

import java.io.File;
import java.io.OutputStreamWriter;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.openfinancialexchange.OpenFinancialExchangeReader;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.SplitArrayField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ConvertOfxTransactionsToCsv {

    private static final String TRANSACTIONS = "BANKMSGSRSV1.STMTTRNRS[0].STMTRS.BANKTRANLIST.STMTTRN";

    public static void main(String[] args) {
        DataReader reader = new OpenFinancialExchangeReader(new File("example/data/input/bank-statement.ofx"));

        reader = new TransformingReader(reader)
                .add(new SplitArrayField(TRANSACTIONS));

        reader = new TransformingReader(reader)
                .add(new CopyField(TRANSACTIONS + ".TRNTYPE", "type"))
                .add(new CopyField(TRANSACTIONS + ".DTPOSTED", "posted"))
                .add(new CopyField(TRANSACTIONS + ".TRNAMT", "amount"))
                .add(new CopyField(TRANSACTIONS + ".FITID", "id"))
                .add(new CopyField(TRANSACTIONS + ".NAME", "name"))
                .add(new SelectFields("type", "posted", "amount", "id", "name"));

        DataWriter writer = new CSVWriter(new OutputStreamWriter(System.out))
                .setFieldNamesInFirstRow(true);

        Job.run(reader, writer);
    }

}
