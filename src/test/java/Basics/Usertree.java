package Basics;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import javax.swing.text.html.parser.Parser;
import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class Usertree {

    public static void main(String[] args) {
        String avatarresp;
        String referralLink;

        Scanner scanner=new Scanner(System.in);
        System.out.println("Parent Email Address");
        String parentemail=scanner.next();
        System.out.println("Parent password");
        String parentpassword=scanner.next();



        RestAssured.baseURI="https://quickdev3.super.one";

        //User Status Check
      String statusresp=  given().header("Content-Type","application/json").header("Bypass-W3villa-Areyxukcyb",true).header("Device-Type","WEB")
             .body(usertreepayload.userstatuspayload(parentemail))
              .when().post("/writer/v3/user/checkAccountStatus")
              .then().log().all().assertThat().statusCode(200).extract().response().asString();//Get Verification URL

        JsonPath statusjson=new JsonPath(statusresp);
        boolean status= statusjson.getBoolean("data.islogin");

        if (status)
        {
            System.out.println(status);

            //Intiate Login
            given().header("device-type","WEB")
                    .body(usertreepayload.intiateloginpayload(parentemail))
                    .when().patch("/writer/v2/user/email/initiatelogin")
                    .then().assertThat().statusCode(200);

            //Login

           String loginresp= given().header("device-type","WEB")
                    .body(usertreepayload.loginpayload(parentemail,parentpassword))
                    .when().patch("/writer/user/email/login")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
           JsonPath loginjson=Reuseablemethods.rawtojson(loginresp);
            String rawreferral=loginjson.getString("data.referralLink");
             referralLink=rawreferral.substring((rawreferral.length())-10,rawreferral.length());
            System.out.println(referralLink);


        }

        else {
            //Get Verification URL

            String verifyresponse = given().header("Bypass-W3villa-Areyxukcyb", true).queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                    .when().get("/reader/getVerificationHistory")
                    .then().statusCode(200).extract().response().asString();
            JsonPath js1 = Reuseablemethods.rawtojson(verifyresponse);
            String hash = js1.getString("data[0].verificationHash");
            System.out.println(hash);
            String vertoken = hash.substring(67, hash.length());
            System.out.println(vertoken);

            //verify user

            given().header("device-type", "WEB")
                    .body(usertreepayload.verifyuserpayload(vertoken))
                    .when().post("/writer/v3/user/verifyUserToken")
                    .then().log().all().assertThat().statusCode(200);

            //Verification Check

            String verificationresponse = given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").queryParam("email", parentemail)
                    .when().get("/reader/user/checkVerificationStatus")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js2 = Reuseablemethods.rawtojson(verificationresponse);
            String token = js2.getString("data.token");
            System.out.println(token);

            RequestSpecification req=new RequestSpecBuilder().addHeader("device-type", "WEB").addHeader("token", token)
                            .build(); //Common parameter in single specbuilder class
            //set password
            given().spec(req).body(usertreepayload.passwordpayload(parentpassword))
                    .when().patch("/writer/v3/user/password/set").
                    then().log().all().assertThat().statusCode(200);


            //Verify Referral
            String referral="amrendra";
            given().spec(req).body(usertreepayload.referralpayload(parentemail,referral))
                    .when().post("/writer/v3/user/verifyReferral")
                    .then().log().all().assertThat().statusCode(200);

            //Set Username
            System.out.println("Parent User Name");
            String parentusername=scanner.next();
            given().spec(req).body(usertreepayload.usernamepayload(parentusername))
                    .when().patch("/writer/v3/user/updateUserName").
                    then().log().all().assertThat().statusCode(200);

            //Set First and Last Name
            System.out.println("Parent First Name");
            String parentfirstname=scanner.next();
            String flresponse = given().spec(req).body(usertreepayload.namepayload(parentfirstname))
                    .when().put("/writer/v3/user/100706/updateUserInfo")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js3 = Reuseablemethods.rawtojson(flresponse);

            String imageurl = js3.getString("data.imageUrl");
            int userid = js3.getInt("data.id");

            System.out.println(imageurl);


            //Avatar
           avatarresp=  given().spec(req).body(usertreepayload.avatarpayload(imageurl,userid))
                    .when().post("/writer/user/update-avatar")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
           JsonPath avatarjson=Reuseablemethods.rawtojson(avatarresp);
           String rawreferral=avatarjson.getString("data.referralLink");
           referralLink=rawreferral.substring((rawreferral.length())-10,rawreferral.length());
            System.out.println(referralLink);

            //Add Balance

//            given().header("Bypass-W3villa-Areyxukcyb", true).log().all().header("device-type", "WEB").header("Content-Type", "application/json").header("token", token)
//                    .queryParams("email",""+parentemail+"","amount","100000","password","711b525c69e8b0edc6221518b8ff878f")
//                    .when().get().
//            then().log().all().assertThat().statusCode(200);

        }

        System.out.println("How many Level ");
        int user= scanner.nextInt();
        for (int i = 0; i < user; i++) {


            System.out.println("Child Email");
            String childemail = scanner.next();
            System.out.println("Child password");
            String childpassword=scanner.next();
            System.out.println("Child Username");
            String childusername = scanner.next();
            System.out.println("Child Firstname");
            String childfirstname = scanner.next();


            //  Member Creation
            //User Status Check
            given().header("Content-Type", "application/json").header("device-type", "WEB")
                    .body(usertreepayload.userstatuspayload(childemail)).when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);

            // Get Verification URL

            String treeverifyresponse = given().queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                    .when().get("/reader/getVerificationHistory")
                    .then().statusCode(200).extract().response().asString();
            JsonPath js5 = Reuseablemethods.rawtojson(treeverifyresponse);
            String treehash = js5.getString("data[0].verificationHash");
            System.out.println(treehash);
            String treevertoken = treehash.substring(67, treehash.length());
            System.out.println(treevertoken);


            //Verify User

            given().header("device-type", "WEB")
                    .body(usertreepayload.verifyuserpayload(treevertoken))
                    .when().post("/writer/v3/user/verifyUserToken")
                    .then().log().all().assertThat().statusCode(200);

            //Verification Check

            String treeverificationresponse = given().header("device-type", "WEB").queryParam("email", childemail)
                    .when().get("/reader/user/checkVerificationStatus")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js6 = Reuseablemethods.rawtojson(treeverificationresponse);
            String treetoken = js6.getString("data.token");
            System.out.println(treetoken);

            RequestSpecification treereq=new RequestSpecBuilder().addHeader("device-type", "WEB").addHeader("token", treetoken)
                    .build(); //Common parameter in single specbuilder class


            //Set password
            given().spec(treereq).body(usertreepayload.passwordpayload(childpassword))
                    .when().patch("/writer/v3/user/password/set")
                    .then().log().all().assertThat().statusCode(200);


            //Verify Referral
            given().spec(treereq).body(usertreepayload.referralpayload(childemail,referralLink))
                    .when().post("/writer/v3/user/verifyReferral")
                    .then().log().all().assertThat().statusCode(200);

            //Set Username
            given().spec(treereq).body(usertreepayload.usernamepayload(childusername))
                    .when().patch("/writer/v3/user/updateUserName")
                    .then().log().all().assertThat().statusCode(200);

            //Set First and Last Name

            String treeflresponse = given().spec(treereq).body(usertreepayload.namepayload(childfirstname))
                    .when().put("/writer/v3/user/100706/updateUserInfo")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js7 = Reuseablemethods.rawtojson(treeflresponse);
            Integer treeid = js7.getInt("data.id");
            String treeimageurl = js7.getString("data.imageUrl");
            System.out.println(treeid);
            System.out.println(treeimageurl);


            //Avatar
            String treeavatarresp = given().spec(treereq)
                    .body(usertreepayload.avatarpayload(treeimageurl,treeid))
                    .when().post("/writer/user/update-avatar")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath treeavatarjson = Reuseablemethods.rawtojson(treeavatarresp);
            String rawreferral = treeavatarjson.getString("data.referralLink");
            referralLink = rawreferral.substring((rawreferral.length()) - 10, rawreferral.length());
            System.out.println(referralLink);

            //Add Balance

//            given().header("Bypass-W3villa-Areyxukcyb", true).log().all().header("device-type", "WEB").header("Content-Type", "application/json").header("token", treetoken)
//                    .queryParams("email",""+childemail+"","amount","100000","password","711b525c69e8b0edc6221518b8ff878f")
//                    .when().get().
//                    then().log().all().assertThat().statusCode(200);

        }



    }





}


