package lk.sliit.it25.bakeryweb.model;

public class DeliverySlot {
    private String id;
    private String customerName;
    private String address;
    private String deliveryDate;
    private String deliveryTime;

    public DeliverySlot() {
    }

    public DeliverySlot(String id, String customerName, String address, String deliveryDate, String deliveryTime) {
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
