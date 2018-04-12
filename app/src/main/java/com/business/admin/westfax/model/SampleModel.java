package com.business.admin.westfax.model;

public class SampleModel {
    private String name;
    private String posting;
    public static String Val;
    public static String disCode;
    public static String disName;

    public SampleModel(String name, String posting) {
        this.name = name;
        this.posting = posting;
    }
    public SampleModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public static String getVal() {
        return Val;
    }

    public static void setVal(String val) {
        Val = val;
    }

    public static String getDisCode() {
        return disCode;
    }

    public static void setDisCode(String disCode) {
        SampleModel.disCode = disCode;
    }

    public static String getDisName() {
        return disName;
    }

    public static void setDisName(String disName) {
        SampleModel.disName = disName;
    }
}
