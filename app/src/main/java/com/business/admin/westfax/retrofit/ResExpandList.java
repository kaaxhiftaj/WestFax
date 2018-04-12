
package com.business.admin.westfax.retrofit;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResExpandList {

    @SerializedName("ItemTypeName")
    private String ItemTypeName;

    @SerializedName("String1Name")
    private String String1Name;
    @SerializedName("String1Value")
    private String String1Value;

    @SerializedName("String2Name")
    private String String2Name;
    @SerializedName("String2Value")
    private String String2Value;

    @SerializedName("String3Name")
    private String String3Name;
    @SerializedName("String3Value")
    private String String3Value;

    @SerializedName("String4Name")
    private String String4Name;
    @SerializedName("String4Value")
    private String String4Value;


    @SerializedName("Id")
    private String mid;
    @SerializedName("Direction")
    private String mdirection;
    @SerializedName("Date")
    private String mdate;
    @SerializedName("Tag")
    private String mtag;
    @SerializedName("Detail")
    private String detail;

    @SerializedName("FaxQuality")
    private String mFaxQuality;
    @SerializedName("PageCount")
    private String mPageCount;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("FaxCallInfoList")
    private List<com.business.admin.westfax.retrofit.FaxCallInfoList> mFaxCallInfoList;


    @SerializedName("Name")
    private String mName;
    @SerializedName("Value")
    private String mValue;



    public ResExpandList(String name, String posting) {
        this.ItemTypeName = name;
        this.String2Name = posting;
    }

    public ResExpandList(String direct) {
        this.mdirection = direct;
    }


    public ResExpandList() {
    }


    public String getItemTypeName() {
        return ItemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        ItemTypeName = itemTypeName;
    }

    public String getString1Name() {
        return String1Name;
    }

    public void setString1Name(String string1Name) {
        String1Name = string1Name;
    }

    public String getString2Value() {
        return String2Value;
    }

    public void setString2Value(String string2Value) {
        String2Value = string2Value;
    }

    public String getString2Name() {
        return String2Name;
    }

    public void setString2Name(String string2Name) {
        String2Name = string2Name;
    }

    public String getString1Value() {
        return String1Value;
    }

    public void setString1Value(String string1Value) {
        String1Value = string1Value;
    }

    public String getString3Name() {
        return String3Name;
    }

    public void setString3Name(String string3Name) {
        String3Name = string3Name;
    }

    public String getString3Value() {
        return String3Value;
    }

    public void setString3Value(String string3Value) {
        String3Value = string3Value;
    }

    public String getString4Name() {
        return String4Name;
    }

    public void setString4Name(String string4Name) {
        String4Name = string4Name;
    }

    public String getString4Value() {
        return String4Value;
    }

    public void setString4Value(String string4Value) {
        String4Value = string4Value;
    }


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMdirection() {
        return mdirection;
    }

    public void setMdirection(String mdirection) {
        this.mdirection = mdirection;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMtag() {
        return mtag;
    }

    public void setMtag(String mtag) {
        this.mtag = mtag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<com.business.admin.westfax.retrofit.FaxCallInfoList> getFaxCallInfoList() {
        return mFaxCallInfoList;
    }

    public void setFaxCallInfoList(List<com.business.admin.westfax.retrofit.FaxCallInfoList> FaxCallInfoList) {
        mFaxCallInfoList = FaxCallInfoList;
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

    public String getFaxQuality() {
        return mFaxQuality;
    }

    public void setFaxQuality(String FaxQuality) {
        mFaxQuality = FaxQuality;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

//    private boolean isSelected;
//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }
}
