package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class Usertree {

    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        String parentemail=scanner.next();
        String parentusername=scanner.next();
        String parentfirstname=scanner.next();

        RestAssured.baseURI="https://quickdev1.super.one";

        //User Status Check
        given().header("Content-Type","application/json").header("Device-Type","WEB")
             .body("{\n" +
                        "    \"countryCode\": \"+91\",\n" +
                        "    \"countryName\": \"India\",\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"lang\": \"en\",\n" +
                        "    \"referenceId\": \"\",\n" +
                        "    \"subscribeMarketing\": true\n" +
                        "}")   .when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);//Get Verification URL


        //Get Verification URL

     String verifyresponse=   given().queryParam("password","711b525c69e8b0edc6221518b8ff878f")
                .when().get("/reader/getVerificationHistory")
                .then().statusCode(200).extract().response().asString();
        JsonPath js1=Reuseablemethods.rawtojson(verifyresponse);
        String hash=js1.getString("data[0].verificationHash");
        System.out.println(hash);
        int length=hash.length();
        System.out.println(length);
       String vertoken= hash.substring(67,length);
        System.out.println(vertoken);

        given().header("Device-Type","WEB")
                .body("{\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"verification_token\": \""+vertoken+"\"\n" +
                        "}")
                .when().post("/writer/v3/user/verifyUserToken")
                .then().log().all().assertThat().statusCode(200);

        String verificationresponse=given().header("Device-Type","WEB").queryParam("email",parentemail)
                .when().get("/reader/user/checkVerificationStatus")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js2=Reuseablemethods.rawtojson(verificationresponse);
        String token=js2.getString("data.token");
        System.out.println(token);

        given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"password\": \"Test@123\"\n" +
                        "}")
                .when().patch("/writer/v3/user/password/set").then().log().all().assertThat().statusCode(200);


        //Verify Referral
        given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"referralCode\": \"amrendra\"\n" +
                        "}")
                .when().post("/writer/v3/user/verifyReferral").then().log().all().assertThat().statusCode(200);

        //Set Username
      given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"languageCode\": \"en\",\n" +
                        "    \"userName\": \""+parentusername+"\"\n" +
                        "}")
                .when().patch("/writer/v3/user/updateUserName").then().log().all().assertThat().statusCode(200);

      //Set First and Last Name

        String flresponse=given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"firstName\": \""+parentfirstname+"\",\n" +
                        "    \"lastName\": \"test\"\n" +
                        "}")
                .when().put("/writer/v3/user/100706/updateUserInfo")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
     JsonPath js3=Reuseablemethods.rawtojson(flresponse);
     String id=js3.getString("data.id");
     String imageurl=js3.getString("data.imageUrl");
        System.out.println(id);
        System.out.println(imageurl);


        //Avatar
        given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"avatarUrl\": \""+imageurl+"\",\n" +
                        "    \"mode\": \"AVATAR\",\n" +
                        "    \"userId\": "+id+"")
        .when().post("/writer/user/update-avatar")
                .then().log().all().assertThat().statusCode(200);

        System.out.println("0-Holding tank \n 1- Powerleg \n 2-Cashleg");
        String legg=scanner.next();

        //Settings
       String settingsresponse= given().header("device-type","WEB").header("token",token)
                .when().post("/writer/user/100622/settings")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
       JsonPath jsonPath=new JsonPath(settingsresponse);
       String leg=jsonPath.getString("data.settings["+legg+"].key");
        System.out.println(leg);

        //Setting leg

       String referralresponse= given().header("device-type","WEB").header("token",token)
                .body("{\n" +
                        "    \"settings\":\""+leg+"\"\n" +
                        "}")
                .when().put("/writer/user/settings/update/100622")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
       JsonPath js4=Reuseablemethods.rawtojson(referralresponse);
       String referrallink=js4.getString("data.referralLink");
        System.out.println(referrallink);






        String childemail=scanner.next();
        String childusername=scanner.next();
        String childfirstname=scanner.next();


                                          // Leg Member Cretion
        //User Status Check
        given().header("Content-Type","application/json").header("Device-Type","WEB")
                .body("{\n" +
                        "    \"countryCode\": \"+91\",\n" +
                        "    \"countryName\": \"India\",\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+childemail+"\",\n" +
                        "    \"lang\": \"en\",\n" +
                        "    \"referenceId\": \"\",\n" +
                        "    \"subscribeMarketing\": true\n" +
                        "}")   .when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);//Get Verification URL


        //Get Verification URL

        String treeverifyresponse=   given().queryParam("password","711b525c69e8b0edc6221518b8ff878f")
                .when().get("/reader/getVerificationHistory")
                .then().statusCode(200).extract().response().asString();
        JsonPath js5=Reuseablemethods.rawtojson(treeverifyresponse);
        String treehash=js5.getString("data[0].verificationHash");
        System.out.println(treehash);
        int treelength=hash.length();
        System.out.println(treelength);
        String treevertoken= hash.substring(67,length);
        System.out.println(treevertoken);



        given().header("Device-Type","WEB")
                .body("{\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"verification_token\": \""+treevertoken+"\"\n" +
                        "}")
                .when().post("/writer/v3/user/verifyUserToken")
                .then().log().all().assertThat().statusCode(200);

        String treeverificationresponse=given().header("Device-Type","WEB").queryParam("email",childemail)
                .when().get("/reader/user/checkVerificationStatus")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js6=Reuseablemethods.rawtojson(treeverificationresponse);
        String treetoken=js6.getString("data.token");
        System.out.println(treetoken);

        given().header("device-type","WEB").header("token",treetoken)
                .body("{\n" +
                        "    \"password\": \"Test@123\"\n" +
                        "}")
                .when().patch("/writer/v3/user/password/set").then().log().all().assertThat().statusCode(200);


        //Verify Referral
        given().header("device-type","WEB").header("token",treetoken)
                .body("{\n" +
                        "    \"email\": \""+childemail+"\",\n" +
                        "    \"referralCode\": \""+referrallink+"\"\n" +
                        "}")
                .when().post("/writer/v3/user/verifyReferral").then().log().all().assertThat().statusCode(200);

        //Set Username
        given().header("device-type","WEB").header("token",treetoken)
                .body("{\n" +
                        "    \"languageCode\": \"en\",\n" +
                        "    \"userName\": \""+childusername+"\"\n" +
                        "}")
                .when().patch("/writer/v3/user/updateUserName").then().log().all().assertThat().statusCode(200);

        //Set First and Last Name

        String treeflresponse=given().header("device-type","WEB").header("token",treetoken)
                .body("{\n" +
                        "    \"firstName\": \""+childfirstname+"\",\n" +
                        "    \"lastName\": \"test\"\n" +
                        "}")
                .when().put("/writer/v3/user/100706/updateUserInfo")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js7=Reuseablemethods.rawtojson(treeflresponse);
        String treeid=js7.getString("data.id");
        String treeimageurl=js7.getString("data.imageUrl");
        System.out.println(treeid);
        System.out.println(treeimageurl);


        //Avatar
        given().header("device-type","WEB").header("token",treetoken)
                .body("{\n" +
                        "    \"avatarUrl\": \""+treeimageurl+"\",\n" +
                        "    \"mode\": \"AVATAR\",\n" +
                        "    \"userId\": 100707")
                .when().post("/writer/user/update-avatar")
                .then().log().all().assertThat().statusCode(200);





    }





}


