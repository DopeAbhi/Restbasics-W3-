package payload;

import pojo.createTemplatePayload;

import java.util.List;

public class Invitepayload {

    public static List<Object> template_payload(List<Object> temp_data )
    {
           createTemplatePayload payload=new createTemplatePayload();
        payload.setBgImageUniqueId((String) temp_data.get(0));
        payload.setImageId((Integer) temp_data.get(1));
        payload.setMemberId((String) temp_data.get(2));
        payload.setMessage((String) temp_data.get(3));
        payload.setName((String) temp_data.get(4));
        payload.setShowUsername((Boolean) temp_data.get(5));
        payload.setShowWelcomeImage((Boolean) temp_data.get(6));
        payload.setTitle((String) temp_data.get(7));
        return temp_data;
    }
}
