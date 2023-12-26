package Basics;

import genrics.APIResources;
import genrics.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Basics.Login.Loginfeature;
import static com.google.common.base.Ascii.equalsIgnoreCase;
import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import payload.Invitepayload;

public class invite {

    ArrayList<String> logindata = new ArrayList<>();
    List<Object> template_data = new ArrayList<>();
    Integer image_id;
    Map<String, Object> inviteImg = new HashMap();

    @Test(groups = {"Regression"}, priority = 5)
    @Parameters({"email", "password", "inviteImageName"})
    public void inviteTest(String email, String password, String imageName) throws IOException {
        logindata = Loginfeature(email, password);
        System.out.println(logindata.get(0));
        //Background Invite Images
        APIResources apiResources = APIResources.valueOf("background_invite_images");
        String back_invite_resp = given().spec(Utils.requestSpecification()).header("Token", logindata.get(0))
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();
        JsonPath back_invite_json = Utils.rawtojson(back_invite_resp);
        String back_image_id = back_invite_json.getString("data[0].uniqueReferenceId");


        apiResources = APIResources.valueOf("invite_images");
        String invite_resp = given().spec(Utils.requestSpecification()).header("Token", logindata.get(0))
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();
        JsonPath invite_images_json = Utils.rawtojson(invite_resp);
        String[] img_data = invite_images_json.getJsonObject("data");
        int length = img_data.length;
        int i = 0;
        while (i < length) {
            if (equalsIgnoreCase(invite_images_json.getString("data.name"), imageName)) {
                image_id = invite_images_json.getInt("data[" + i + "].id");
            }


            template_data.add(back_image_id);
            template_data.add(image_id);
            template_data.add(logindata.get(2));
            template_data.add("Template Creation");
            template_data.add("Test");
            template_data.add(true);
            template_data.add(true);
            template_data.add("Test Title");


            apiResources = APIResources.valueOf("create_template");
            String create_template = given().spec(Utils.requestSpecification()).header("token", logindata.get(0))
                    .body(Invitepayload.template_payload(template_data))
                    .when().post(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();


            i++;
        }
    }
}