package com.business.admin.westfax.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SONY on 26-01-2018.
 */

public class ResultRet {

    @SerializedName("Status")
    private String success;

    private String resultt;

  ///////////////////////Get Sender Lists
    @SerializedName("Data")
    @Expose
    private ArrayList<ResExpandList> resultpera = new ArrayList<ResExpandList>();
    @SerializedName("Name")
    private String namee;
    @SerializedName("Value")
    private String value;
    @SerializedName("Message")
    private String messagee;

    @SerializedName("StatusCode")
    private String StatusCode;

    public ResultRet() {

    }

    public ResultRet(String success, String result) {
        this.success = success;
 }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getResultt() {
        return resultt;
    }

    public void setResultt(String resultt) {
        this.resultt = resultt;
    }


    public ArrayList<ResExpandList> getResultpera() {
        return resultpera;
    }

    public void setResultpera(ArrayList<ResExpandList> resultpera) {
        this.resultpera = resultpera;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessagee() {
        return messagee;
    }

    public void setMessagee(String messagee) {
        this.messagee = messagee;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
