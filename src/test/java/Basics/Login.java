package Basics;

import static io.restassured.RestAssured.given;

import payload.usertreepayload;
import io.restassured.path.json.JsonPath;
import genrics.APIResources;
import genrics.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class Login {

    public static ArrayList<String> Loginfeature(String Email, String Password) throws IOException {     //User Status
        //System.out.println(login);
        ArrayList<String> logindata = new ArrayList<>();

        APIResources apiResources = APIResources.valueOf("user_status_check");
        given().spec(Utils.requestSpecification()).body(usertreepayload.userstatuspayload(Email))
                .when().post(apiResources.getResource()).then().assertThat().statusCode(200);

        apiResources = APIResources.valueOf("login");
        String login_response = given().spec(Utils.requestSpecification()).body(usertreepayload.loginpayload(Email, Password)).
                when().patch(apiResources.getResource()).
                then().assertThat().statusCode(200).extract().response().asString();
        JsonPath login_json = Utils.rawtojson(login_response);
         logindata.add( 0,login_json.getString("data.token"));
        logindata.add(  1,login_json.getString("data.referralCode"));
        return logindata;
    }
    public static void main(String[] args) throws IOException {
        Loginfeature("SBB1@gmail.com","Test@123");

    }
}