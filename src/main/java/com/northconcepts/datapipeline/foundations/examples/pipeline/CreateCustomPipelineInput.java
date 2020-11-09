package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.foundations.sourcecode.JavaCodeBuilder;
import com.northconcepts.datapipeline.internal.lang.Util;

public class CreateCustomPipelineInput {

    public static void main(String[] args) {
        CustomPipelineInput pipelineInput = new CustomPipelineInput();

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFile().setPath("example/data/output/custom_package_pipeline_input.xlsx"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline()
                .setInput(pipelineInput)
                .setOutput(pipelineOutput);

        pipeline.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Generated Code:" + System.lineSeparator());
        System.out.println(pipeline.getJavaCode().getSource());

        System.out.println("---------------------------------------------------------------------------------------------------------");

        Record record = pipeline.toRecord();
        System.out.println(record);

        System.out.println("---------------------------------------------------------------------------------------------------------");

        Pipeline pipeline2 = new Pipeline().fromRecord(record);
        pipeline2.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Pipeline as JSON:");
        System.out.println(Util.formatJson(pipeline.toJsonString()));
    }

    public static class CustomPipelineInput implements PipelineInput {

        private static final int MAX_TRUCKS = 10;
        private static final long MAX_PACKAGES = 20;
        private static final int RECORD_DELAY_MILLISECONDS = 250;

        @Override
        public DataReader createDataReader() {
            return new FakePackageReader(MAX_TRUCKS, MAX_PACKAGES, RECORD_DELAY_MILLISECONDS);
        }

        @Override
        public void generateJavaCode(JavaCodeBuilder code) {
            code.addImport("com.northconcepts.datapipeline.core.DataReader");
            code.addImport("com.northconcepts.datapipeline.job.Job");
            code.addImport("com.northconcepts.datapipeline.foundations.examples.pipeline.CreateCustomPipelineInput.FakePackageReader");

            CodeWriter writer = code.getSourceWriter();
            writer.println("DataReader reader = new FakePackageReader(%d, %d, %d);", MAX_TRUCKS, MAX_PACKAGES, RECORD_DELAY_MILLISECONDS);
        }

        @Override
        public String getName() {
            return "CustomPipelineInput";
        }

        @Override
        public Record toRecord() {
            return new Record();
        }

        @Override
        public PipelineInput fromRecord(Record source) {
            return this;
        }
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

}
