package Basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class Basics extends payload {

    //given-all input details
    //when-submit the api (path,type of request)
    //then-contains all assertionss
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);

        System.out.println("Enter Name");
        String name= scanner.next();
        System.out.println("Enter phone number");
        String phoneno= scanner.next();



        RestAssured.baseURI="https://rahulshettyacademy.com";
     String response=   given().log().all().queryParam("key","qaclick123").header("Content-type","application/json")
//             .body("{\n" +
//                        "  \"location\": {\n" +
//                        "    \"lat\": -38.383494,\n" +
//                        "    \"lng\": 33.427362\n" +
//                        "  },\n" +
//                        "  \"accuracy\": 50,\n" +
//                        "  \"name\": \"dada Nan\",\n" +
//                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
//                        "  \"address\": \"29, side layout, cohen 09\",\n" +
//                        "  38.\"types\": [\n" +
//                        "    \"shoe park\",\n" +
//                        "    \"shop\"\n" +
//                        "  ],\n" +
//                        "  \"website\": \"http://google.com\",\n" +
//                        "  \"language\": \"French-IN\"\n" +
//                        "}\n" +
//                        "\n")
            .body(payload.Addplace(name,phoneno))
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString();
        System.out.println(response);
        JsonPath js=new JsonPath(response); //for parsing json
        String placeId=js.getString("place_id");
        System.out.println(placeId);

        //Update Place
        Scanner scan=new Scanner(System.in);
        String newAddress=scan.next();
      //  String newAddress="70 Summer walk, USA";

        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"name\": \"Test\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));


        //get place
        String res=given().log().all().queryParam("key","qaclick123").queryParam("place_id",placeId)
        .when().get("maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js1=Reuseablemethods.rawtojson(res);
        //JsonPath js1=new JsonPath(res);
       String respadd= js1.getString("address");
//       Float latt= js1.getFloat("location.lat");
//        System.out.println(latt);
//       Float lngg= js1.getFloat("location.lng");
       String namee= js1.getString("name");
       String phone= js1.getString("phone_number");


        System.out.printf(respadd);
     //   Assert.assertEquals(newAddress,respadd);

        Assert.assertEquals(name,namee);
        Assert.assertEquals(phoneno,phone);






    }
}

//
//print number of courses returned by API
//print purchasse amount
//print title of the first course
//Print title of all the courses and their respective prices
//print number of copies sold by selected course
//verify sum of all the course prices is equal to the purchase amount