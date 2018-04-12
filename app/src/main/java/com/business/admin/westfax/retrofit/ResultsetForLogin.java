package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SONY on 31-01-2018.
 */

public class ResultsetForLogin {

    @SerializedName("Status")
    private String status;
    @SerializedName("Data")
    private String dattta;
    @SerializedName("StatusCode")
    private String statusCode;
    @SerializedName("Message")
    private String messag;

    ///////////////////////Get Sender Lists

    public ResultsetForLogin() {

    }

    public ResultsetForLogin(String success, String result) {
        this.status = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDattta() {
        return dattta;
    }

    public void setDattta(String dattta) {
        this.dattta = dattta;
    }


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessag() {
        return messag;
    }

    public void setMessag(String messag) {
        this.messag = messag;
    }
}
