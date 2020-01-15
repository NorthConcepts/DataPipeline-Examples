package com.northconcepts.datapipeline.examples.cookbook;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Endpoint;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingWriter;

public class ProfilePerformance {

    public static void main(String[] args) {
        Endpoint.setCaptureElapsedTime(true);
        
        DataReader reader = new FakeTransactionsReader(100_000, 15, 0, Moment.parseMoment("2010-02-14 12:01").getDate(), TimeUnit.MINUTES.toMillis(1));
        reader = new FilteringReader(reader).add(new FilterExpression("transaction_id % 10 == 0"));
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        writer = new TransformingWriter(writer).add(new SetCalculatedField("next_transaction_id", "transaction_id + 1"));
        
        Job job = Job.run(reader, writer);
        
        profileJob(job);

    }

    // ==================================================
    
    public static final Logger log = DataEndpoint.log; 

    private static void profileJob(Job job) {
        log.info("running time: " + job.getRunningTimeAsString());
        log.info("records transferred: " + job.getRecordsTransferred());
        
        logEndpoint("    --> ", job.getReaders());
        logEndpoint("    <-- ", job.getWriters());
    }

	private static void logEndpoint(String string, List<? extends DataEndpoint> endpoints) {
		for (DataEndpoint endpoint : endpoints) {
            DataEndpoint nestedEndpoint = endpoint.getNestedEndpoint();
			if (nestedEndpoint != null) {
                log.info(string + endpoint.getName() +  ":    " + new Interval((nestedEndpoint.getElapsedTime() - nestedEndpoint.getElapsedTime()), 0).toString());
            } else {
                log.info(string + endpoint.getName() +  ":    " + endpoint.getElapsedTimeAsString());
            }
        }
	}

    // ==================================================

    /**
     * A data source that simulates reading a predictable stream of records (from a CSV file or database for example).
     */
    public static class FakeTransactionsReader extends DataReader {

        private final long maxTransactions;
        private long nextTransactionId;
        private final int maxAccounts;
        private final long recordDelay;
        private Date nextTime;
        private final long timeDelta;

        public FakeTransactionsReader(long maxTransactions, int maxAccounts, long recordDelay, Date startTime, long timeDelta) {
            this.maxTransactions = maxTransactions;
            this.maxAccounts = maxAccounts;
            this.recordDelay = recordDelay;
            this.nextTime = startTime;
            this.timeDelta = timeDelta;
        }

        @Override
        protected Record readImpl() throws Throwable {
            if (nextTransactionId >= maxTransactions) {
                return null;
            }

            if (recordDelay > 0) {
                Thread.sleep(recordDelay);
            }

            Record record = new Record();
            record.setField("transaction_id", nextTransactionId++);
            record.setField("account_id", "account-" + nextTransactionId % maxAccounts);
            record.setField("transaction_time", nextTime);
            nextTime = new Date(nextTime.getTime() + timeDelta);
            record.setField("price1", BigDecimal.valueOf(nextTransactionId + 0.01));
            record.setField("price2", BigDecimal.valueOf(nextTransactionId + 0.02));
            record.setField("price3", BigDecimal.valueOf(nextTransactionId + 0.03));
            record.setField("price4", BigDecimal.valueOf(nextTransactionId + 0.04));
            return record;
        }

    }


}
