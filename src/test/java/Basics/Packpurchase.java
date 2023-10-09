package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.Scanner;


import static io.restassured.RestAssured.given;

public class Packpurchase extends  exceltest {

    /*
    1-Entry
    2-Bronze
    3-Silver
    4-Gold
    7-Platinum
    9-Diamond
    */
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Parent Email Address");
        String parentemail=scanner.next();
        System.out.println("Parent password");
        String parentpassword=scanner.next();
        System.out.println("Pack number");
        int packnumber=scanner.nextInt();


        RestAssured.baseURI="https://quickdev3.super.one";


            //Intiate Login
            given().header("device-type", "WEB")
                    .body(usertreepayload.intiateloginpayload(parentemail))
                    .when().patch("/writer/v2/user/email/initiatelogin")
                    .then().assertThat().statusCode(200);
            //Login

            String loginresp = given().header("device-type", "WEB")
                    .body(usertreepayload.loginpayload(parentemail, parentpassword))
                    .when().patch("/writer/user/email/login")
                    .then().assertThat().statusCode(200).extract().response().asString();
            JsonPath loginjson = Reuseablemethods.rawtojson(loginresp);
            String token = loginjson.getString("data.token");
            System.out.println(token);

            String purchaseresp = given().header("token", token).header("device-type", "WEB")
                    .body(usertreepayload.pack_purchase_payload(packnumber))
                    .when().patch("/writer/member/package/103138").
                    then().log().all().assertThat().statusCode(200).extract().response().asString();


        }

    }

