package com.northconcepts.datapipeline.examples.security;

import java.io.InputStreamReader;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filesystem.ftp.FtpFileSystem;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.security.Credentials;
import com.northconcepts.datapipeline.security.SuppliedCredentialsResolver;

public class ReadFromFtpUsingACredentialsResolver {

    private static final String HOST = "localhost";
    private static final int PORT = 21;
    private static final String FOLDER = "/upload";
    private static final String FILE = "trades.csv";

    public static void main(String[] args) throws Throwable {
        FtpFileSystem ftp = new FtpFileSystem(HOST, PORT)
                .setCredentialsResolver(new SuppliedCredentialsResolver(() -> Credentials.of(
                        Credentials.USERNAME, "YOUR USERNAME",
                        Credentials.PASSWORD, "YOUR PASSWORD")));
        ftp.open();
        try {
            DataReader reader = new CSVReader(new InputStreamReader(ftp.readFile(FOLDER, FILE)))
                    .setFieldNamesInFirstRow(true);
            DataWriter writer = StreamWriter.newSystemOutWriter();

            Job.run(reader, writer);
        } finally {
            ftp.close();
        }
    }

}
