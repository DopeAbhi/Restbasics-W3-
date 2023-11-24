package resources;

import com.aventstack.extentreports.ExtentReports;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import static junit.framework.Assert.*;

public class Utils {
    public static RequestSpecification req;

    public static String getGlobalValue(String key) throws IOException
    {
        Properties prop=new Properties();
        FileInputStream fis=new FileInputStream("src/test/java/resources/global.properties");
        prop.load(fis);
        return prop.getProperty(key);

    }
    public static RequestSpecification requestSpecification() throws IOException

    {

   //   ExtentReports extent=new ExtentReports() ; //for html report
        if (req==null) {
            PrintStream log = new PrintStream(new FileOutputStream("/home/abhay/IdeaProjects/Restbasics-W3-/src/test/java/Logs/logging.txt"));
            req = new RequestSpecBuilder().setBaseUri(getGlobalValue("RestAssured.baseURIdev3"))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("device-Type", "WEB")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))

                    .setContentType(ContentType.JSON).build();   //request spec builder
            return req;

        }
        return req;
    }
    public static JsonPath rawtojson(String response)
    {
        JsonPath js1=new JsonPath(response);
        return js1;

    }

    public static ArrayList<String> excelAccess(String accesssheet) throws IOException {

        ArrayList<String> transferdata = new ArrayList<String>();
        FileInputStream fis = new FileInputStream("/home/abhay/IdeaProjects/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase(accesssheet)) {
                XSSFSheet sheet = workbook.getSheetAt(i);


                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                rows.next();
                while (rows.hasNext()) {
                    Row row = rows.next();

                    Iterator<Cell> ce = row.cellIterator();
                    int k = 0;
                    while (ce.hasNext()) {
                        Cell value = ce.next();

                        switch (value.getCellType()) {
                            case NUMERIC -> transferdata.add(k, String.valueOf(value.getNumericCellValue()));
                            case STRING -> transferdata.add(k, value.getStringCellValue());
                            default -> System.out.println("Any other case check log or excel");
                        }
                        k++;
                        }
                    }

                }

            }

        return transferdata;
    }
}
