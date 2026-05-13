package lk.sliit.it25.bakeryweb.model;

import java.math.BigDecimal;

public class CartItem {

    private String cakeName;
    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(String cakeName, Integer quantity, BigDecimal unitPrice) {
        this.cakeName = cakeName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}
