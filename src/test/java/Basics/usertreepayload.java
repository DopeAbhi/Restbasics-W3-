package Basics;

import Pojo.*;

public class usertreepayload {

    public static String userstatuspayload(String email)

    {
        return"{\n" +
                "    \"countryCode\": \"+91\",\n" +
                "    \"countryName\": \"India\",\n" +
                "    \"deviceToken\": \"\",\n" +
                "    \"deviceType\": \"WEB\",\n" +
                "    \"email\": \""+email+"\",\n" +
                "    \"lang\": \"en\",\n" +
                "    \"referenceId\": \"\",\n" +
                "    \"subscribeMarketing\": true\n" +
                "}" ;
    }
    public static String intiateloginpayload(String email)
    {
        return "{\n" +
                "    \"deviceToken\": \"\",\n" +
                "    \"deviceType\": \"WEB\",\n" +
                "    \"email\": \""+email+"\",\n" +
                "    \"password\": \"Test@123\"\n" +
                "}";
    }

    public static Loginpayload loginpayload(String email,String password)
    {
        Loginpayload loginpayload=new Loginpayload();
        loginpayload.setDeviceToken("");
        loginpayload.setDeviceType("WEB");
        loginpayload.setEmail(email);
        loginpayload.setPassword(password);
        loginpayload.setRecaptchaToken("");

        return loginpayload;
    }

public static String verifyuserpayload(String vertoken)
{
    return "{\n" +
            "    \"deviceType\": \"WEB\",\n" +
            "    \"verification_token\": \"" + vertoken + "\"\n" +
            "}";
}

public static String passwordpayload(String password)
{
    return "{\n" +
            "    \"password\": \""+password+"\"\n" +
            "}";
}
public static String referralpayload(String email, String referral)
{
    return "{\n" +
            "    \"email\": \"" + email + "\",\n" +
            "    \"referralCode\": \""+referral+"\"\n" +
            "}";
}
public static String usernamepayload(String username)
{
    return "{\n" +
            "    \"languageCode\": \"en\",\n" +
            "    \"userName\": \"" + username + "\"\n" +
            "}";
}
public static String namepayload (String firstname)
{
    return "{\n" +
            "    \"firstName\": \"" + firstname + "\",\n" +
            "    \"lastName\": \"test\"\n" +
            "}";
}
public static String avatarpayload(String imageurl,int userid)
{
    return "{\n" +
            "    \"avatarUrl\": \"" + imageurl + "\",\n" +
            "    \"mode\": \"AVATAR\",\n" +
            "    \"userId\": " + userid + "\n" +
            "}";
}

public  static String pack_purchase_payload(int packnumber)
{

//    purchasepayload payload=new purchasepayload();
//    payload.setisStore(true);
//    payload.setisUpgrade(false);
//    payload.setpackagePaymentType("CASH");
//    payload.setpackageType("AFFILIATE");
//    payload.setpackageTypeId(packnumber);
//    payload.setpurchaseType("CASH");
//    payload.setquantity(1);
//    payload.setreserve(true);
//    return payload;
    return "{\n" +
            "    \"isStore\": true,\n" +
            "    \"isUpgrade\": false,\n" +
            "    \"packagePaymentType\": \"CASH\",\n" +
            "    \"packageType\": \"AFFILIATE\",\n" +
            "    \"packageTypeId\": "+packnumber+",\n" +
            "    \"purchaseType\": \"CASH\",\n" +
            "    \"quantity\": 1,\n" +
            "    \"reserve\": true\n" +
            "}";
}
}


