package Basics;

import io.restassured.RestAssured;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.stringtemplate.v4.ST;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class exceldriven {


    //Generic Method to access excel
    public static void main(String[] args) throws IOException {
        String treedata[] = new String[4];

        FileInputStream fis = new FileInputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
        RestAssured.baseURI = "https://quickdev3.super.one";

        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase("PlayerDetails")) {
                XSSFSheet sheet = workbook.getSheetAt(i);


                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                rows.next();
                while (rows.hasNext()) {
                    Row row = rows.next();

                    Iterator<Cell> ce = row.cellIterator();
                    int k = 0;
                    int column = 0;

                    while (ce.hasNext()) {
                        Cell value = ce.next();
                        //      System.out.println(value.getStringCellValue());
                        if (value.getStringCellValue().equalsIgnoreCase("Email")) {

                            System.out.println(value.getStringCellValue());

                            column = k;


                        }
                        k++;
                    }

                    while (rows.hasNext()) {
                        Row r = rows.next();

                            Iterator<Cell> cv = r.cellIterator();
                            while (cv.hasNext()) {
                                Cell c = cv.next();
                                System.out.println(c.getStringCellValue());


                            }



                    }
                }

            }
            int rowNum;
            FileInputStream fiss = new FileInputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone2.xlsx");
            //FileInputStream fis = new FileInputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone2.xlsx"); //for mac
            XSSFWorkbook workbook1 = new XSSFWorkbook(fis);

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
            rowNum = sheet.getLastRowNum() + 1;


            Row row = sheet.createRow(rowNum);

            for (int j = 0; j < 10; j++) {


                Cell cell = row.createCell(i); // Specify the cell index (0, 1, 2, ...)
                // Specify the cell index (0, 1, 2, ...)

                // Set the cell value (e.g., from your API response)
                // Example:
                cell.setCellValue("yes");
            }

            FileOutputStream fileOutputStream = new FileOutputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone2.xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();

            // Close the input stream
            fis.close();
        }
    }
}
