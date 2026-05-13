package com.cakecraft.orderbookings.model;

import java.math.BigDecimal;

public class CartItem {
    private String cakeName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    public CartItem() {
    }

    public CartItem(String cakeName, Integer quantity, BigDecimal unitPrice) {
        this.cakeName = cakeName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
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
        updateSubTotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        updateSubTotal();
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    private void updateSubTotal() {
        if (unitPrice != null && quantity != null) {
            this.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
