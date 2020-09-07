/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import com.northconcepts.datapipeline.core.Record;

public class SerializeAndDeserializeRecords {

	public static void main(String[] args) throws Throwable {
		Record record1 = new Record();
        record1.setField("name", "John Wayne");
        record1.setField("balance", "12345678901234567890");

        Record record2 = new Record();
        record2.setField("name", "Peter Parker");
        record2.setField("balance", new BigInteger("98765432109876543210"));
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream); 
        outputStream.writeObject(record1);
        outputStream.writeObject(record2);
        outputStream.close();
        
        System.out.println("Serialization of records completed..!!");
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.out.println("Bytes written :- " + byteArray.length);
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream); 
        record1 = (Record) inputStream.readObject();
        record2 = (Record) inputStream.readObject();
        inputStream.close();
        
        System.out.println(record1);
        System.out.println(record2);
        
        System.out.println("Deserialization of records completed..!!");
	}
	
}
