
package com.business.admin.westfax.retrofit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Dataforall {

    @SerializedName("Data")
    private List<Datum> mData;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Status")
    private Boolean mStatus;
    @SerializedName("StatusCode")
    private Long mStatusCode;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> Data) {
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
