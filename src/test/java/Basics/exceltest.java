package Basics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;

public class exceltest {
    public static void main(String[] args) throws IOException {


        FileInputStream fis = new FileInputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
      String [] userdata = new String[4];



        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase("Packpurchase")) {
                XSSFSheet sheet = workbook.getSheetAt(i);


                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                Row firstrow = rows.next();
                Row secondrow = rows.next();

                Iterator<Cell> ce=secondrow.cellIterator();
                int k=0;
                int coloumn = 0;
                while(ce.hasNext())
                {
                    Cell value=ce.next();

                    userdata[k]=value.getStringCellValue();

                    System.out.println(userdata[k]);
              //      System.out.println(value.getStringCellValue());


//                    if(value.getStringCellValue().equals("User"))
//                    {
//                        System.out.println(value.getStringCellValue());
//                        coloumn=k;
//                        System.out.println(coloumn);
//                        break;
//
//                    }


                    k++;
                }



            }

        }
    }
}