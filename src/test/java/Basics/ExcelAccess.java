package Basics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelAccess {

    public static void main(String[] args) throws IOException {
        ArrayList<String> transferdata = new ArrayList<String>();
        FileInputStream fis = new FileInputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase("Packpurchase")) {
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

                        System.out.println(value.getCellType());
                        switch (value.getCellType()) {
                            case NUMERIC -> {
                                transferdata.add(k, String.valueOf(value.getNumericCellValue()));
                                System.out.println(transferdata.get(k));
                            }
                            case STRING -> transferdata.add(k, value.getStringCellValue());
                            default -> System.out.println("Any other case check log or excel");
                        }
                        k++;
//                        if (value.getCellType().equals("NUMERIC")) {
//
//                            System.out.println(transferdata.get(k));
//                            k++;
//                        }
//                        else {
//                            transferdata.add(k, value.getStringCellValue());
//                            System.out.println(transferdata.get(k));
//                            k++;
//
//                        }

                    }
                }

            }

        }


    }
}


