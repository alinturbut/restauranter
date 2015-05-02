package com.alinturbut.restauranter.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author alinturbut.
 */
public class Receipt implements Serializable {
    private String id;
    private String orderId;
    private String waiterId;
    private String offerId;
    private Date date;

    public Receipt(String id, String orderId, String waiterId, String offerId, Date date) {
        this.id = id;
        this.orderId = orderId;
        this.waiterId = waiterId;
        this.offerId = offerId;
        this.date = date;
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

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
