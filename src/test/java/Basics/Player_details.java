package Basics;

import payload.usertreepayload;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import genrics.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static io.restassured.RestAssured.given;

public class Player_details {
    public static void main(String[] args) throws IOException {
        int rowNum;


        // Load an existing Excel file
          FileInputStream fis = new FileInputStream("/src/test/java/resources/Superone2.xlsx");
        //FileInputStream fis = new FileInputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone2.xlsx"); //for mac
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Specify the sheet name where you want to add/overwrite data
        String sheetName = "PlayerDetails";

        // Find the specified sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);

        // Check if the sheet exists
        if (sheet == null) {
            System.out.println("Sheet '" + sheetName + "' not found in the Excel file.");
            return;
        }

        // Add data from Rest Assured to Excel
        rowNum = sheet.getLastRowNum() + 1;


            Row row = sheet.createRow(rowNum);


///Login Access

        String avatarresp;
        String [] userdata=new String[10];
        boolean T = true;


        String[] Treedata = new String[4];

        int sheets = workbook.getNumberOfSheets();

        RestAssured.baseURI = "https://quickdev3.super.one";

        //Login
        for (int i = 0; i < sheets; i++) {
            String referralLink = null;

            if (workbook.getSheetName(i).equalsIgnoreCase("UserTree")) {
                XSSFSheet sheet2 = workbook.getSheetAt(i);

                Iterator<Row> rows = sheet2.iterator();// sheet is collection of rows
                rows.next();

                while (rows.hasNext()) {
                    Row row2 = rows.next();


                    Iterator<Cell> ce = row2.cellIterator();
                    int counter = 0;

                    while (ce.hasNext()) {
                        Cell value = ce.next();


                        Treedata[counter] = value.getStringCellValue();

                        System.out.println(Treedata[counter]);
                        counter++;
                    }


                    if (T) {

                        //User Status Check
                        String statusresp = given().header("Content-Type", "application/json").header("Bypass-W3villa-Areyxukcyb", true).header("Device-Type", "WEB")
                                .body(usertreepayload.userstatuspayload(Treedata[0]))
                                .when().post("/writer/v3/user/checkAccountStatus")
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();//Get Verification URL

                        JsonPath statusjson = new JsonPath(statusresp);
                        boolean status = statusjson.getBoolean("data.islogin");

                        if (status) {
                            System.out.println(status);

                            //Intiate Login
                            given().header("device-type", "WEB")
                                    .body(usertreepayload.intiateloginpayload(Treedata[0]))
                                    .when().patch("/writer/v2/user/email/initiatelogin")
                                    .then().assertThat().statusCode(200);

                            //Login
                            String loginresp = given().header("device-type", "WEB")
                                    .body(usertreepayload.loginpayload(Treedata[0], Treedata[1]))
                                    .when().patch("/writer/user/email/login")
                                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
                            JsonPath loginjson = Utils.rawtojson(loginresp);
                            referralLink = loginjson.getString("data.referralCode");
                            userdata[0]= loginjson.getString("data.email");
                            userdata[1]= loginjson.getString("data.firstName");
                            userdata[2]= loginjson.getString("data.lastName");
                            userdata[3] =loginjson.getString("data.userName");
                            userdata[4] =loginjson.getString("data.id");
                            userdata[5]= loginjson.getString("data.member");
                            userdata[6]= loginjson.getString("data.imageUrl");
                            userdata[7]= loginjson.getString("data.countryCode");
                            userdata[8]= loginjson.getString("data.countryName");
                            userdata[9]= loginjson.getString("data.token");

                            System.out.println(referralLink);

                            for (int l = 0; l <userdata.length ; l++) {

                                Cell cell = row.createCell(l); // Specify the cell index (0, 1, 2, ...)
                                // Specify the cell index (0, 1, 2, ...)

                                // Set the cell value (e.g., from your API response)
                                // Example:
                                cell.setCellValue(userdata[l]);


                            }
                            rowNum++;
                            row = sheet.createRow(rowNum);



                        } else {
                            //Get Verification URL

                            String verifyresponse = given().header("Bypass-W3villa-Areyxukcyb", true).queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                                    .when().get("/reader/getVerificationHistory")
                                    .then().statusCode(200).extract().response().asString();
                            JsonPath js1 = Utils.rawtojson(verifyresponse);
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

                            String verificationresponse = given().header("Bypass-W3villa-Areyxukcyb", true).header("device-type", "WEB").queryParam("email", Treedata[0])
                                    .when().get("/reader/user/checkVerificationStatus")
                                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
                            JsonPath js2 = Utils.rawtojson(verificationresponse);
                            String token = js2.getString("data.token");
                            System.out.println(token);

                            RequestSpecification req = new RequestSpecBuilder().addHeader("device-type", "WEB").addHeader("token", token)
                                    .build(); //Common parameter in single specbuilder class
                            //set password
                            given().spec(req).body(usertreepayload.passwordpayload(Treedata[1]))
                                    .when().patch("/writer/v3/user/password/set").
                                    then().log().all().assertThat().statusCode(200);


                            //Verify Referral
                            given().spec(req).body(usertreepayload.referralpayload(Treedata[0], ""))
                                    .when().post("/writer/v3/user/verifyReferral")
                                    .then().log().all().assertThat().statusCode(200);

                            //Set Username
                            given().spec(req).body(usertreepayload.usernamepayload(Treedata[2]))
                                    .when().patch("/writer/v3/user/updateUserName").
                                    then().log().all().assertThat().statusCode(200);

                            //Set First and Last Name
                            String flresponse = given().spec(req).body(usertreepayload.namepayload(Treedata[3]))
                                    .when().put("/writer/v3/user/100706/updateUserInfo")
                                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
                            JsonPath js3 = Utils.rawtojson(flresponse);

                            String imageurl = js3.getString("data.imageUrl");
                            int userid = js3.getInt("data.id");

                            System.out.println(imageurl);


                            //Avatar
                            avatarresp = given().spec(req).body(usertreepayload.avatarpayload(imageurl, userid))
                                    .when().post("/writer/user/update-avatar")
                                    .then().log().all().assertThat().statusCode(200).extract().response().asString();
                            JsonPath avatarjson = Utils.rawtojson(avatarresp);
                            String rawreferral = avatarjson.getString("data.referralLink");
                            referralLink = rawreferral.substring((rawreferral.length()) - 10, rawreferral.length());
                            System.out.println(referralLink);

                            userdata[0]= avatarjson.getString("data.email");
                            userdata[1]= avatarjson.getString("data.firstName");
                            userdata[2]= avatarjson.getString("data.lastName");
                            userdata[3] =avatarjson.getString("data.userName");
                            userdata[4] =avatarjson.getString("data.id");
                            userdata[5]= avatarjson.getString("data.member");
                            userdata[6]= avatarjson.getString("data.imageUrl");
                            userdata[7]= avatarjson.getString("data.countryCode");
                            userdata[8]= avatarjson.getString("data.countryName");
                            userdata[9]= js2.getString("data.token");


                            System.out.println(referralLink);

                            for (int l = 0; l <userdata.length ; l++) {

                                Cell cell = row.createCell(l); // Specify the cell index (0, 1, 2, ...)
                                // Specify the cell index (0, 1, 2, ...)

                                // Set the cell value (e.g., from your API response)
                                // Example:
                                cell.setCellValue(userdata[l]);


                            }
                            rowNum++;
                            row = sheet.createRow(rowNum);

                            //Add Balance

//            given().header("Bypass-W3villa-Areyxukcyb", true).log().all().header("device-type", "WEB").header("Content-Type", "application/json").header("token", token)
//                    .queryParams("email",""+parentemail+"","amount","100000","password","711b525c69e8b0edc6221518b8ff878f")
//                    .when().get().
//            then().log().all().assertThat().statusCode(200);

                        }
                        T = false;
                    } else {

                        //  Member Creation

                        //User Status Check
                        given().header("Content-Type", "application/json").header("device-type", "WEB")
                                .body(usertreepayload.userstatuspayload(Treedata[0])).when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);

                        // Get Verification URL

                        String treeverifyresponse = given().queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                                .when().get("/reader/getVerificationHistory")
                                .then().statusCode(200).extract().response().asString();
                        JsonPath js5 = Utils.rawtojson(treeverifyresponse);
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

                        String treeverificationresponse = given().header("device-type", "WEB").queryParam("email", Treedata[0])
                                .when().get("/reader/user/checkVerificationStatus")
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();
                        JsonPath js6 = Utils.rawtojson(treeverificationresponse);
                        String treetoken = js6.getString("data.token");
                        System.out.println(treetoken);

                        RequestSpecification treereq = new RequestSpecBuilder().addHeader("device-type", "WEB").addHeader("token", treetoken)
                                .build(); //Common parameter in single specbuilder class


                        //Set password
                        given().spec(treereq).body(usertreepayload.passwordpayload(Treedata[1]))
                                .when().patch("/writer/v3/user/password/set")
                                .then().log().all().assertThat().statusCode(200);


                        //Verify Referral
                        given().log().all().spec(treereq).body(usertreepayload.referralpayload(Treedata[0], referralLink))
                                .when().post("/writer/v3/user/verifyReferral")
                                .then().log().all().assertThat().statusCode(200);

                        //Set Username
                        given().spec(treereq).body(usertreepayload.usernamepayload(Treedata[2]))
                                .when().patch("/writer/v3/user/updateUserName")
                                .then().log().all().assertThat().statusCode(200);

                        //Set First and Last Name

                        String treeflresponse = given().spec(treereq).body(usertreepayload.namepayload(Treedata[3]))
                                .when().put("/writer/v3/user/100706/updateUserInfo")
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();
                        JsonPath js7 = Utils.rawtojson(treeflresponse);
                        Integer treeid = js7.getInt("data.id");
                        String treeimageurl = js7.getString("data.imageUrl");
                        System.out.println(treeid);
                        System.out.println(treeimageurl);


                        //Avatar
                        String treeavatarresp = given().spec(treereq)
                                .body(usertreepayload.avatarpayload(treeimageurl, treeid))
                                .when().post("/writer/user/update-avatar")
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();
                        JsonPath treeavatarjson = Utils.rawtojson(treeavatarresp);
                        String rawreferral = treeavatarjson.getString("data.referralLink");
                        referralLink = rawreferral.substring((rawreferral.length()) - 10, rawreferral.length());
                        System.out.println(referralLink);
                        userdata[0]= treeavatarjson.getString("data.email");
                        userdata[1]= treeavatarjson.getString("data.firstName");
                        userdata[2]= treeavatarjson.getString("data.lastName");
                        userdata[3] =treeavatarjson.getString("data.userName");
                        userdata[4] =treeavatarjson.getString("data.id");
                        userdata[5]= treeavatarjson.getString("data.member");
                        userdata[6]= treeavatarjson.getString("data.imageUrl");
                        userdata[7]= treeavatarjson.getString("data.countryCode");
                        userdata[8]= treeavatarjson.getString("data.countryName");
                        userdata[9]= js6.getString("data.token");


                        System.out.println(referralLink);

                        for (int l = 0; l <userdata.length ; l++) {

                            Cell cell = row.createCell(l); // Specify the cell index (0, 1, 2, ...)
                            // Specify the cell index (0, 1, 2, ...)

                            // Set the cell value (e.g., from your API response)
                            // Example:
                            cell.setCellValue(userdata[l]);


                        }
                        rowNum++;
                        row = sheet.createRow(rowNum);



                        T = false;
                    }
                }
               // FileOutputStream fileOutputStream = new FileOutputStream("/Users/abhayverma/IdeaProjects/BasicsofRest/src/test/java/resources/Superone2.xlsx");
                FileOutputStream fileOutputStream = new FileOutputStream("/home/abhay/Restbasics-W3-/src/test/java/resources/Superone2.xlsx");
                workbook.write(fileOutputStream);
                fileOutputStream.close();

                // Close the input stream
                fis.close();


            }


        }
    }

}
