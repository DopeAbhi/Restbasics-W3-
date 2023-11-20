package Basics;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class OTP_checking {
    public static void main(String[] args) {
        RestAssured.baseURI="https://quickdev3.super.one";

        given().header("Token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEwNTA0OSwiaWF0IjoxNzAwMjE1MTkwLCJleHAiOjE3MDI4MDcxOTB9.R4nrN9R0wGgb1xXMidQuyZto4cRTbgPuZjMZBYXdC54")
                .header("Device-Type","WEB")
                .when().get("/reader/user/105049/setting")
                .then().log().all();
    }
}
