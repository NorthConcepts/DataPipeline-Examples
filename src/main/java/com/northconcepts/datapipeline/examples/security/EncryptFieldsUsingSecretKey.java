package com.northconcepts.datapipeline.examples.security;

import java.io.File;

import javax.crypto.SecretKey;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.MultiWriter;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.security.SymmetricDecryptingReader;
import com.northconcepts.datapipeline.security.SymmetricEncryptingReader;

public class EncryptFieldsUsingSecretKey {

    private static final String SYMMETRIC_ALGORITHM = "AES";
    private static final int SYMMETRIC_KEY_BITS = 128;

    public static void main(String[] args) throws Throwable {
        SecretKey secretKey = Util.generateSymmetricKey(SYMMETRIC_ALGORITHM, SYMMETRIC_KEY_BITS);
        RecordList encryptedRecord = encrypt(secretKey);
        decrypt(secretKey, encryptedRecord);
    }

    private static RecordList encrypt(SecretKey secretKey) {
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);

        reader = new SymmetricEncryptingReader(reader, SYMMETRIC_ALGORITHM, secretKey)
                .addFields("Vendor", "Published", "Variant Price");

        RecordList recordList = new RecordList();

        Job.run(reader, new MultiWriter(
                StreamWriter.newSystemOutWriter(),
                new MemoryWriter(recordList)));

        return recordList;
    }

    private static void decrypt(SecretKey secretKey, RecordList recordList) {
        DataReader reader = new MemoryReader(recordList);

        reader = new SymmetricDecryptingReader(reader, SYMMETRIC_ALGORITHM, secretKey)
                .addFields("Vendor", "Published", "Variant Price");

        Job.run(reader, StreamWriter.newSystemOutWriter());
    }

}
