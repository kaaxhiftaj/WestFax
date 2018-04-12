package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.business.admin.westfax.LoginWestfax;
import com.business.admin.westfax.MainActivity;
import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.TollNumAdapter;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.business.admin.westfax.retrofit.SignToll;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import morxander.editcard.EditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 21-01-2018.
 */

public class PaymentMethod extends Fragment {
    Button btnpaydon;
    String ufname, ulname, umail, uphn, upass, namee, value, areacode, typp, dayy, uname;
    ProgressDialog pDialog;
    EditText edtadd1, edtadd2, edtpaycity, edtpaystat, edtpayzip, edtcrholname, edtpayccv;
    EditCard edtcrname;
    String stradd1, stradd2, strycity, strstat, strholdr, strcard, strzip, strccv, areaname;
    String selmonth, selyear;
    LinearLayout carddetail;
    String faxnum, origfaxnum;
    ArrayList<String> yearlist;
    ArrayList<String> monthhlist;
    Spinner spinyer, spinmon;

    public PaymentMethod() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.paymentmethod, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment Method");
        yearlist = new ArrayList<>();
        monthhlist = new ArrayList<>();
        Bundle bundle = getArguments();
        //  fax number/////////////////
        namee = bundle.getString("Name");
        value = bundle.getString("Value");
        areacode = bundle.getString("Areacode");
        ufname = bundle.getString("fname");
        ulname = bundle.getString("lname");
        umail = bundle.getString("email");
        upass = bundle.getString("pass");
        uphn = bundle.getString("phone");
        typp = bundle.getString("type");
        dayy = bundle.getString("days");
        uname = bundle.getString("uname");
        areaname = bundle.getString("AreaName");
        faxnum = bundle.getString("Faxnum");
        origfaxnum = bundle.getString("OrigFaxnum");


        Log.e("params name--->", namee + "\n valll--->" + value
                + "\n areacod--->" + areacode +
                "\n ufname--->" + ufname +
                "\n ulname--->" + ulname +
                "\n umail--->" + umail +
                "\n upass--->" + upass +
                "\n uphn--->" + uphn +
                "\n typp--->" + typp +
                "\n dayy--->" + dayy +
                "\n uname--->" + uname +
                "\n faxnum" + faxnum + "==" + origfaxnum +
                "\n areaname" + areaname);
        btnpaydon = (Button) rootview.findViewById(R.id.btnpaydon);
        edtadd1 = (EditText) rootview.findViewById(R.id.edtadd1);
        edtadd2 = (EditText) rootview.findViewById(R.id.edtadd2);
        edtpaycity = (EditText) rootview.findViewById(R.id.edtpaycity);
        edtpaystat = (EditText) rootview.findViewById(R.id.edtpaystat);
        edtpayzip = (EditText) rootview.findViewById(R.id.edtpayzip);
        edtcrholname = (EditText) rootview.findViewById(R.id.edtcrholname);
        edtcrname = (EditCard) rootview.findViewById(R.id.edtcrname);
        edtpayccv = (EditText) rootview.findViewById(R.id.edtpayccv);
        carddetail = (LinearLayout) rootview.findViewById(R.id.carddetail);
        spinyer = (Spinner) rootview.findViewById(R.id.spinyer);
        spinmon = (Spinner) rootview.findViewById(R.id.spinmon);
        edtcrname.getCardNumber(); // Get the card number
        edtcrname.isValid(); // Is the card number valid
        edtcrname.getCardType();

        for (int i = 1; i < 13; i++) {

            if (i < 10) {
                String s = "0" + i;
                monthhlist.add(s);
            }
            if (i >= 10) {
                String s = "" + i;
                monthhlist.add(s);
            }
        }

        Log.e("month bhar===>", "" + monthhlist);
        ArrayAdapter monthapter = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.textspinn, monthhlist);
        spinmon.setAdapter(monthapter);


        int calyr = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = calyr; i < calyr + 6; i++) {
            String s = "" + i;
            yearlist.add(s);
        }

