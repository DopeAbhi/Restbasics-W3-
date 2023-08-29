package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import javax.swing.text.html.parser.Parser;
import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class Usertree {

    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        System.out.println("Parent Email Address");
        String parentemail=scanner.next();
        System.out.println("Parent User Name");
        String parentusername=scanner.next();
        System.out.println("Parent First Name");
        String parentfirstname=scanner.next();

        RestAssured.baseURI="https://quickdev3.super.one";

        //User Status Check
      String statusresp=  given().header("Content-Type","application/json").header("Bypass-W3villa-Areyxukcyb",true).header("Device-Type","WEB")
             .body("{\n" +
                        "    \"countryCode\": \"+91\",\n" +
                        "    \"countryName\": \"India\",\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"lang\": \"en\",\n" +
                        "    \"referenceId\": \"\",\n" +
                        "    \"subscribeMarketing\": true\n" +
                        "}")   .when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200)
              .extract().response().asString();//Get Verification URL
        JsonPath statusjson=new JsonPath(statusresp);
        boolean status= statusjson.getBoolean("data.islogin");
        if (status)
        {
            System.out.println(status);

            //Intiate Login
            given().header("device-type","WEB")
                    .body("{\n" +
                            "    \"deviceToken\": \"\",\n" +
                            "    \"deviceType\": \"WEB\",\n" +
                            "    \"email\": \""+parentemail+"\",\n" +
                            "    \"password\": \"Test@123\"\n" +
                            "}")
                    .when().patch("/writer/v2/user/email/initiatelogin")
                    .then().assertThat().statusCode(200);
            //Login

           String loginresp= given().header("device-type","WEB")
                    .body("{\n" +
                            "    \"deviceToken\": \"\",\n" +
                            "    \"deviceType\": \"WEB\",\n" +
                            "    \"email\": \""+parentemail+"\",\n" +
                            "    \"password\": \"Test@123\",\n" +
                            "    \"recaptchaToken\": \"03ADUVZwCNQsIxAE7YRt-Mlv-CjLozA7qo4ExCFcydJtQ3wwHdlRbb9TcTB0T_msS4QJCAfdatMry0v_5yAEsqn68c0rxgiuc7HE5y-3PA2TAkxo-5e3S7VJDoGtdVYrGmlKGdgdNCXdXPZeFTtQxnLOdkPNHlqqWZuLbVp2-07XBKm2-RvU3_Lxl4_8TjQAOxQ8xJFy1kj3YWOUo0-FVuIQq_BZBzoxQohBtin_85AYuHrDZFIm7BjYv_z-ob0O8zLZFlDmwWNulV-G75jcFPiLHWPNmwo9qgwMDDG1k-T2cTfovaTiBTWYq3UkahqjEIWpZrCeKDKfb9PAwg-_F11kaL-gOggJ70PiYJQpLtXnFihpwYC3eREqPb8bKcPlvAX9-q_C3ZHVmVHeAw3vPVbtYlrZehJGNi2zlxgvQcn7X4G9I8e5Mzp15_bbugNPUSxJ7dYvJJFTVYAs4BbPppAJXVrF6y_Cc7ZkCriAWs4V3Z-7Bt4ANoVjg-VkzPCsazmoP3VdtTSsGBQlAw_qKG-ETBXbfITBFxCxunlzsZGK06lX_PD1tRiac\"\n" +
                            "}")
                    .when().patch("/writer/user/email/login")
                    .then().assertThat().statusCode(200).extract().response().asString();
           JsonPath loginjson=Reuseablemethods.rawtojson(loginresp);
        String token=  loginjson.getString("data.token");

        //Referral
       String referralresp= given().header("token",token).header("device-type","WEB").
                body("{\n" +
                        "    \"settings\":\"PAYLEGSTATIC\"\n" +
                        "}")
                .when().put("/writer/user/settings/update/100622")
                .then().assertThat().statusCode(200).extract().response().asString();

       JsonPath referraljson=Reuseablemethods.rawtojson(referralresp);
       String referrallink=referraljson.getString("data.referralLink");
            System.out.println(referrallink);





        }

        else {


            //Get Verification URL

            String verifyresponse = given().header("Bypass-W3villa-Areyxukcyb", true).queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                    .when().get("/reader/getVerificationHistory")
                    .then().statusCode(200).extract().response().asString();
            JsonPath js1 = Reuseablemethods.rawtojson(verifyresponse);
            String hash = js1.getString("data[0].verificationHash");
            System.out.println(hash);
            int length = hash.length();
            System.out.println(length);
            String vertoken = hash.substring(67, length);
            System.out.println(vertoken);

            given().header("Device-Type", "WEB")
                    .body("{\n" +
                            "    \"deviceType\": \"WEB\",\n" +
                            "    \"verification_token\": \"" + vertoken + "\"\n" +
                            "}")
                    .when().post("/writer/v3/user/verifyUserToken")
                    .then().log().all().assertThat().statusCode(200);

            //Verification Check

            String verificationresponse = given().header("Bypass-W3villa-Areyxukcyb", true).header("Device-Type", "WEB").queryParam("email", parentemail)
                    .when().get("/reader/user/checkVerificationStatus")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js2 = Reuseablemethods.rawtojson(verificationresponse);
            String token = js2.getString("data.token");
            System.out.println(token);

            given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").header("token", token)
                    .body("{\n" +
                            "    \"password\": \"Test@123\"\n" +
                            "}")
                    .when().patch("/writer/v3/user/password/set").then().log().all().assertThat().statusCode(200);


            //Verify Referral
            given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").header("token", token)
                    .body("{\n" +
                            "    \"email\": \"" + parentemail + "\",\n" +
                            "    \"referralCode\": \"oL34oVxbTy\"\n" +
                            "}")
                    .when().post("/writer/v3/user/verifyReferral").then().log().all().assertThat().statusCode(200);

            //Set Username
            given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").header("token", token)
                    .body("{\n" +
                            "    \"languageCode\": \"en\",\n" +
                            "    \"userName\": \"" + parentusername + "\"\n" +
                            "}")
                    .when().patch("/writer/v3/user/updateUserName").then().log().all().assertThat().statusCode(200);

            //Set First and Last Name

            String flresponse = given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").header("token", token)
                    .body("{\n" +
                            "    \"firstName\": \"" + parentfirstname + "\",\n" +
                            "    \"lastName\": \"test\"\n" +
                            "}")
                    .when().put("/writer/v3/user/100706/updateUserInfo")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath js3 = Reuseablemethods.rawtojson(flresponse);

            String imageurl = js3.getString("data.imageUrl");
            Integer userid = js3.getInt("data.id");

            System.out.println(imageurl);


            //Avatar
            given().header("Bypass-W3villa-Areyxukcyb", true).log().all().header("device-type", "WEB").header("Content-Type", "application/json").header("token", token)

                    .body("{\n" +
                            "    \"avatarUrl\": \"" + imageurl + "\",\n" +
                            "    \"mode\": \"AVATAR\",\n" +
                            "    \"userId\": " + userid + "\n" +
                            "}")
                    .when().post("/writer/user/update-avatar")
                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
        }


