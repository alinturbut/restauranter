package com.alinturbut.restauranter.model;

import java.io.Serializable;

/**
 * @author alinturbut.
 */
public class Offer implements Serializable {
    private String id;
    private String orderId;
    private int discount;
    private String tableId;

    public Offer(String id, String orderId, int discount, String tableId) {
        this.id = id;
        this.orderId = orderId;
        this.discount = discount;
        this.setTableId(tableId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
