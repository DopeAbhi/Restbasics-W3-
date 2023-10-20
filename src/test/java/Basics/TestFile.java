package Basics;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class TestFile {
    public static void main(String[] args) throws IOException {
        // Initialize Rest Assured or perform API requests to get data
        RestAssured.baseURI = "https://example.com"; // Set your API endpoint

        // Fetch data using Rest Assured
        // Example:
        // Response response = RestAssured.get("/api/data");

        // Load an existing Excel file
        FileInputStream fis = new FileInputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone2.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Specify the sheet name where you want to add/overwrite data
        String sheetName = "PlayerDetails";

        // Find the specified sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);

        // Check if the sheet exists
        if (sheet == null) {
            System.out.println("Sheet '" + sheetName + "' not found in the Excel file.");
            return;
        }

        // Add data from Rest Assured to Excel
        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0); // Specify the cell index (0, 1, 2, ...)

        // Set the cell value (e.g., from your API response)
        // Example:
         cell.setCellValue("Testt");

        // Save the changes to the Excel file
        FileOutputStream fileOutputStream = new FileOutputStream("existing_data.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        // Close the input stream
        fis.close();
    }
}