//        System.out.println("0-Holding tank \n 1- Powerleg \n 2-Cashleg");
//        Integer legg=scanner.nextInt();
//
//            //Settings
//            String settingsresponse = given().log().all().header("device-type", "WEB").header("token", token)
//                    .body("{}")
//                    .when().post("/writer/user/100622/settings")
//                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
//            JsonPath jsonPath = new JsonPath(settingsresponse);
//            String leg = jsonPath.getString("data.settings[1].key");
//            System.out.println(leg);
//
//            //Setting leg
//
//            String referralresponse = given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").header("token", token)
//                    .body("{\n" +
//                            "    \"settings\":\"PAYLEGSTATIC\"\n" +
//                            "}")
//                    .when().put("/writer/user/settings/update/" + userid + "")
//                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
//            JsonPath js4 = Reuseablemethods.rawtojson(referralresponse);
//            String referrallink = js4.getString("data.referralLink");
//            System.out.println(referrallink);
//        }
////
//            String childemail = scanner.next();
//            String childusername = scanner.next();
//            String childfirstname = scanner.next();



//                                          // Leg Member Cretion
//        //User Status Check
//        given().header("Content-Type","application/json").header("Device-Type","WEB")
//                .body("{\n" +
//                        "    \"countryCode\": \"+91\",\n" +
//                        "    \"countryName\": \"India\",\n" +
//                        "    \"deviceToken\": \"\",\n" +
//                        "    \"deviceType\": \"WEB\",\n" +
//                        "    \"email\": \""+childemail+"\",\n" +
//                        "    \"lang\": \"en\",\n" +
//                        "    \"referenceId\": \"\",\n" +
//                        "    \"subscribeMarketing\": true\n" +
//                        "}")   .when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);

        //Get Verification URL
