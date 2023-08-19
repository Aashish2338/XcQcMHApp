package com.xtracover.xcqcmh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {
    @SerializedName("RespCode")
    @Expose
    private Integer respCode;
    @SerializedName("RespMsg")
    @Expose
    private String respMsg;
    @SerializedName("JobID")
    @Expose
    private Object jobID;
    @SerializedName("Pickup")
    @Expose
    private Object pickup;
    @SerializedName("Doorstep")
    @Expose
    private Object doorstep;
    @SerializedName("Pincode")
    @Expose
    private Object pincode;
    @SerializedName("DATA")
    @Expose
    private List<LoginData> loginData = null;
    @SerializedName("DATA1")
    @Expose
    private Object data1;

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public Object getJobID() {
        return jobID;
    }

    public void setJobID(Object jobID) {
        this.jobID = jobID;
    }

    public Object getPickup() {
        return pickup;
    }

    public void setPickup(Object pickup) {
        this.pickup = pickup;
    }

    public Object getDoorstep() {
        return doorstep;
    }

    public void setDoorstep(Object doorstep) {
        this.doorstep = doorstep;
    }

    public Object getPincode() {
        return pincode;
    }

    public void setPincode(Object pincode) {
        this.pincode = pincode;
    }

    public List<LoginData> getLoginData() {
        return loginData;
    }

    public void setLoginData(List<LoginData> loginData) {
        this.loginData = loginData;
    }

    public Object getData1() {
        return data1;
    }

    public void setData1(Object data1) {
        this.data1 = data1;
    }
}