package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;
import net.openhft.chronicle.core.values.StringValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.html.parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class Transfer {



    public static void main(String[] args) throws IOException, InterruptedException {
        String transferdata[]=new String[5];

//        Scanner scanner=new Scanner(System.in);
//
//        System.out.println("Sender Email");
//        String senderemail= scanner.next();
//        System.out.println("Sender Password");
//        String senderpass=scanner.next();
//        System.out.println("User to be Searched");
//        String searchuser=scanner.next();
//        System.out.println("Receiver Email ");
//        String receiveduser=scanner.next();
//        System.out.println("Enter receiver Password");
//        String receivepass=scanner.next();
//        System.out.println("Amount to be Transfer");
//        int amount=scanner.nextInt();
        //Check User exist
        FileInputStream fis = new FileInputStream("/home/abhay/IdeaProjects/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
        RestAssured.baseURI = "https://quickdev3.super.one";

        for (int i = 0; i < sheets; i++) {

            if (workbook.getSheetName(i).equalsIgnoreCase("Transfer")) {
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
                        if (k < 4) {
                            transferdata[k] = value.getStringCellValue();
                            System.out.println(transferdata[k]);
                            k++;

                        }
                    else {
                            transferdata[k] = String.valueOf(value.getNumericCellValue());
                            System.out.println(transferdata[k]);
                            k++;

                        }
                    }


                    RestAssured.baseURI = "https://quickdev3.super.one";
                    given().log().all().header("Device-Type", "WEB").header("Content-Type", "application/json")
                            .body(Transferpayload.statuspayload(transferdata[0]))
                            .when().post("/writer/v3/user/checkAccountStatus")
                            .then().log().all().assertThat().statusCode(200).body("message", equalTo("User found."));


//Login
                    String loginresponse = given().log().all().header("Device-Type", "WEB").header("Content-Type", "application/json")
                            .body(Transferpayload.Loginpayload(transferdata[0], transferdata[1]))
                            .when().patch("/writer/user/email/login")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js = Reuseablemethods.rawtojson(loginresponse);
                    String token = js.getString("data.token");
                    System.out.println(token);


                    //Get wallet data

                    String walletdata = given().header("Device-Type", "WEB").header("Token", token)
                            .when().get("/reader/members/get/walletdata")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js1 = Reuseablemethods.rawtojson(walletdata);
                    String freebalance = js1.getString("data.Balance.freeBalance");
                    System.out.println(freebalance);

                    //Login receiver
                    String receiveuserresponse = given().log().all().header("Device-Type", "WEB").header("Content-Type", "application/json")
                            .body(Transferpayload.receiveduser(transferdata[2], transferdata[3]))
                            .when().patch("/writer/user/email/login")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js4 = Reuseablemethods.rawtojson(receiveuserresponse);
                    String receiver_token = js4.getString("data.token");
                   String receiver_referralcode= js4.getString("data.referralCode");
                    System.out.println(receiver_token);

                    //Search Member

                    String memberdetail = given().header("Device-Type", "WEB").header("Token", token)
                            .body(Transferpayload.searchpayload(receiver_referralcode))
                            .when().post("/reader/member/searchmemberbyreferralcode")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js2 = Reuseablemethods.rawtojson(memberdetail);
                    String memberid = js2.getString("data.members[0].id");
                    System.out.println(memberid);


                    //Transfer

                    given().header("Device-Type", "WEB").header("Token", token)
                            .body(Transferpayload.Transferpayload(memberid, (int)Double.parseDouble(transferdata[4])))
                            .when().post("/writer/v3/user/100623/transfer")
                            .then().log().all().assertThat().statusCode(200);

                    //Checking Balance is deducted

                    String wallettrdata = given().header("Device-Type", "WEB").header("Token", token)
                            .when().get("/reader/members/get/walletdata")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js3 = Reuseablemethods.rawtojson(wallettrdata);
                    String freetrbalance = js3.getString("data.Balance.freeBalance");
                    System.out.println(freetrbalance);
//        System.out.println(freebalance);
//        int trbal=Integer.parseInt(freetrbalance);
//        int bal=Integer.parseInt(freebalance);
//        Assert.assertEquals(bal-10,trbal);




                    //Get wallet data

                    String receiverwalletdata = given().header("Device-Type", "WEB").header("Token", receiver_token)
                            .when().get("/reader/members/get/walletdata")
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath js5 = Reuseablemethods.rawtojson(receiverwalletdata);
                    String receiverfreebalance = js5.getString("data.Balance.freeBalance");
                    System.out.println(receiverfreebalance);
                    //int refreebalance=Integer.parseInt(receiverfreebalance);


                }
            }

        }}}