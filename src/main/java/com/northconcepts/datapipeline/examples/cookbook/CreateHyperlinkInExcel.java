package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelFieldMetadata;
import com.northconcepts.datapipeline.excel.ExcelHyperlink;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class CreateHyperlinkInExcel {

    private static File OUTPUT_FILE = new File("example/data/output", "order_details.xlsx");

    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument();

        DataReader reader;
        DataWriter writer;

        // create order_details excel sheet with different hyperlink
        reader = new MemoryReader(getOrderRecordList());

        writer = new ExcelWriter(document)
                .setSheetName("order_details")
                .setAutofitColumns(true)
                .setWriteMetadata(true);

        writer = new ExcelHyperlinkProxyWriter(writer);
        Job.run(reader, writer);

        // create product_details excel sheet
        reader = new MemoryReader(getProductRecordList());
        writer = new ExcelWriter(document).setSheetName("product_details").setAutofitColumns(true);
        Job.run(reader, writer);

        document.save(OUTPUT_FILE);
    }

    public static RecordList getOrderRecordList() {
        RecordList recordList = new RecordList();
        recordList.add(getOrderRecord(1, 5, 10, 50, "Laptop Stand", "TechGear"));
        recordList.add(getOrderRecord(2, 10, 100, 1000, "Noise-Cancel Headphones", "SoundMax"));
        recordList.add(getOrderRecord(3, 20, 5, 100, "USB-C Cable", "CableCo"));
        recordList.add(getOrderRecord(4, 50, 200, 10000, "Office Chair", "ComfortWork"));
        recordList.add(getOrderRecord(5, 20, 25, 500, "Wireless Mouse", "ClickPoint"));

        return recordList;
    }

    private static Record getOrderRecord(Integer orderId, Integer orderCount, Integer pricePerUnit, Integer totalPrice, String productName, String manufacturer) {
        return new Record()
                .setField("order_id", orderId)
                .setField("order_count", orderCount)
                .setField("price_per_unit", pricePerUnit)
                .setField("total", totalPrice)
                .setField("product_name", productName)
                .setField("order_link", "http://127.0.0.1:8080/orders/status/" + orderId)
                .setField("manufacturer_email_id", productName.replace(" ", "_") + "@" + manufacturer + ".com");
    }

    public static RecordList getProductRecordList() {
        RecordList recordList = new RecordList();
        recordList.add(getProductRecord(1, "Laptop Stand", "Adjustable aluminum stand for laptops 13–17. Helps heat dissipation.", "TechGear"));
        recordList.add(getProductRecord(2, "Noise-Cancel Headphones", "Wireless over-ear headphones with ANC and 30-hour battery life.", "SoundMax"));
        recordList.add(getProductRecord(3, "USB-C Cable", "1-meter fast-charging cable, supports 60W.", "CableCo"));
        recordList.add(getProductRecord(4, "Office Chair", "Ergonomic chair with lumbar support and tilt adjustment.", "ComfortWork"));
        recordList.add(getProductRecord(5, "Wireless Mouse", "2.4GHz wireless mouse with 1200 DPI.", "ClickPoint"));

        return recordList;
    }

    private static Record getProductRecord(Integer productId, String productName, String productDescription, String manufacturer) {
        return new Record()
                .setField("product_id", productId)
                .setField("product_name", productName)
                .setField("product_description", productDescription)
                .setField("manufacturer", manufacturer)
                .setField("manufacturer_email", productName+ "@" + manufacturer +".com");
    }
}

class ExcelHyperlinkProxyWriter extends ProxyWriter {

    private ExcelFieldMetadata metadata = new ExcelFieldMetadata();

    public ExcelHyperlinkProxyWriter(DataWriter nestedDataWriter) {
        super(nestedDataWriter);
    }

    @Override
    protected Record interceptRecord(Record record) throws Throwable {
        for (Field field : record) {
            metadata.setField(field);

            if ("product_name".equalsIgnoreCase(field.getName())) {
                metadata.setExcelHyperlink(ExcelHyperlink.forCell("product_details", 1, (int) getRecordCount() + 2));
            } else if ("order_link".equalsIgnoreCase(field.getName())) {
                metadata.setExcelHyperlink(ExcelHyperlink.forUrl(field.getValueAsString()));
            } else if ("manufacturer_email_id".equalsIgnoreCase(field.getName())) {
                metadata.setExcelHyperlink(ExcelHyperlink.forEmail(field.getValueAsString()));
            }
        }
        return super.interceptRecord(record);
    }
}
