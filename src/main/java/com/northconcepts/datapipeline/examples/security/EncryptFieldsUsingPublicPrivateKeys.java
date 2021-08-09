package com.northconcepts.datapipeline.examples.security;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.MultiWriter;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.security.AsymmetricDecryptingReader;
import com.northconcepts.datapipeline.security.AsymmetricEncryptingReader;

public class EncryptFieldsUsingPublicPrivateKeys {

    private static final String ASYMMETRIC_ALGORITHM = "RSA";
    private static final int ASYMMETRIC_KEY_BITS = 2048;

    public static void main(String[] args) throws Throwable {
        KeyPair keyPair = Util.generateAsymmetricKeyPair(ASYMMETRIC_ALGORITHM, ASYMMETRIC_KEY_BITS);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        RecordList encryptedRecord = encrypt(privateKey);
        decrypt(publicKey, encryptedRecord);
    }

    private static RecordList encrypt(PrivateKey privateKey) {
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);

        reader = new AsymmetricEncryptingReader(reader, ASYMMETRIC_ALGORITHM, privateKey)
                .addFields("Vendor", "Published", "Variant Price");

        RecordList recordList = new RecordList();

        Job.run(reader, new MultiWriter(
                StreamWriter.newSystemOutWriter(),
                new MemoryWriter(recordList)));

        return recordList;
    }

    private static void decrypt(PublicKey publicKey, RecordList recordList) {
        DataReader reader = new MemoryReader(recordList);

        reader = new AsymmetricDecryptingReader(reader, ASYMMETRIC_ALGORITHM, publicKey)
                .addFields("Vendor", "Published", "Variant Price");

        Job.run(reader, StreamWriter.newSystemOutWriter());
    }

}
