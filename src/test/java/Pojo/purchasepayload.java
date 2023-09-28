package Pojo;

public class purchasepayload {


    public boolean isStore() {
        return isStore;
    }

    public void setisStore(boolean store) {
        isStore = store;
    }

    public boolean isUpgrade() {
        return isUpgrade;
    }

    public void setisUpgrade(boolean upgrade) {
        isUpgrade = upgrade;
    }

    public String getpackagePaymentType() {
        return packagePaymentType;
    }

    public void setpackagePaymentType(String packagePaymentType) {
        this.packagePaymentType = packagePaymentType;
    }

    public String getpackageType() {
        return packageType;
    }

    public void setpackageType(String packageType) {
        this.packageType = packageType;
    }

    public int getpackageTypeId() {
        return packageTypeId;
    }

    public void setpackageTypeId(int packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getpurchaseType() {
        return purchaseType;
    }

    public void setpurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean Reserve() {
        return reserve;
    }

    public void setreserve(boolean reserve) {
        this.reserve = reserve;
    }

    private boolean isStore;
    private boolean isUpgrade;
    private String packagePaymentType;
    private String packageType;
    private int packageTypeId;
    private String purchaseType;
    private int quantity;
    private boolean reserve;

    }





