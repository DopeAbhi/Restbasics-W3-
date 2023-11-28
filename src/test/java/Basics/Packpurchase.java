package Basics;



import genrics.APIResources;
import genrics.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import payload.usertreepayload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static genrics.Utils.requestSpecification;
import static io.restassured.RestAssured.given;

@Test(groups = {"Regression"},priority = 4)
public class Packpurchase {


    public void purchasepack() throws IOException {
        ArrayList<String> userdata = new ArrayList<String>();
     ArrayList<String> sender_login_data = new ArrayList<String>();
     String []Treedata =  new String[4];
FileInputStream fis = new FileInputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();


        //Login
        for(int i = 0;i<sheets;i++) {


            if (workbook.getSheetName(i).equalsIgnoreCase("PackPurchase")) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                rows.next();

                while (rows.hasNext()) {
                    Row row = rows.next();


                    Iterator<Cell> ce = row.cellIterator();
                    int k = 0;

                    while (ce.hasNext()) {
                        Cell value = ce.next();


                        Treedata[k] = value.getStringCellValue();

                        System.out.println(Treedata[k]);
                        k++;
                    }
                    for (int j = 0; j < 3; j++) {
                        userdata.add(j, Treedata[j]);

                        System.out.println(userdata.get(j));
                    }


                    //User Login
                    sender_login_data = Login.Loginfeature(userdata.get(0), userdata.get(1));
                    //Pack Purchase
                    APIResources apiResources = APIResources.valueOf("pack_purchase");
                    String purchaseresp = given().spec(requestSpecification()).header("token", sender_login_data.get(0))
                            .body(usertreepayload.pack_purchase_payload((int) Double.parseDouble(userdata.get(2))))
                            .when().patch(apiResources.getResource()).
                            then().assertThat().statusCode(200).extract().response().asString();


                }

            }

        }}}


