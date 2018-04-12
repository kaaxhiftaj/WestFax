
package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.SerializedName;

public class JsonObjCust {
    @SerializedName("Data")
    private com.business.admin.westfax.retrofit.Data mData;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Status")
    private Boolean mStatus;
    @SerializedName("StatusCode")
    private Long mStatusCode;

    public com.business.admin.westfax.retrofit.Data getData() {
        return mData;
    }

    public void setData(com.business.admin.westfax.retrofit.Data Data) {
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

}
