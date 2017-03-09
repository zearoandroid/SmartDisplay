package com.zearoconsulting.smartdisplay.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 24-05-2016.
 */
public class Warehouse implements Serializable {

    private long orgId;
    private long warehouseId;
    private String warehouseName;

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
