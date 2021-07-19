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

public class FixedLengthFieldDef extends FlatFileFieldDef {

    private final StringBuilder buffer = new StringBuilder();
    private int length;

    public FixedLengthFieldDef() {
    }

    @Override
    protected void parseNextField(FlatFileReader reader, Parser parser, Record record) {
        buffer.setLength(0);

        for (int charOffset = 0; charOffset < length; charOffset++) {
            int c = parser.peek(0);
            if (c == Parser.EOF) {
                throw new DataException("unexpected EOF in fixed length field: " + getName())
                .set("fieldBuffer", buffer)
                .set("charOffset", charOffset)
                .set("fieldType", getClass())
                .set("field", this);
            }
            buffer.append((char)c);
            parser.consume();
        }

        record.addField(getName(), buffer.toString());
    }

    @Override
    public FixedLengthFieldDef setName(String name) {
        return (FixedLengthFieldDef) super.setName(name);
    }

    @Override
    public FixedLengthFieldDef setSkip(boolean skip) {
        return (FixedLengthFieldDef) super.setSkip(skip);
    }

    public int getLength() {
        return length;
    }

    public FixedLengthFieldDef setLength(int length) {
        this.length = length;
        return this;
    }

    @Override
    public Record toRecord() {
        return super.toRecord()
                .setField("length", length);
    }

    @Override
    public FixedLengthFieldDef fromRecord(Record source) {
        super.fromRecord(source);
        this.length = source.getFieldValueAsInteger("length", 0);
        return this;
    }

    @Override
    public Element toXmlElement(Document document) {
        Element element = super.toXmlElement(document);
        setAttribute(element, "length", length);
        return element;
    }

    @Override
    public FixedLengthFieldDef fromXmlElement(Element element) {
        super.fromXmlElement(element);
        this.length = getAttribute(element, "length", 0);
        return this;
    }

}
