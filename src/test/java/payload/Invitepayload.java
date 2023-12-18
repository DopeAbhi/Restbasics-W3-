package payload;

import pojo.createTemplatePayload;

import java.util.List;

public class Invitepayload {

    public static String template_payload(List<Object> temp_data )
    {
           createTemplatePayload payload=new createTemplatePayload();
        payload.setBgImageUniqueId();
        payload.setImageId();
        payload.setMemberId();
        payload.setMessage();
        payload.setName();
        payload.setShowUsername();
        payload.setShowWelcomeImage();
        payload.setTitle();
    }
}
