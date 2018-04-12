package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SONY on 03-02-2018.
 */

public class SignToll {

    @SerializedName("Data")
    private List<String> mData;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Status")
    private Boolean mStatus;
    @SerializedName("StatusCode")
    private Long mStatusCode;


    @SerializedName("Success")
    private Boolean mSuccess;

    public List<String> getData() {
        return mData;
    }

    public void setData(List<String> Data) {
        mData = Data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String Message) {
        mMessage = Message;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean Status) {
        mStatus = Status;
    }

    public Long getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(Long StatusCode) {
        mStatusCode = StatusCode;
    }


    public Boolean getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(Boolean mSuccess) {
        this.mSuccess = mSuccess;
    }
}
