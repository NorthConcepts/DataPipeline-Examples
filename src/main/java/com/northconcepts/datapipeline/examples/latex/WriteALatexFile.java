package com.northconcepts.datapipeline.examples.latex;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.latex.LatexWriter;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteALatexFile {

    public static void main(String[] args) {
        RecordList records = new RecordList(

            new Record()
                .setField("Product",       "Wireless Keyboard")
                .setField("SKU",           "WK-1001")
                .setField("Quantity",      150)
                .setField("Unit Price",    new BigDecimal("49.99"))
                .setField("Discount %",    10.0)          // '%' is a LaTeX comment char -> escaped to \%
                .setField("In Stock",      true)
                .setField("Last Updated",  Date.valueOf("2026-05-15"))
                .setField("Notes",         "Best seller"),

            new Record()
                .setField("Product",       "Tom & Jerry Special Edition")    // & -> \&
                .setField("SKU",           "TJ-2002")
                .setField("Quantity",      30)
                .setField("Unit Price",    new BigDecimal("99.95"))
                .setField("Discount %",    5.0)
                .setField("In Stock",      true)
                .setField("Last Updated",  Date.valueOf("2026-04-01"))
                .setField("Notes",         "Price in $USD; see section #4_A"), // $ # _ -> \$ \# \_

            new Record()
                .setField("Product",       "Config {Pro} v2")               // { } -> \{ \}
                .setField("SKU",           "CP-3003")
                .setField("Quantity",      5)
                .setField("Unit Price",    new BigDecimal("299.00"))
                .setField("Discount %",    0.0)
                .setField("In Stock",      false)
                .setField("Last Updated",  Date.valueOf("2026-03-20"))
                .setField("Notes",         "Path: C:\\Program Files\\App"),   // \ -> \textbackslash{}

            new Record()
                .setField("Product",       "SuperCharge^2")                  // ^ -> \textasciicircum{}
                .setField("SKU",           "SC-4004")
                .setField("Quantity",      200L)          // long type
                .setField("Unit Price",    new BigDecimal("14.50"))
                .setField("Discount %",    15.5)
                .setField("In Stock",      true)
                .setField("Last Updated",  Date.valueOf("2026-06-01"))
                .setField("Notes",         "~Experimental model"),             // ~ -> \textasciitilde{}

            new Record()
                .setField("Product",       "Legacy Adapter")
                .setField("SKU",           "LA-5005")
                .setField("Quantity",      0)
                .setField("Unit Price",    new BigDecimal("4.99"))
                .setField("Discount %",    0.0)
                .setField("In Stock",      false)
                .setField("Last Updated",  (Object) null)  // null -> empty cell
                .setField("Notes",         (Object) null)  // null -> empty cell
        );

        DataReader reader = new MemoryReader(records);
        DataWriter writer = new LatexWriter(new File("example/data/output/product-catalog.tex"));

        Job.run(reader, writer);
    }

}
