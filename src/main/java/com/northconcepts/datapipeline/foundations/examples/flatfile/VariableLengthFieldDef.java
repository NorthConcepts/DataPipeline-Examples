/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.flatfile;

import static com.northconcepts.datapipeline.core.XmlSerializable.getAttribute;
import static com.northconcepts.datapipeline.core.XmlSerializable.setAttribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Parser;
import com.northconcepts.datapipeline.core.Record;

public class VariableLengthFieldDef extends FlatFileFieldDef {

    private final StringBuilder buffer = new StringBuilder();
    private String terminator;

    public VariableLengthFieldDef() {
    }

    private boolean peekTerminator(Parser parser) {
        for (int i = 0; i < terminator.length(); i++) {
            if (terminator.charAt(i) != parser.peek(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void parseNextField(FlatFileReader reader, Parser parser, Record record) {
        buffer.setLength(0);

        for (int charOffset = 0; !peekTerminator(parser); charOffset++) {
            int c = parser.peek(0);
            if (c == Parser.EOF) {
                throw new DataException("unexpected EOF in variable length field: " + getName())
                .set("fieldBuffer", buffer)
                .set("charOffset", charOffset)
                .set("fieldType", getClass())
                .set("field", this);
            }
            buffer.append((char) c);
            parser.consume();
        }

        parser.consume(terminator.length());

        record.addField(getName(), buffer.toString());
    }

    @Override
    public VariableLengthFieldDef setName(String name) {
        return (VariableLengthFieldDef) super.setName(name);
    }

    @Override
    public VariableLengthFieldDef setSkip(boolean skip) {
        return (VariableLengthFieldDef) super.setSkip(skip);
    }

    public VariableLengthFieldDef setTerminator(String terminator) {
        this.terminator = terminator;
        return this;
    }

    public String getTerminator() {
        return terminator;
    }

    @Override
    public Record toRecord() {
        return super.toRecord()
                .setField("terminator", terminator);
    }

    @Override
    public VariableLengthFieldDef fromRecord(Record source) {
        super.fromRecord(source);
        this.terminator = source.getFieldValueAsString("terminator", null);
        return this;
    }

    @Override
    public Element toXmlElement(Document document) {
        Element element = super.toXmlElement(document);
        setAttribute(element, "terminator", terminator);
        return element;
    }

    @Override
    public VariableLengthFieldDef fromXmlElement(Element element) {
        super.fromXmlElement(element);
        this.terminator = getAttribute(element, "terminator", (String) null);
        return this;
    }

}
