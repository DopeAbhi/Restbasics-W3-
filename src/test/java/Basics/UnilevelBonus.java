package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

import static io.restassured.RestAssured.given;

public class UnilevelBonus {

    public static void main(String[] args) {
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY5MzU4Mzk1MywiZXhwIjoxNjk2MTc1OTUzfQ.ctk6Ydeszk1oMoQLaICIOCP428oPnC5WzTPxdlTfxsM";

        RestAssured.baseURI="https://quickdev3.super.one";

        //Get All Reward from Admin
     String rewardresp=   given().header("device-type","WEB").header("token",token)
                .queryParams("skips",0,"start",0,"end","1693584026585","page_num",25,"memberPackageId","")
                .when().get("/reader/admin/getPayoutsList")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath rewardjson=Reuseablemethods.rawtojson(rewardresp);
        int j=0;

        String re;
       do {


            re = rewardjson.getString("data["+j+"].payoutType");
            String profiteer=rewardjson.getString("data["+j+"].profiteerReferralCode");
           int reward=rewardjson.getInt("data["+j+"].distributeVolume");



     //Search Member

     String searchresp=   given().header("device-type","WEB").header("token",token)
                .queryParams("searchType","referralCode","userSearchByDetail",""+profiteer+"","countrySearchInput","")
                .when().get("/reader/admin/searchUserByReferralCode")
                .then().assertThat().statusCode(200).extract().response().asString();
     JsonPath searchjson=Reuseablemethods.rawtojson(searchresp);
     String highestpack=searchjson.getString("data.details[0].userRank");
     String email=searchjson.getString("data.details[0].email");
           System.out.println(profiteer);
           System.out.println(highestpack);
           System.out.println(email);;
           System.out.println(re);
           System.out.println(reward);
           j++;

        }while (re.equalsIgnoreCase("UNILEVEL"));



        }

}
