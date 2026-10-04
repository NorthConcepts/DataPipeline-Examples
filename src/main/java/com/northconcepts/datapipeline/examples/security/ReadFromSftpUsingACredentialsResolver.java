package com.northconcepts.datapipeline.examples.security;

import java.io.InputStreamReader;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filesystem.sftp.SftpFileSystem;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.security.Credentials;
import com.northconcepts.datapipeline.security.SuppliedCredentialsResolver;

public class ReadFromSftpUsingACredentialsResolver {

    private static final String HOST = "localhost";
    private static final int PORT = 22;
    private static final String FOLDER = "/upload";
    private static final String FILE = "trades.csv";

    public static void main(String[] args) throws Throwable {
        SftpFileSystem sftp = new SftpFileSystem(HOST, PORT)
                .setCredentialsResolver(new SuppliedCredentialsResolver(() -> Credentials.of(
                        Credentials.USERNAME, "YOUR USERNAME",
                        Credentials.PASSWORD, "YOUR PASSWORD")));
        sftp.open();
        try {
            DataReader reader = new CSVReader(new InputStreamReader(sftp.readFile(FOLDER, FILE)))
                    .setFieldNamesInFirstRow(true);
            DataWriter writer = StreamWriter.newSystemOutWriter();

            Job.run(reader, writer);
        } finally {
            sftp.close();
        }
    }

}
