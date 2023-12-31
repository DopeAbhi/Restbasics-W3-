package Basics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
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
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import payload.Invitepayload;

public class invite {

    ArrayList<String> logindata = new ArrayList<>();
    List<Object> template_data = new ArrayList<>();
    Integer image_id;
    String back_image_id;
    Map<String, Object> inviteImg = new HashMap<>();

    @Test(groups = {"Regression"}, priority = 1)
    @Parameters({"email","password","inviteImageName","backInviteImage"})
    public void inviteTest(String email, String password, String imageName, String back_image_name) throws IOException {
        logindata = Loginfeature(email, password);
        System.out.println(logindata.get(0));
        //Background Invite Images
        APIResources apiResources = APIResources.valueOf("background_invite_images");
        String back_invite_resp = given().spec(Utils.requestSpecification()).header("Token", logindata.get(0))
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();
        JsonPath back_invite_json = Utils.rawtojson(back_invite_resp);



        apiResources = APIResources.valueOf("invite_images");
        String invite_resp = given().spec(Utils.requestSpecification()).header("Token", logindata.get(0))
                .when().get(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();
        JsonPath invite_images_json = Utils.rawtojson(invite_resp);
//        System.out.println(invite_images_json.getJsonObject("data").getClass().getName());
//
        ArrayList<Object> img_data=  invite_images_json.getJsonObject("data");

    int length = img_data.size();
        int i = 0;
        while (i < length) {
            if (equalsIgnoreCase(invite_images_json.getString("data["+i+"].name"), imageName)) {
                image_id = invite_images_json.getInt("data["+i+"].id");
                System.out.println(image_id);
            }
                if(equalsIgnoreCase(back_invite_json.getString("data["+i+"].name"),back_image_name))
                {
                    back_image_id=back_invite_json.getString("data["+i+"].uniqueReferenceId");
                    System.out.println(back_image_id);
                }

            i++;
        }

            template_data.add(back_image_id);
            template_data.add(image_id);
            template_data.add(logindata.get(2));
            template_data.add("8Jan Template");
            template_data.add("8 Jan Test");
            template_data.add(true);
            template_data.add(true);
            template_data.add("Test Title");


            apiResources = APIResources.valueOf("create_template");
            String create_template = given().spec(Utils.requestSpecification()).header("token", logindata.get(0))
                    .body(Invitepayload.template_payload(template_data))
                    .when().post(apiResources.getResource()).then().assertThat().statusCode(200).extract().response().asString();



    }
}