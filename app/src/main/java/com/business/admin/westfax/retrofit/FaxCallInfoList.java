
package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FaxCallInfoList {

    @SerializedName("CallId")
    private String mCallId;
    @SerializedName("CompletedUTC")
    private String mCompletedUTC;
    @SerializedName("OrigCSID")
    private String mOrigCSID;
    @SerializedName("OrigNumber")
    private String mOrigNumber;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("TermNumber")
    private String mTermNumber;



/////////////////////

    @SerializedName("FileContents")
    private String mFileContents;
    @SerializedName("ContentType")
    private String mContentType;
    @SerializedName("ContentLength")
    private String mContentLength;

    public String getmFileContents() {
        return mFileContents;
    }

    public void setmFileContents(String mFileContents) {
        this.mFileContents = mFileContents;
    }

    public String getmContentType() {
        return mContentType;
    }

    public void setmContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    public String getmContentLength() {
        return mContentLength;
    }

    public void setmContentLength(String mContentLength) {
        this.mContentLength = mContentLength;
    }

//////////////////////////////////
    public FaxCallInfoList(String  callIdd) {
        this.mCallId = callIdd;
    }
    public FaxCallInfoList() {
    }
    public String getCallId() {
        return mCallId;
    }

    public void setCallId(String CallId) {
        mCallId = CallId;
    }

    public String getCompletedUTC() {
        return mCompletedUTC;
    }

    public void setCompletedUTC(String CompletedUTC) {
        mCompletedUTC = CompletedUTC;
    }

    public String getOrigCSID() {
        return mOrigCSID;
    }

    public void setOrigCSID(String OrigCSID) {
        mOrigCSID = OrigCSID;
    }

    public String getOrigNumber() {
        return mOrigNumber;
    }

    public void setOrigNumber(String OrigNumber) {
        mOrigNumber = OrigNumber;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String Result) {
        mResult = Result;
    }

    public String getTermNumber() {
        return mTermNumber;
    }

    public void setTermNumber(String TermNumber) {
        mTermNumber = TermNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Result = "+mResult+", OrigCSID = "+mOrigCSID+", TermNumber = "+mTermNumber+", CompletedUTC = "+mCompletedUTC+", OrigNumber = "+mOrigNumber+", CallId = "+mCallId+"]";
    }
}
