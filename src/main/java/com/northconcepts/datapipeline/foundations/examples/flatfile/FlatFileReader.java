package com.northconcepts.datapipeline.foundations.examples.flatfile;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Parser;
import com.northconcepts.datapipeline.core.Record;

public class FlatFileReader extends DataReader {

    private List<FlatFileFieldDef> fields = new ArrayList<>();
    private final Parser parser;

    public FlatFileReader(Reader reader) {
        this.parser = new Parser(reader);
    }

    @Override
    public void open() throws DataException {
        if (fields.size() == 0) {
            throw exception("No field definitions added to parse");
        }
        super.open();
    }

    @Override
    public void close() throws DataException {
        try {
            parser.close();
        } finally {
            super.close();
        }
    }

    @Override
    public boolean isLineageSupported() {
        return true;
    }

    public List<FlatFileFieldDef> getFields() {
        return fields;
    }

    public FlatFileReader addField(FlatFileFieldDef field) {
        fields.add(field);
        return this;
    }

    public FlatFileReader addFixedLengthField(String name, int length) {
        return addField(new FixedLengthFieldDef().setName(name).setLength(length));
    }

    public FlatFileReader addVariableLengthField(String name, String terminator) {
        return addField(new VariableLengthFieldDef().setName(name).setTerminator(terminator));
    }

    @Override
    protected Record readImpl() throws Throwable {
        if (parser.peek(0) == Parser.EOF) {
            return null;
        }

        Record record = new Record();

        if (isSaveLineage()) {
            recordLineage.setRecord(record).setFileColumnNumber(parser.getColumn());
        }

        for (int i = 0; i < fields.size(); i++) {
            FlatFileFieldDef fieldDef = fields.get(i);
            try {
                long fileColumnNumber = parser.getColumn();

                fieldDef.parseNextField(this, parser, record);

                if (isSaveLineage()) {
                    fieldLineage.setField(record.getField(-1)).setFileColumnNumber(fileColumnNumber);
                }

            } catch (Throwable e) {
                throw exception(e);
            }
        }

        return record;
        //        return record.getFieldCount() == 0?null:record;
    }

    @Override
    public DataException addExceptionProperties(DataException exception) {
        exception = parser.exception(exception);
        exception.set(name + ".fieldCount", fields.size());
        for (int i = 0; i < fields.size(); i++) {
            exception.set(name + ".field[" + i + "]", fields.get(i));
        }
        return super.addExceptionProperties(exception);
    }

}
