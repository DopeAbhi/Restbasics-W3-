package pojo;

public class createTemplatePayload {

    private String bgImageUniqueId;
    private int imageId;
    private String memberId;
    private String message;
    private String name;
    private boolean showUsername;
    private boolean showWelcomeImage;
    private String title;


    public String getBgImageUniqueId() {
        return bgImageUniqueId;
    }

    public void setBgImageUniqueId(String bgImageUniqueId) {
        this.bgImageUniqueId = bgImageUniqueId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowUsername() {
        return showUsername;
    }

    public void setShowUsername(boolean showUsername) {
        this.showUsername = showUsername;
    }

    public boolean isShowWelcomeImage() {
        return showWelcomeImage;
    }

    public void setShowWelcomeImage(boolean showWelcomeImage) {
        this.showWelcomeImage = showWelcomeImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
