package com.kanmanus.kmutt.sit.ijoint.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
public class SignInResponse {
    private boolean status;
    private String pid;

    @SerializedName("pFirstName")
    private String firstName;

    @SerializedName("pLastName")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
