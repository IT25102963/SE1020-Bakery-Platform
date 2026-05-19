package lk.sliit.it25.bakeryweb.orders;

import java.util.Date;

public class Order {
    private String orderId;
    private String customerName;
    private Date orderDate;
    private String status;

    public Order(String orderId, String customerName, Date orderDate, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
