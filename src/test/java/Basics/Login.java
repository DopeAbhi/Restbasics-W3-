package Basics;

import io.restassured.RestAssured;

import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class Login {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Parent Email Address");
        String parentemail=scanner.next();


        RestAssured.baseURI="https://quickdev1.super.one";

        //User Status Check
        given().header("Content-Type","application/json").header("Bypass-W3villa-Areyxukcyb",true).header("Device-Type","WEB")
                .body("{\n" +
                        "    \"countryCode\": \"+91\",\n" +
                        "    \"countryName\": \"India\",\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"lang\": \"en\",\n" +
                        "    \"referenceId\": \"\",\n" +
                        "    \"subscribeMarketing\": true\n" +
                        "}")   .when().post("/writer/v3/user/checkAccountStatus").then().log().all().assertThat().statusCode(200);

        given().header("Content-Type","application/json").header("Bypass-W3villa-Areyxukcyb",true).header("Device-Type","WEB")
                .body("{\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"password\": \"Test@123\"\n" +
                        "}")
                .when().patch("/writer/v2/user/email/initiatelogin").then().log().all().assertThat().statusCode(200);


        given().header("Content-Type","application/json").header("Bypass-W3villa-Areyxukcyb",true).header("Device-Type","WEB")
                .body("{\n" +
                        "    \"deviceToken\": \"\",\n" +
                        "    \"deviceType\": \"WEB\",\n" +
                        "    \"email\": \""+parentemail+"\",\n" +
                        "    \"password\": \"Test@123\",\n" +
                        "    \"recaptchaToken\": \"03ADUVZwCNQsIxAE7YRt-Mlv-CjLozA7qo4ExCFcydJtQ3wwHdlRbb9TcTB0T_msS4QJCAfdatMry0v_5yAEsqn68c0rxgiuc7HE5y-3PA2TAkxo-5e3S7VJDoGtdVYrGmlKGdgdNCXdXPZeFTtQxnLOdkPNHlqqWZuLbVp2-07XBKm2-RvU3_Lxl4_8TjQAOxQ8xJFy1kj3YWOUo0-FVuIQq_BZBzoxQohBtin_85AYuHrDZFIm7BjYv_z-ob0O8zLZFlDmwWNulV-G75jcFPiLHWPNmwo9qgwMDDG1k-T2cTfovaTiBTWYq3UkahqjEIWpZrCeKDKfb9PAwg-_F11kaL-gOggJ70PiYJQpLtXnFihpwYC3eREqPb8bKcPlvAX9-q_C3ZHVmVHeAw3vPVbtYlrZehJGNi2zlxgvQcn7X4G9I8e5Mzp15_bbugNPUSxJ7dYvJJFTVYAs4BbPppAJXVrF6y_Cc7ZkCriAWs4V3Z-7Bt4ANoVjg-VkzPCsazmoP3VdtTSsGBQlAw_qKG-ETBXbfITBFxCxunlzsZGK06lX_PD1tRiac\"\n" +
                        "}")
                .when().patch("/writer/user/email/login").then().log().all().assertThat().statusCode(200);

    }
}
