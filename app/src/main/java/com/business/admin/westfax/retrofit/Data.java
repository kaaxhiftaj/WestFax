
package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("Email")
    private String mEmail;
    @SerializedName("FirstName")
    private String mFirstName;
    @SerializedName("LastName")
    private String mLastName;
    @SerializedName("Title")
    private String mTitle;

    @SerializedName("Phone")
    private String mPhonee;
    @SerializedName("MobilePhone")
    private String mMobilePhone;


    @SerializedName("State")
    private String mState;
    @SerializedName("Zip")
    private String mZip;
    @SerializedName("CompanyName")
    private String mCompanyName;
    @SerializedName("City")
    private String mCity;
    @SerializedName("Address1")
    private String mAddress1;

    private boolean isChecked;


    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String Email) {
        mEmail = Email;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String FirstName) {
        mFirstName = FirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String LastName) {
        mLastName = LastName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public String getmPhonee() {
        return mPhonee;
    }

    public void setmPhonee(String mPhonee) {
        this.mPhonee = mPhonee;
    }

    public String getmMobilePhone() {
        return mMobilePhone;
    }

    public void setmMobilePhone(String mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmZip() {
        return mZip;
    }

    public void setmZip(String mZip) {
        this.mZip = mZip;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public void setmAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }
}
