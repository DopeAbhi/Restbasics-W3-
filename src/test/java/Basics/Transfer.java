package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;
import net.openhft.chronicle.core.values.StringValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import resources.APIResources;

import javax.swing.text.html.parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.equalTo;
import static resources.Utils.requestSpecification;

public class Transfer {



    public static void main(String[] args) throws IOException, InterruptedException {
        String transferdata[]=new String[5];

//Data Reading from Excel
        FileInputStream fis = new FileInputStream("/home/abhay/IdeaProjects/Restbasics-W3-/src/test/java/resources/Superone.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheets = workbook.getNumberOfSheets();
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



                //Sender Login
                    ArrayList<String> sender_logindata;
              sender_logindata= Login.Loginfeature(transferdata[0],transferdata[1]);
                    System.out.println(sender_logindata.get(1));


                    //Get wallet data
                    APIResources apiResources=APIResources.valueOf("get_walletdata");
                    String sender_wallet_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(1))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath sender_wallet_json = Reuseablemethods.rawtojson(sender_wallet_response);
                    String sender_free_balance = sender_wallet_json.getString("data.Balance.freeBalance");
                    System.out.println(sender_free_balance);

                    //Login receiver
                    ArrayList<String> receiver_logindata;

                     receiver_logindata=Login.Loginfeature(transferdata[2], transferdata[3]);
                    
//                   String receiver_referralcode= js4.getString("data.referralCode");
//                    System.out.println(receiver_token);

                    //Search Member
                    apiResources=APIResources.valueOf("member_search");
                    String member_search_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(1))
                            .body(Transferpayload.searchpayload(receiver_logindata.get(2)))
                            .when().post(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath member_search_json = Reuseablemethods.rawtojson(member_search_response);
                    String receiver_memberid = member_search_json.getString("data.members[0].id");
                    System.out.println(receiver_memberid);


                    //Transfer
                    apiResources=APIResources.valueOf("transfer");
                    given().spec(requestSpecification()).header("Token", sender_logindata.get(1))
                            .body(Transferpayload.Transferpayload(receiver_memberid, (int)Double.parseDouble(transferdata[4])))
                            .when().post(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200);
                    


                    //Checking Receiver Received Amount
                    apiResources= APIResources.valueOf("get_walletdata");
                    String receiver_wallet_response = given().spec(requestSpecification()).header("Token", receiver_logindata.get(1))
                           .when().get(apiResources.getResource())
                           .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath receiver_wallet_json = Reuseablemethods.rawtojson(receiver_wallet_response);
                    String receiver_received_amount = receiver_wallet_json.getString("data.Balance.freeBalance");
                    System.out.println(receiver_received_amount);

                    

                    //Checking Balance is deducted from sender

                   apiResources=APIResources.valueOf("get_walletdata");
                    String sender_wallet = given().spec(requestSpecification()).header("Token", sender_logindata.get(1))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath sender_wallett = Reuseablemethods.rawtojson(sender_wallet);
                    String free_balance = sender_wallett.getString("data.Balance.freeBalance");
                    System.out.println(free_balance);

                }
            }

        }}}