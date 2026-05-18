package lk.sliit.it25.bakeryweb.model;

public class BaseDeliveryRecord {
    private String id;
    private String customerName;

    public BaseDeliveryRecord() {
    }

    public BaseDeliveryRecord(String id, String customerName) {
        this.id = id;
        this.customerName = customerName;
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
}
