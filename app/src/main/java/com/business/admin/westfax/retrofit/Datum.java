
package com.business.admin.westfax.retrofit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("Date")
    private String mDate;
    @SerializedName("Direction")
    private String mDirection;
    @SerializedName("FaxCallInfoList")
    private List<com.business.admin.westfax.retrofit.FaxCallInfoList> mFaxCallInfoList;
    @SerializedName("FaxQuality")
    private String mFaxQuality;
    @SerializedName("Id")
    private String mId;
    @SerializedName("PageCount")
    private String mPageCount;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("Tag")
    private String mTag;

/////////////////////////////
    @SerializedName("Format")
    private String mFormat;

    @SerializedName("FaxFiles")
    private List<com.business.admin.westfax.retrofit.FaxCallInfoList> mFaxfiles;



    public Datum(String name, String posting) {
        this.mId = name;
        this.mDirection = posting;
    }
    public Datum() {
      }

///////////////////////////////////
    public List<FaxCallInfoList> getmFaxfiles() {
        return mFaxfiles;
    }

    public void setmFaxfiles(List<FaxCallInfoList> mFaxfiles) {
        this.mFaxfiles = mFaxfiles;
    }

    public String getmFormat() {
        return mFormat;
    }

    public void setmFormat(String mFormat) {
        this.mFormat = mFormat;
    }



/////////////////////////////////
    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        mDate = Date;
    }

    public String getDirection() {
        return mDirection;
    }

    public void setDirection(String Direction) {
        mDirection = Direction;
    }

    public List<com.business.admin.westfax.retrofit.FaxCallInfoList> getFaxCallInfoList() {
        return mFaxCallInfoList;
    }

    public void setFaxCallInfoList(List<com.business.admin.westfax.retrofit.FaxCallInfoList> FaxCallInfoList) {
        mFaxCallInfoList = FaxCallInfoList;
    }

    public String getFaxQuality() {
        return mFaxQuality;
    }

    public void setFaxQuality(String FaxQuality) {
        mFaxQuality = FaxQuality;
    }

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getPageCount() {
        return mPageCount;
    }

    public void setPageCount(String PageCount) {
        mPageCount = PageCount;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String Status) {
        mStatus = Status;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String Tag) {
        mTag = Tag;
    }

}
