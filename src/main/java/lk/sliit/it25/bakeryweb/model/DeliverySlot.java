package lk.sliit.it25.bakeryweb.model;

public class DeliverySlot extends BaseDeliveryRecord {
    private String address;
    private String deliveryDate;
    private String deliveryTime;

    public DeliverySlot() {
    }

    public DeliverySlot(String id, String customerName, String address, String deliveryDate, String deliveryTime) {
        super(id, customerName);
        this.address = address;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
