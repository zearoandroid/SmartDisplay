package com.zearoconsulting.smartdisplay.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 30-05-2016.
 */
public class Customer implements Serializable {

    private long posId;
    private long bpId;
    private String customerName;
    private long priceListId;
    private long customerValue;
    private String email;
    private long mobile;
    private int isCashCustomer;

    public long getPosId() {
        return posId;
    }

    public void setPosId(long posId) {
        this.posId = posId;
    }

    public long getBpId() {
        return bpId;
    }

    public void setBpId(long bpId) {
        this.bpId = bpId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(long priceListId) {
        this.priceListId = priceListId;
    }

    public long getCustomerValue() {
        return customerValue;
    }

    public void setCustomerValue(long customerValue) {
        this.customerValue = customerValue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public int getIsCashCustomer() {
        return isCashCustomer;
    }

    public void setIsCashCustomer(int isCashCustomer) {
        this.isCashCustomer = isCashCustomer;
    }
}
