package Basics;

public class Transferpayload {

    public static String statuspayload(String email)
    {
        return "{\n" +
                "\"countryCode\": \"+91\",\n" +
                "\"countryName\": \"India\",\n" +
                "\"deviceToken\": \"\",\n" +
                "\"email\": \""+email+"\",\n" +
                "\"lang\": \"en\",\n" +
                "\"referenceId\":\"\", \n" +
                "\"subscribeMarketing\": true\n" +
                "}";
    }

    public static String Loginpayload(String email,String pass)
    {
        return "{\n" +
                "\"deviceToken\": \"\",\n" +
                "\"deviceType\":\"WEB\",\n" +
                "\"email\": \""+email+"\",\n" +
                "\"password\": \""+pass+"\"\n" +
                "\n" +
                "}";
    }

    public static String searchpayload(String searchuser)
    {
     return "{\n" +
             "\"limit\": 10,\n" +
             "\"pageNo\": 0,\n" +
             "\"referralCode\": \""+searchuser+"\"\n" +
             "}";
    }

    public static String Transferpayload(String memberid ,int amount)

    {
        return "{\n" +
                "    \"amount\": "+amount+",\n" +
                "    \"isReserve\": false,\n" +
                "    \"medium\": \"MOBILE\",\n" +
                "    \"receiverId\": "+memberid+",\n" +
                "    \"requestId\": \"\"\n" +
                "}";

    }
    public static String receiveduser(String receiveuser,String pass)
    {
        return "{\n" +
                "\"deviceToken\": \"\",\n" +
                "\"deviceType\":\"WEB\",\n" +
                "\"email\": \""+receiveuser+"\",\n" +
                "\"password\": \""+pass+"\"\n" +
                "\n" +
                "}";
    }
}