//        {
//            "Status": true,
//                "StatusCode": 200,
//                "Message": "OK",
//                "Data": [
//            {
//                "Name": "MobileApp_FAQUrl",
//                    "Value": "https://home.westfax.com/app/content/faq"
//            }
//    ]
//        }


        Log.e("year bhar===>", "" + yearlist);
        ArrayAdapter monthadapter = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.textspinn, yearlist);
        spinyer.setAdapter(monthadapter);

        selmonth = spinmon.getSelectedItem().toString();
        selyear = spinyer.getSelectedItem().toString();
        Log.e("Seledd month yaer===>", "" + selmonth + "--" + selyear);

        if (dayy.equals("5")) {
            carddetail.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create The Account");

        } else {
            carddetail.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment Method");

        }
        if (areaname.length() > 4) {
            String valuee = areaname;
            valuee = valuee.substring(4);
            edtpaystat.setText(valuee);
        }


        btnpaydon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zipp = edtpayzip.getText().toString();
                String add1 = edtadd1.getText().toString();
                String add2 = edtadd2.getText().toString();
                String adcity = edtpaycity.getText().toString();
                String adstat = edtpaystat.getText().toString();
                String adcardhol = edtcrholname.getText().toString();
                String adcardnum = edtcrname.getText().toString();
                String addccv = edtpayccv.getText().toString();

//                if (dayy.equals("5")) {
//                    if (add1.length() > 0 || add2.length() > 0 ||
//                            adcity.length() > 0 || adstat.length() > 0) {
//
//                        if (zipp.length() == 5 || zipp.length() == 9) {
//
//                            getCreateAccount();
//
//                            getCreateAccountForFiveDay();
//                        } else {
//                            Toast.makeText(getContext(), "Enter Valid Zipcode", Toast.LENGTH_SHORT).show();
//
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "Enter all credentials", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } else {


                if (add1.length() > 0 &&
                        adcity.length() > 0 && adstat.length() > 0) {

                    if (zipp.length() == 5 || zipp.length() == 9) {
                        getCreateAccount();
                    } else {
                        Toast.makeText(getContext(), "Enter Valid Zipcode", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getContext(), "Enter all credentials", Toast.LENGTH_SHORT).show();

                }
//                }
            }
        });
        return rootview;
    }

