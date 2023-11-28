package Basics;

import genrics.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import payload.usertreepayload;

import javax.swing.text.html.parser.Parser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static genrics.Utils.requestSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

@Test

public class Usertree {

public void tree_creation() throws Exception
{
    String avatarresp;
    boolean T = true;


    String[] Treedata = new String[4];
    ArrayList<String> TreedataList = new ArrayList<String>();
    ArrayList<String> UserdataList = new ArrayList<String>();
    FileInputStream fis = new FileInputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone.xlsx");
    XSSFWorkbook workbook = new XSSFWorkbook(fis);
    int sheets = workbook.getNumberOfSheets();


    //Login
        for(
    int i = 0;
    i<sheets;i++)

    {
        String referralLink = null;

        if (workbook.getSheetName(i).equalsIgnoreCase("UserTree")) {
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
                for (int j = 0; j < 4; j++) {
                    TreedataList.add(j, Treedata[j]);

                    System.out.println(TreedataList.get(j));
                }


                if (T) {

                    //User Status Check
                    String statusresp = given().spec(requestSpecification())
                            .body(usertreepayload.userstatuspayload(TreedataList.get(0)))
                            .when().post("/writer/v3/user/checkAccountStatus")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();//Get Verification URL

                    JsonPath statusjson = new JsonPath(statusresp);
                    boolean status = statusjson.getBoolean("data.islogin");

                    if (status) {
                        System.out.println(status);

                        UserdataList = Login.Loginfeature(TreedataList.get(0), TreedataList.get(1));

                        System.out.println(UserdataList.get(1));
                        referralLink = UserdataList.get(1);


                    } else {
                        UserdataList = Signup.signupfeature(TreedataList, "amrendra");
                        referralLink = UserdataList.get(1);
                        System.out.println(UserdataList.get(1));
                        //Add Balance

            given().spec(requestSpecification()).header("Token",UserdataList.get(0))
                    .queryParams("email",""+TreedataList.get(0)+"","amount","100000","password","711b525c69e8b0edc6221518b8ff878f")
                    .when().get("/writer/test/api/addBalanceForTest").
            then().log().all().assertThat().statusCode(200);

                    }
                    T = false;
                } else {

                    UserdataList = Signup.signupfeature(TreedataList, referralLink);
                    referralLink = UserdataList.get(1);
                    System.out.println(UserdataList.get(1));


                    T = false;
                }
            }


        }

    }

}
}
