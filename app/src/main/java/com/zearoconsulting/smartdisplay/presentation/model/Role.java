package com.zearoconsulting.smartdisplay.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 24-05-2016.
 */
public class Role implements Serializable {

    private long orgId;
    private long roleId;
    private String roleName;

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