//    CrmContact=
//    {"Title":"Mr.", "FirstName":"Mayank", "LastName":"Patel", "CompanyName":"", "Email":
//        "patelmayankp@gmail.com", "Fax":"", "MobilePhone":"", "Phone":"", "Address1":"", "Address2":
//        "", "City":"", "Zip":"", "Country":""}
//    PmtCard
//    {
//        "Address1":"101 Main St.", "Address2":"", "CardNumber":"", "CCV":"", "City":
//        "", "CompanyName":"", "Country":"", "Email":"", "ExpireMonth":"", "ExpireYear":
//        "", "FirstName":"", "Id":"", "LastName":"", "Phone":"", "State":"", "Zip":""
//    }
//    MethodParams1 {"Name":"producttype", "Value":"FaxForward"}
//    MethodParams2 {"Name":"inboundphone", "Value":"8005551212" }
//    MethodParams3 {"Name":"username", "Value":"newUserName"}
//    MethodParams4{"Name":"password", "Value":"newPassword" }
//    MethodParams5{"Name":"freetrialdays", "Value":"60"}

    public void getCreateAccount() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String stcookie = Config.cokkiee;
        stradd1 = edtadd1.getText().toString();
        stradd2 = edtadd2.getText().toString();
        strycity = edtpaycity.getText().toString();
        strstat = edtpaystat.getText().toString();
        strzip = edtpayzip.getText().toString();
        strholdr = edtcrholname.getText().toString();
        strcard = edtcrname.getText().toString();
        strccv = edtpayccv.getText().toString();
        selmonth = spinmon.getSelectedItem().toString();
        selyear = spinyer.getSelectedItem().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject crmobj = new JSONObject();
        try {

            crmobj.put("Title", "Mr.");
            crmobj.put("FirstName", ufname);
            crmobj.put("LastName", ulname);
            crmobj.put("CompanyName", "");
            crmobj.put("Email", umail);
            crmobj.put("Fax", "");
            crmobj.put("MobilePhone", "");
            crmobj.put("Phone", uphn);
            crmobj.put("Address1", stradd1);
            crmobj.put("Address2", stradd2+"");
            crmobj.put("City", strycity);
            crmobj.put("Zip", strzip);
            crmobj.put("Country", strstat);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject pmtobj = new JSONObject();
        try {

//                pmtobj.put("Address1", "");
//                pmtobj.put("Address2", "");
//                pmtobj.put("CardNumber", "");
//                pmtobj.put("CCV", "");
//                pmtobj.put("City", "");
//                pmtobj.put("CompanyName", "");
//                pmtobj.put("Country", "");
//                pmtobj.put("Email", "");
//                pmtobj.put("ExpireMonth", "");
//                pmtobj.put("ExpireYear", "");
//                pmtobj.put("FirstName", "");
//                pmtobj.put("Id", "");
//                pmtobj.put("LastName", "");
//                pmtobj.put("Phone", "");
//                pmtobj.put("State", "");
//                pmtobj.put("Zip", "");
//                Log.e("my 5 daysss", "true----");

//////////////////////////////////////////////Respo
//            false
//            crm{"Title":"Mr.","FirstName":"demmo","LastName":"last","CompanyName":"","Email":"hiiiii@helloo.com","Fax":"","MobilePhone":"","Phone":"1237566988","Address1":"ad1","Address2":"ad2","City":"colomo","Zip":"21213","Country":"colombia"}
//            pmt{"Address1":"101 Main St.","Address2":"","CardNumber":"4007000000027","CCV":"777","City":"Denver","CompanyName":"","Country":"","Email":"hiiiii@helloo.com","ExpireMonth":"10","ExpireYear":"2020","FirstName":"demmo","Id":"","LastName":"last","Phone":"1237566988","State":"CO","Zip":"80015"}
//            mthdone{"Name":"producttype","Value":"FaxForward"}
//            mthdtwo{"Name":"inboundphone","Value":"(800)878-4516"}
//            mthdthree{"Name":"username","Value":"demolast"}
//            mthdfor{"Name":"password","Value":"11111111"}
//            mthdfiv{"Name":"freeTrialDays","Value":"5"}
//            mthdsix{"Name":"forceCardSuccess","Value":"true"}
//            mthdseven{"Name":"signupSource","Value":"Android"}
//            false----470--Signup Failed (Invalid Parameters)
//


            if (dayy.equals("5")) {
                pmtobj.put("Address1", "");
                pmtobj.put("Address2", "");
                pmtobj.put("CardNumber", "4007000000027");
                pmtobj.put("CCV", "777");
                pmtobj.put("City", "");
                pmtobj.put("CompanyName", "");
                pmtobj.put("Country", "");
                pmtobj.put("Email", "");
                pmtobj.put("ExpireMonth", "0");
                pmtobj.put("ExpireYear", "0");
                pmtobj.put("FirstName", "");
                pmtobj.put("Id", "");
                pmtobj.put("LastName", "");
                pmtobj.put("Phone", "");
                pmtobj.put("State", "");
                pmtobj.put("Zip", "");

            } else {
                pmtobj.put("Address1", stradd1);
                pmtobj.put("Address2", stradd2+"");
                pmtobj.put("CardNumber", strcard);
                pmtobj.put("CCV", strccv);
                pmtobj.put("City", strycity);
                pmtobj.put("CompanyName", "");
                pmtobj.put("Country", "");
                pmtobj.put("Email", umail);
                pmtobj.put("ExpireMonth", selmonth);
                pmtobj.put("ExpireYear", selyear);
                pmtobj.put("FirstName", ufname);
                pmtobj.put("Id", "");
                pmtobj.put("LastName", ulname);
                pmtobj.put("Phone", uphn);
                pmtobj.put("State", strstat);
                pmtobj.put("Zip", strzip);
            }

//            pmtobj.put("Address1", "101 Main St.");
//            pmtobj.put("Address2", "");
//            pmtobj.put("CardNumber", "4007000000027");
//            pmtobj.put("CCV", "777");
//            pmtobj.put("City", "Denver");
//            pmtobj.put("CompanyName", "");
//            pmtobj.put("Country", "");
//            pmtobj.put("Email", umail);
//            pmtobj.put("ExpireMonth", "10");
//            pmtobj.put("ExpireYear", "2020");
//            pmtobj.put("FirstName", ufname);
//            pmtobj.put("Id", "");
//            pmtobj.put("LastName", ulname);
//            pmtobj.put("Phone", uphn);
//            pmtobj.put("State", "CO");
//            pmtobj.put("Zip", "80015");

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        crm{"Title":"Mr.","FirstName":"demmmo","LastName":"demmmo","CompanyName":"","Email":"demotest@test.com","Fax":"","MobilePhone":"","Phone":"1235698896","Address1":"djgjv","Address2":"chfjv","City":"columbia","Zip":"24568","Country":"District of Columbia"}
//        pmt{"Address1":"djgjv","Address2":"chfjv","CardNumber":"4100400421820393","CCV":"777","City":"columbia","CompanyName":"","Country":"","Email":"demotest@test.com","ExpireMonth":"10","ExpireYear":"2019","FirstName":"demmmo","Id":"","LastName":"demmmo","Phone":"1235698896","State":"District of Columbia","Zip":"24568"}
//        mthdone{"Name":"producttype","Value":"FaxForward"}
//        mthdtwo{"Name":"inboundphone","Value":"2029000942"}
//        mthdthree{"Name":"username","Value":"ss11ss11"}
//        mthdfor{"Name":"password","Value":"12121212"}
//        mthdfiv{"Name":"freeTrialDays","Value":"60"}
//        mthdsix{"Name":"forceCardSuccess","Value":"true"}
//        mthdseven{"Name":"signupSource","Value":"Android"}


        JSONObject mthdoneobj = new JSONObject();
        try {
            mthdoneobj.put("Name", "producttype");
            mthdoneobj.put("Value", "FaxForward");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject mthdtwoobj = new JSONObject();
        try {
            mthdtwoobj.put("Name", "inboundphone");
            mthdtwoobj.put("Value", origfaxnum);/////////////////////////////

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject mthdthreeobj = new JSONObject();
        try {
            mthdthreeobj.put("Name", "username");
            mthdthreeobj.put("Value", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject mthdforobj = new JSONObject();
        try {
            mthdforobj.put("Name", "password");
            mthdforobj.put("Value", upass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject mthfivobj = new JSONObject();
        try {
            mthfivobj.put("Name", "freeTrialDays");
            mthfivobj.put("Value", dayy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject mthsixobj = new JSONObject();
        try {
            mthsixobj.put("Name", "forceCardSuccess");
            mthsixobj.put("Value", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject mthsevnobj = new JSONObject();
        try {
            mthsevnobj.put("Name", "signupSource");
            mthsevnobj.put("Value", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("all data----->", stcookie + "\n crm" + crmobj +
                "\n pmt" + pmtobj +
                "\n mthdone" + mthdoneobj +
                "\n mthdtwo" + mthdtwoobj +
                "\n mthdthree" + mthdthreeobj +
                "\n mthdfor" + mthdforobj +
                "\n mthdfiv" + mthfivobj +
                "\n mthdsix" + mthsixobj +
                "\n mthdseven" + mthsevnobj);

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.createAccount(stcookie, crmobj, pmtobj, mthdoneobj, mthdtwoobj, mthdthreeobj, mthdforobj, mthfivobj, mthsixobj, mthsevnobj);
//     ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
//                Log.e("ressbodyyyy----->", response.body().getDattta() + "----" + response.body().getStatusCode() + "--" +
//                        response.body().getMessag());
                pDialog.dismiss();

                if (response.body().getStatusCode().equals("200")) {
                    Intent iin = new Intent(getContext(), LoginWestfax.class);
                    startActivity(iin);
                    getActivity().finish();
                    Toast.makeText(getContext(), "Account Created", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Log.e("faillier----->", call + "");
                pDialog.dismiss();

                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }

}
