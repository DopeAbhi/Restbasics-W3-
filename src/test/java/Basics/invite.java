package Basics;

import genrics.APIResources;
import genrics.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Basics.Login.Loginfeature;
import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import payload.Invitepayload;
public class invite {

    ArrayList<String> logindata=new ArrayList<>();
    List<Object> template_data=new ArrayList<>();
    public void inviteTest() throws IOException {
     logindata=   Loginfeature("SBB2@gmail.com","Test@123");

     //Background Invite Images
        APIResources apiResources = APIResources.valueOf("background_invite_images");
        String back_invite_resp= given().spec(Utils.requestSpecification()).queryParam("token",logindata.getFirst())
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();

   apiResources = APIResources.valueOf("invite_images");
        String invite_resp= given().spec(Utils.requestSpecification()).queryParam("token",logindata.getFirst())
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();
        JsonPath invite_images_json =Utils.rawtojson(invite_resp);
      Integer image_id=invite_images_json.getInt("data[0].id");

        apiResources = APIResources.valueOf("create_template");
        String create_template= given().spec(Utils.requestSpecification()).queryParam("token",logindata.getFirst())
                .body(Invitepayload.template_payload())
                .when().post(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();


    }
}
