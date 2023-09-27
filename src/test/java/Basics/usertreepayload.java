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
        loginpayload.setRecaptchaToken("03ADUVZwCNQsIxAE7YRt-Mlv-CjLozA7qo4ExCFcydJtQ3wwHdlRbb9TcTB0T_msS4QJCAfdatMry0v_5yAEsqn68c0rxgiuc7HE5y-3PA2TAkxo-5e3S7VJDoGtdVYrGmlKGdgdNCXdXPZeFTtQxnLOdkPNHlqqWZuLbVp2-07XBKm2-RvU3_Lxl4_8TjQAOxQ8xJFy1kj3YWOUo0-FVuIQq_BZBzoxQohBtin_85AYuHrDZFIm7BjYv_z-ob0O8zLZFlDmwWNulV-G75jcFPiLHWPNmwo9qgwMDDG1k-T2cTfovaTiBTWYq3UkahqjEIWpZrCeKDKfb9PAwg-_F11kaL-gOggJ70PiYJQpLtXnFihpwYC3eREqPb8bKcPlvAX9-q_C3ZHVmVHeAw3vPVbtYlrZehJGNi2zlxgvQcn7X4G9I8e5Mzp15_bbugNPUSxJ7dYvJJFTVYAs4BbPppAJXVrF6y_Cc7ZkCriAWs4V3Z-7Bt4ANoVjg-VkzPCsazmoP3VdtTSsGBQlAw_qKG-ETBXbfITBFxCxunlzsZGK06lX_PD1tRiac");

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

public  static packpurchasepayloadd pack_purchase_payload(int packnumber)
{

    packpurchasepayloadd payload=new packpurchasepayloadd();
    payload.setStore(true);
    payload.setUpgrade(false);
    payload.setPackagePaymentType("CASH");
    payload.setPackageType("AFFILIATE");
    payload.setPackageTypeId(packnumber);
    payload.setPurchaseType("CASH");
    payload.setQuantity(1);
    payload.setReserve(true);
    return payload;
//    return "{\n" +
//            "    \"isStore\": true,\n" +
//            "    \"isUpgrade\": false,\n" +
//            "    \"packagePaymentType\": \"CASH\",\n" +
//            "    \"packageType\": \"AFFILIATE\",\n" +
//            "    \"packageTypeId\": "+packnumber+",\n" +
//            "    \"purchaseType\": \"CASH\",\n" +
//            "    \"quantity\": 1,\n" +
//            "    \"reserve\": true\n" +
//            "}";
}
}


