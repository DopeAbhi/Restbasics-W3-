package Basics;

import payload.Transferpayload;
import io.restassured.path.json.JsonPath;

import genrics.APIResources;
import genrics.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static genrics.Utils.requestSpecification;

public class Transfer {



    public static void main(String[] args) throws IOException, InterruptedException {

        String requestId = null;

//Data Reading from Excel
       ArrayList<String> transferdata= Utils.excelAccess("Transfer");


                    //Sender Login
                    ArrayList<String> sender_logindata;
                    sender_logindata = Login.Loginfeature(transferdata.get(0), transferdata.get(1));
                    System.out.println(sender_logindata.get(1));


                    //Get wallet data
                    APIResources apiResources = APIResources.valueOf("get_walletdata");
                    String sender_wallet_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath sender_wallet_json = Utils.rawtojson(sender_wallet_response);
                    String sender_free_balance = sender_wallet_json.getString("data.Balance.freeBalance");
                    System.out.println(sender_free_balance);
                    //Checking Sender OTP settings

                    apiResources = APIResources.valueOf("user_settings");
                    String user_settings_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asPrettyString();
                    System.out.println(user_settings_response);
                    JsonPath user_settings_json = Utils.rawtojson(user_settings_response);
                    Integer Withdraw_status = user_settings_json.getInt("data.getallsecuritypreferences.WITHDRAW_TRANSFER");
                    System.out.println(Withdraw_status);


                    //Login receiver
                    ArrayList<String> receiver_logindata;

                    receiver_logindata = Login.Loginfeature(transferdata.get(2), transferdata.get(3));

//                   String receiver_referralcode= js4.getString("data.referralCode");
//                    System.out.println(receiver_token);

                    //Search Member
                    apiResources = APIResources.valueOf("member_search");
                    String member_search_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                            .body(Transferpayload.searchpayload(receiver_logindata.get(1)))
                            .when().post(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath member_search_json = Utils.rawtojson(member_search_response);
                    String receiver_memberid = member_search_json.getString("data.members[0].id");
                    System.out.println(receiver_memberid);

                    if (Withdraw_status.equals(1)) {

                        //Sending OTP
                        apiResources = APIResources.valueOf("send_otp");
                        given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                                .body(Transferpayload.send_otp(transferdata.get(0)))
                                .when().post(apiResources.getResource())
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();
                        //Verify OTP

                        apiResources = APIResources.valueOf("verify_otp");
                        String verify_otp_response = given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                                .body(Transferpayload.verify_otp())
                                .when().patch(apiResources.getResource())
                                .then().log().all().assertThat().statusCode(200).extract().response().asString();
                        JsonPath verify_otp_json = Utils.rawtojson(verify_otp_response);
                        requestId = verify_otp_json.getString("data.requestId");


                    }


                    //Transfer
                    System.out.println(requestId);
                    apiResources = APIResources.valueOf("transfer");
                    given().log().all().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                            .body(Transferpayload.Transferpayload(receiver_memberid,(int) Double.parseDouble(transferdata.get(4)),requestId))
                            .when().post(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200);


                    //Checking Receiver Received Amount
                    apiResources = APIResources.valueOf("get_walletdata");
                    String receiver_wallet_response = given().spec(requestSpecification()).header("Token", receiver_logindata.get(0))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath receiver_wallet_json = Utils.rawtojson(receiver_wallet_response);
                    String receiver_received_amount = receiver_wallet_json.getString("data.Balance.freeBalance");
                    System.out.println(receiver_received_amount);


                    //Checking Balance is deducted from sender

                    apiResources = APIResources.valueOf("get_walletdata");
                    String sender_wallet = given().spec(requestSpecification()).header("Token", sender_logindata.get(0))
                            .when().get(apiResources.getResource())
                            .then().log().all().assertThat().statusCode(200).extract().response().asString();
                    JsonPath sender_wallett = Utils.rawtojson(sender_wallet);
                    String free_balance = sender_wallett.getString("data.Balance.freeBalance");
                    System.out.println(free_balance);

                }
            }

