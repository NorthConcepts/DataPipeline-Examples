/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.flatfile;

import static com.northconcepts.datapipeline.core.XmlSerializable.getChildElement;
import static com.northconcepts.datapipeline.core.XmlSerializable.getChildElements;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordSerializable;
import com.northconcepts.datapipeline.core.XmlSerializable;
import com.northconcepts.datapipeline.foundations.file.FileSource;
import com.northconcepts.datapipeline.foundations.pipeline.input.FileSourcePipelineInput;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FlatFilePipelineInput extends FileSourcePipelineInput {

    private FileSource fileSource;
    private List<FlatFileFieldDef> fields = new ArrayList<>();

    public FlatFilePipelineInput() {
    }

    @Override
    public boolean isLineageSupported() {
        return true;
    }

    @Override
    public FileSource getFileSource() {
        return fileSource;
    }

    @Override
    public FlatFilePipelineInput setFileSource(FileSource fileSource) {
        this.fileSource = fileSource;
        return this;
    }

    @Override
    public FlatFilePipelineInput setSaveLineage(boolean saveLineage) {
        return (FlatFilePipelineInput) super.setSaveLineage(saveLineage);
    }

    public FlatFilePipelineInput addField(FlatFileFieldDef field) {
        fields.add(field);
        return this;
    }

    public FlatFilePipelineInput addFixedLengthField(String name, int length) {
        return addField(new FixedLengthFieldDef().setName(name).setLength(length));
    }

    public FlatFilePipelineInput addVariableLengthField(String name, String terminator) {
        return addField(new VariableLengthFieldDef().setName(name).setTerminator(terminator));
    }

    @Override
    public DataReader createDataReader() {
        if (fileSource == null) {
            throw exception("fileSource is null");
        }

        try {
            InputStream in = fileSource.getInputStream();
            FlatFileReader reader = new FlatFileReader(new InputStreamReader(in, "UTF-8"));
            for (FlatFileFieldDef fieldDef : fields) {
                reader.addField(fieldDef);
            }
            reader.setSaveLineage(isSaveLineage());
            return reader;
        } catch (Throwable e) {
            throw exception(e);
        }
    }

    @Override
    public Record toRecord() {
        Record record = super.toRecord()
                .setField("fileSource", fileSource == null ? null : fileSource.toRecord());

        //record.setField("fillChar", fillChar)

        ArrayValue fieldsArray = new ArrayValue();
        if (fields != null) {
            for (FlatFileFieldDef field : fields) {
                fieldsArray.addValue(field.toRecord());
            }
        }
        record.setField("fields", fieldsArray);

        return record;
    }

    @Override
    public FlatFilePipelineInput fromRecord(Record source) {
        super.fromRecord(source);

        this.fileSource = RecordSerializable.newInstanceFromRecord(source.getFieldValueAsRecord("fileSource", null), FileSource.class);

        //this.fillChar = source.getFieldValueAsChar("fillChar", fixedWidthReader.getFillChar());

        ArrayValue array = source.getFieldValueAsArray("fields", null);
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                addField(RecordSerializable.newInstanceFromRecord(array.getValueAsRecord(i), FlatFileFieldDef.class));

            }
        }

        return this;
    }

    @Override
    public Element toXmlElement(Document document) {
        Element pipelineInputElement = super.toXmlElement(document);

        //setAttribute(pipelineInputElement, "fillChar", fillChar);

        if (fileSource != null) {
            pipelineInputElement.appendChild(fileSource.toXmlElement(document));
        }

        if (Util.isNotEmpty(fields)) {
            Element fieldsElement = document.createElement("fields");
            for (FlatFileFieldDef field : fields) {
                fieldsElement.appendChild(field.toXmlElement(document));
            }
            pipelineInputElement.appendChild(fieldsElement);
        }

        return pipelineInputElement;
    }

    @Override
    public FlatFilePipelineInput fromXmlElement(Element pipelineInputElement) {
        super.fromXmlElement(pipelineInputElement);

        Element fileSourceElement = getChildElement(pipelineInputElement, "file-source");
        fileSource = XmlSerializable.newInstanceFromXml(fileSourceElement, FileSource.class);

        //this.fillChar = getAttribute(pipelineInputElement, "fillChar", ' ');

        Element fieldsElement = getChildElement(pipelineInputElement, "fields");
        if (fieldsElement != null) {
            List<Element> fieldElementList = getChildElements(fieldsElement, "field");
            for (Element fieldElement : fieldElementList) {
                addField(XmlSerializable.newInstanceFromXml(fieldElement, FlatFileFieldDef.class));
            }
        }

        return this;
    }

}
