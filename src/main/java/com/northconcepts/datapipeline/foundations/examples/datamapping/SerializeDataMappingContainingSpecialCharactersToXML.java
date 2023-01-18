/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;

public class SerializeDataMappingContainingSpecialCharactersToXML {

	public static void main(String[] args) {
		DataMapping mapping = new DataMapping()
				// Description contains special character SOH (start of heading)
				.setDescription("DataMapping for user table having start of heading character" + ((char) 01))
				.addFieldMapping("first_name", "source.fname")
				.addFieldMapping("last_name", "toUpperCase(source.lname)")
				.addFieldMapping("name", "source.fname + ' ' + target.last_name")
				.addFieldMapping("name_length", "length(target.name)");

		/*
		 * Default XML version 1.0 is used in XML Serialization.
		 * JDK 19 (and onwards) will throw an exception because of special character.
		 */
		try {
			System.out.println(mapping.toXml());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println("===============================================================================");

		/*
		 * From JDK 19 onwards, to serialize such special characters, XML version 1.1 is required.
		 * To Enable XML version 1.1 declaration, consider this snippet.
		 */
		System.out.println(mapping.toXml(true));
	}

}
