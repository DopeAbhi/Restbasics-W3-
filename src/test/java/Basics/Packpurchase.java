package Basics;


import payload.usertreepayload;

import resources.APIResources;
import resources.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static resources.Utils.requestSpecification;


public class Packpurchase {


        public static void main(String[] args) throws IOException {


            ArrayList<String> userdata = new ArrayList<String>();
       userdata=Utils.excelAccess("Packpurchase");
       ArrayList<String>sender_login_data=new ArrayList<String>();

       //User Login
       sender_login_data=Login.Loginfeature(userdata.get(0),userdata.get(1));
            //Pack Purchase
            APIResources apiResources= APIResources.valueOf("pack_purchase");
            String purchaseresp = given().spec(requestSpecification()).header("token", sender_login_data.get(0))
                    .body(usertreepayload.pack_purchase_payload((int) Double.parseDouble(userdata.get(2))))
                    .when().patch(apiResources.getResource()).
                    then().assertThat().statusCode(200).extract().response().asString();


        }
}






