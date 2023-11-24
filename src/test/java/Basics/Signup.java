package Basics;


import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.util.ArrayList;
import payload.*;
import genrics.APIResources;
import genrics.Utils;

import static io.restassured.RestAssured.given;
import static genrics.Utils.requestSpecification;

public class Signup {

    public static ArrayList<String> signupfeature(ArrayList<String> Treedata) throws IOException, IOException {
        //Excel Access
        ArrayList<String> signup_user_data = new ArrayList<String>();
        //User Status Check
        APIResources apiResources = APIResources.valueOf("user_status_check");
        String statusresp = given().spec(Utils.requestSpecification())
                .body(usertreepayload.userstatuspayload(Treedata.get(0)))
                .when().post(apiResources.getResource())
                .then().assertThat().statusCode(200).extract().response().asString();//Get Verification URL

//Verification URL
        apiResources = APIResources.valueOf("get_verification_url");
        String verification_url_response = given().spec(requestSpecification()).queryParam("password", "711b525c69e8b0edc6221518b8ff878f")
                .when().get(apiResources.getResource())
                .then().statusCode(200).extract().response().asString();
        JsonPath verification_url_json = Utils.rawtojson(verification_url_response);
        String hash = verification_url_json.getString("data[0].verificationHash");
        System.out.println(hash);
        String verification_token = hash.substring(67);
        System.out.println(verification_token);

        //verify user

        apiResources = APIResources.valueOf("verify_url");
        given().spec(requestSpecification())
                .body(usertreepayload.verifyuserpayload(verification_token))
                .when().post(apiResources.getResource())
                .then().assertThat().statusCode(200);

        //Verification Check
        apiResources = APIResources.valueOf("verification_check");
        String verification_check_response = given().spec(requestSpecification()).queryParam("email", Treedata.get(0))
                .when().get(apiResources.getResource())
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath verification_check_json = Utils.rawtojson(verification_check_response);
        String token = verification_check_json.getString("data.token");
        System.out.println(token);


        //set password
        apiResources = APIResources.valueOf("set_password");
        given().spec(requestSpecification()).header("Token", token)
                .body(usertreepayload.passwordpayload(Treedata.get(1)))
                .when().patch(apiResources.getResource()).
                then().assertThat().statusCode(200);


        //Verify Referral
        apiResources = APIResources.valueOf("verify_referral");
        given().spec(requestSpecification()).header("Token", token)
                .body(usertreepayload.referralpayload(Treedata.get(0), "amrendra"))
                .when().post("/writer/v3/user/verifyReferral")
                .then().assertThat().statusCode(200);

        //Set Username
        apiResources = APIResources.valueOf("set_username");
        given().spec(requestSpecification()).header("Token", token)
                .body(usertreepayload.usernamepayload(Treedata.get(2)))
                .when().patch(apiResources.getResource()).
                then().log().all().assertThat().statusCode(200);

        //Set First and Last Name
        apiResources = APIResources.valueOf("set_firstname_lastname");
        String flresponse = given().spec(requestSpecification())
                .body(usertreepayload.namepayload(Treedata.get(3)))
                .when().put(apiResources.getResource())
                .then().assertThat().statusCode(200).extract().response().asString();
        JsonPath fljson = Utils.rawtojson(flresponse);
        String imageurl = fljson.getString("data.imageUrl");
        int userid = fljson.getInt("data.id");


        //Avatar
        apiResources = APIResources.valueOf("set_avatar");
        String avatar_response = given().spec(requestSpecification()).body(usertreepayload.avatarpayload(imageurl, userid))
                .when().post(apiResources.getResource())
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath avatar_json = Utils.rawtojson(avatar_response);
        String rawreferral = avatar_json.getString("data.referralLink");
        String referral_link = rawreferral.substring((rawreferral.length()) - 10);

        // String referral_link=  avatar_json.getString("data.referralLink").substring((avatar_json.getString("data.referralLink").length()) - 10,  avatar_json.getString("data.referralLink").length());

        signup_user_data.add(0, referral_link);
        System.out.println(referral_link);

        return signup_user_data;// change variable

    }


    public static void main(String[] args) throws IOException {
        ArrayList<String> Treedata = Utils.excelAccess("signup");
        signupfeature(Treedata);
    }
}
