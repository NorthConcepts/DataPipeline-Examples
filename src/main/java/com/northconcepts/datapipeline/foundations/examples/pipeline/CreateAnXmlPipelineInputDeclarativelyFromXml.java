package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.XmlPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.DataWriterPipelineOutput;

import java.io.FileInputStream;

public class CreateAnXmlPipelineInputDeclarativelyFromXml {

    public static void main(String[] args) throws Throwable {
        Pipeline pipeline = new Pipeline();

        XmlPipelineInput input = (XmlPipelineInput) new XmlPipelineInput()
            .fromXml(new FileInputStream("example/data/input/pipeline/xmlpipelineinput.xml"));

        pipeline.setInput(input);
        pipeline.setOutput(new DataWriterPipelineOutput(() -> StreamWriter.newSystemOutWriter()));

        pipeline.run();
    }

}
