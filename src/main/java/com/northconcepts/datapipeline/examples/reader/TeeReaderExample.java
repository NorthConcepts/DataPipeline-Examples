package com.northconcepts.datapipeline.examples.reader;

import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.core.TeeReader;
import com.northconcepts.datapipeline.job.Job;

public class TeeReaderExample {

	private static final int MAX_TRUCKS = 1;
	private static final long MAX_PACKAGES = 20;
	private static final long RECORD_DELAY_MILLISECONDS = TimeUnit.SECONDS.toMillis(0);

	public static void main(String[] args) throws Throwable {
		DataReader reader = new FakePackageReader(MAX_TRUCKS, MAX_PACKAGES, RECORD_DELAY_MILLISECONDS);

		TeeReader teeReader = new TeeReader(reader, new FakePackageWriter(), true);
		Job job = Job.run(teeReader, new StreamWriter(new File("TeeReader_output.txt")));

		System.out.println(job);
	}

	public static class FakePackageReader extends DataReader {
		private final int maxTrucks;
		private final long maxPackages;
		private long nextPackageId;
		private final long recordDelay;

		public FakePackageReader(int maxTrucks, long maxPackages, long recordDelay) {
			this.maxTrucks = maxTrucks;
			this.maxPackages = maxPackages;
			this.recordDelay = recordDelay;
		}

		@Override
		protected Record readImpl() throws Throwable {
			if (nextPackageId >= maxPackages) {
				return null;
			}

			if (recordDelay > 0) {
				Thread.sleep(recordDelay);
			}

			Record record = new Record();
			record.setField("package_id", nextPackageId++);
			record.setField("truck_id", "truck" + nextPackageId % maxTrucks);
			record.setField("amount", nextPackageId + 0.01);
			return record;
		}
	}
	
	private static class FakePackageWriter extends DataWriter {
		@Override
		protected void writeImpl(Record record) throws Throwable {
			record.addField("timestamp", LocalDateTime.now());
			System.out.println(record);
		}
	}

}