//
//        String treeverifyresponse=   given().queryParam("password","711b525c69e8b0edc6221518b8ff878f")
//                .when().get("/reader/getVerificationHistory")
//                .then().statusCode(200).extract().response().asString();
//        JsonPath js5=Reuseablemethods.rawtojson(treeverifyresponse);
//        String treehash=js5.getString("data[0].verificationHash");
//        System.out.println(treehash);
//        int treelength=hash.length();
//        System.out.println(treelength);
//        String treevertoken= hash.substring(67,length);
//        System.out.println(treevertoken);
//
//
//
//        given().header("Device-Type","WEB")
//                .body("{\n" +
//                        "    \"deviceType\": \"WEB\",\n" +
//                        "    \"verification_token\": \""+treevertoken+"\"\n" +
//                        "}")
//                .when().post("/writer/v3/user/verifyUserToken")
//                .then().log().all().assertThat().statusCode(200);
//
//        String treeverificationresponse=given().header("Device-Type","WEB").queryParam("email",childemail)
//                .when().get("/reader/user/checkVerificationStatus")
//                .then().log().all().assertThat().statusCode(200).extract().response().asString();
//        JsonPath js6=Reuseablemethods.rawtojson(treeverificationresponse);
//        String treetoken=js6.getString("data.token");
//        System.out.println(treetoken);
//
//        given().header("device-type","WEB").header("token",treetoken)
//                .body("{\n" +
//                        "    \"password\": \"Test@123\"\n" +
//                        "}")
//                .when().patch("/writer/v3/user/password/set").then().log().all().assertThat().statusCode(200);
//
//
//        //Verify Referral
//        given().header("device-type","WEB").header("token",treetoken)
//                .body("{\n" +
//                        "    \"email\": \""+childemail+"\",\n" +
//                        "    \"referralCode\": \""+referrallink+"\"\n" +
//                        "}")
//                .when().post("/writer/v3/user/verifyReferral").then().log().all().assertThat().statusCode(200);
//
//        //Set Username
//        given().header("device-type","WEB").header("token",treetoken)
//                .body("{\n" +
//                        "    \"languageCode\": \"en\",\n" +
//                        "    \"userName\": \""+childusername+"\"\n" +
//                        "}")
//                .when().patch("/writer/v3/user/updateUserName").then().log().all().assertThat().statusCode(200);
//
//        //Set First and Last Name
//
//        String treeflresponse=given().header("device-type","WEB").header("token",treetoken)
//                .body("{\n" +
//                        "    \"firstName\": \""+childfirstname+"\",\n" +
//                        "    \"lastName\": \"test\"\n" +
//                        "}")
//                .when().put("/writer/v3/user/100706/updateUserInfo")
//                .then().log().all().assertThat().statusCode(200).extract().response().asString();
//        JsonPath js7=Reuseablemethods.rawtojson(treeflresponse);
//        Integer treeid=js7.getInt("data.id");
//        String treeimageurl=js7.getString("data.imageUrl");
//        System.out.println(treeid);
//        System.out.println(treeimageurl);
//
//
//        //Avatar
//        given().header("device-type","WEB").header("token",treetoken)
//                .body("{\n" +
//                        "    \"avatarUrl\": \""+treeimageurl+"\",\n" +
//                        "    \"mode\": \"AVATAR\",\n" +
//                        "    \"userId\": "+treeid+"\n" +
//                        "}")
//                .when().post("/writer/user/update-avatar")
//                .then().log().all().assertThat().statusCode(200);
//
//



    }





}


