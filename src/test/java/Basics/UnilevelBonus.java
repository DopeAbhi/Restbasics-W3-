package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class UnilevelBonus {

    public static void main(String[] args) {
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY5MzgzMTgzMCwiZXhwIjoxNjk2NDIzODMwfQ.l22R1tUXa9H0y924T2pk8AOLx9lPLyXkMwLxthEQOsE";

        RestAssured.baseURI="https://quickdev3.super.one";

        //Get All Reward from Admin

        Date date=new Date();
        long end=date.getTime();
     String rewardresp=   given().header("device-type","WEB").header("token",token)
                .queryParams("skips",0,"start",0,"end",""+end+"","page_num",25,"memberPackageId","")
                .when().get("/reader/admin/getPayoutsList")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath rewardjson=Reuseablemethods.rawtojson(rewardresp);
        int j=0;

        String rewardtype;
       do {


            rewardtype = rewardjson.getString("data["+j+"].payoutType");
            String profiteer=rewardjson.getString("data["+j+"].profiteerReferralCode");
           String reward=rewardjson.getString("data["+j+"].distributeVolume");



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
           System.out.println(rewardtype);
           System.out.println(reward);
           j++;

        }while (rewardtype.equalsIgnoreCase("UNILEVEL"));



        }

}
