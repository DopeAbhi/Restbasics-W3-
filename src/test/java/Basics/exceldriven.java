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
        }
    }
}
