package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import net.openhft.chronicle.core.values.StringValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;

import static io.restassured.RestAssured.given;

public class exceltest {
    public static void main(String[] args) throws IOException {


        FileInputStream fis = new FileInputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
        String[] userdata = new String[4];


        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase("Packpurchase")) {
                XSSFSheet sheet = workbook.getSheetAt(i);


                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                while (rows.hasNext()) {
                    Row firstrow = rows.next();
                    Row secondrow = rows.next();

                    Iterator<Cell> ce = secondrow.cellIterator();
                    int k = 0;
                    int coloumn = 0;
                    while (ce.hasNext()) {
                        Cell value = ce.next();
                        if (k<2) {

                            userdata[k] = value.getStringCellValue();

                            System.out.println(userdata[k]);
                        }
                        else {
                            userdata[k]=String.valueOf(value.getNumericCellValue());
                            System.out.println(userdata[k]);
                        }



                        RestAssured.baseURI="https://quickdev3.super.one";


                        //Intiate Login
                        given().header("device-type", "WEB")
                                .body(usertreepayload.intiateloginpayload(userdata[0]))
                                .when().patch("/writer/v2/user/email/initiatelogin")
                                .then().assertThat().statusCode(200);
                        //Login

                        String loginresp = given().header("device-type", "WEB")
                                .body(usertreepayload.loginpayload(userdata[0], userdata[1]))
                                .when().patch("/writer/user/email/login")
                                .then().assertThat().statusCode(200).extract().response().asString();
                        JsonPath loginjson = Reuseablemethods.rawtojson(loginresp);
                        String token = loginjson.getString("data.token");
                        System.out.println(token);


                         given().header("token", token).header("device-type", "WEB")
                                .body(usertreepayload.pack_purchase_payload(Integer.parseInt(userdata[2])))
                                .when().patch("/writer/member/package/103138").
                                then().log().all().assertThat().statusCode(200).extract().response().asString();




                        k++;
                    }


                }

            }
        }
    }
}