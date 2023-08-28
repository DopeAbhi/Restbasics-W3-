package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

import javax.swing.text.html.parser.Parser;

import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class Transfer {



    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Sender Email");
        String senderemail= scanner.next();
        System.out.println("Sender Password");
        String senderpass=scanner.next();
        System.out.println("User to be Searched");
        String searchuser=scanner.next();
        System.out.println("Receiver Email ");
        String receiveduser=scanner.next();
        System.out.println("Enter receiver Password");
        String receivepass=scanner.next();
        System.out.println("Amount to be Transfer");
        int amount=scanner.nextInt();
        //Check User exist
        RestAssured.baseURI="https://quickdev1.super.one";
        given().log().all().header("Device-Type","WEB").header("Content-Type","application/json")
                .body(Transferpayload.statuspayload(senderemail))
                .when().post("/writer/v3/user/checkAccountStatus")
                .then().log().all().assertThat().statusCode(200).body("message",equalTo("User found."));

//Login
       String loginresponse= given().log().all().header("Device-Type","WEB").header("Content-Type","application/json")
                .body(Transferpayload.Loginpayload(senderemail,senderpass))
                .when().patch("/writer/user/email/login")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js =Reuseablemethods.rawtojson(loginresponse);
        String token=js.getString("data.token");
        System.out.println(token);


      //Get wallet data

        String walletdata=given().header("Device-Type","WEB").header("Token",token)
                .when().get("/reader/members/get/walletdata")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js1=Reuseablemethods.rawtojson(walletdata);
      String freebalance=js1.getString("data.Balance.freeBalance");
        System.out.println(freebalance);



        //Search Member

      String memberdetail  =given().header("Device-Type","WEB").header("Token",token)
                .body(Transferpayload.searchpayload(searchuser))
                .when().post("/reader/member/searchmemberbyreferralcode")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js2=Reuseablemethods.rawtojson(memberdetail);
        String memberid=js2.getString("data.members[0].id");
        System.out.println(memberid);



        //Transfer

        given().header("Device-Type","WEB").header("Token",token)
                .body(Transferpayload.Transferpayload(memberid,amount))
                .when().post("/writer/v3/user/100623/transfer")
                .then().log().all().assertThat().statusCode(200);

        //Checking Balance is deducted

        String wallettrdata=given().header("Device-Type","WEB").header("Token",token)
                .when().get("/reader/members/get/walletdata")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js3=Reuseablemethods.rawtojson(wallettrdata);
        String freetrbalance=js3.getString("data.Balance.freeBalance");
              System.out.println(freetrbalance);
//        System.out.println(freebalance);
//        int trbal=Integer.parseInt(freetrbalance);
//        int bal=Integer.parseInt(freebalance);
//        Assert.assertEquals(bal-10,trbal);


        //Checking for received  user
        String receiveuserresponse= given().log().all().header("Device-Type","WEB").header("Content-Type","application/json")
                .body(Transferpayload.receiveduser(receiveduser,receivepass))
                .when().patch("/writer/user/email/login")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js4 =Reuseablemethods.rawtojson(receiveuserresponse);
        String toke=js4.getString("data.token");
        System.out.println(toke);


        //Get wallet data

        String receiverwalletdata=given().header("Device-Type","WEB").header("Token",toke)
                .when().get("/reader/members/get/walletdata")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js5=Reuseablemethods.rawtojson(receiverwalletdata);
        String receiverfreebalance=js5.getString("data.Balance.freeBalance");
        System.out.println(receiverfreebalance);
        //int refreebalance=Integer.parseInt(receiverfreebalance);






    }
}
