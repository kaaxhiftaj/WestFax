package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.LoginWestfax;
import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.retrofit.JsonObjCust;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResultsetForLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 21-01-2018.
 */

public class MyProfileFragment extends Fragment {
    ProgressDialog pDialog;
    TextView edtlnam, edtfnm, edtemail, edtphn;
    EditText edtconpass, edtpass;
    Button btnconsave, btnchngpas;
    String fname, lname, emailu, phnn, add1, city, compname, state, zip;
    String unamee, upaasss, uprodid;

    UserSessionManager sessionManager;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.myprofile, container, false);
        sessionManager = new UserSessionManager(getContext());

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");
        edtlnam = (TextView) rootview.findViewById(R.id.edtlnam);
        edtfnm = (TextView) rootview.findViewById(R.id.edtfnm);
        edtemail = (TextView) rootview.findViewById(R.id.edtemail);
        edtphn = (TextView) rootview.findViewById(R.id.edtphn);
        edtconpass = (EditText) rootview.findViewById(R.id.edtconpass);
        edtpass = (EditText) rootview.findViewById(R.id.edtpass);
        btnconsave = (Button) rootview.findViewById(R.id.btnconsave);
        btnchngpas = (Button) rootview.findViewById(R.id.btnchngpas);

        getContactDetails();
        btnconsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfileDetils();
            }
        });


        btnchngpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mainpass = edtpass.getText().toString();
                String confpass = edtconpass.getText().toString();

                Log.e("pass-->", mainpass + "--" + confpass);
                if (mainpass.length() == confpass.length()) {
                    if (mainpass.equals(confpass)) {
                        if (mainpass.length() < 6) {
                            Toast.makeText(getContext(), "Enter strong password", Toast.LENGTH_SHORT).show();

                        } else {
                            changepassworduser();
                        }
                    } else {
                        Toast.makeText(getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Password lenth doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootview;
    }


    public void changepassworduser() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("Name", "newpassword");
            jsonobject_one.put("Value", edtpass.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);
        Call<ResultsetForLogin> retCall = easyApi.changepass(unamee, upaasss, stcookie, jsonobject_one);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall r----->", retCall + "");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("ressbodyyyy----->", response.body().getDattta() + "");
                pDialog.dismiss();

                if (response.body().getStatusCode().equals("200")) {
                    sessionManager.logoutUser();
                    Intent iin = new Intent(getContext(), LoginWestfax.class);
                    startActivity(iin);
                    getActivity().finish();

                         Toast.makeText(getContext(), "Password successfully changed", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }


    public void getContactDetails() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);
        Call<JsonObjCust> retCall = easyApi.getContactDetails(unamee, upaasss, stcookie);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall r----->", retCall + "");

        retCall.enqueue(new Callback<JsonObjCust>() {
            @Override
            public void onResponse(Call<JsonObjCust> call, Response<JsonObjCust> response) {
                Log.e("ressbodyyyy----->", response.body().getMessage() + "");
                pDialog.dismiss();


                if (response.body().getMessage().equals("OK")) {
                    fname = response.body().getData().getFirstName();
                    lname = response.body().getData().getLastName();
                    emailu = response.body().getData().getEmail();
                    phnn = response.body().getData().getmPhonee();

                    add1 = response.body().getData().getmAddress1();
                    city = response.body().getData().getmCity();
                    compname = response.body().getData().getmCompanyName();
                    state = response.body().getData().getmState();
                    zip = response.body().getData().getmZip();

                    edtemail.setText(emailu);
                    edtfnm.setText(fname);
                    edtlnam.setText(lname);
                    edtphn.setText(phnn);

                    Log.e("fname----->", fname + "");
                    Log.e("lname----->", lname + "");
                    Log.e("email----->", emailu + "");

                    //     Toast.makeText(getContext(), "magic", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObjCust> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }


    public void editProfileDetils() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("Title", "Mr.");
            jsonobject_one.put("FirstName", edtfnm.getText().toString());
            jsonobject_one.put("LastName", edtlnam.getText().toString());
            jsonobject_one.put("CompanyName", compname);
            jsonobject_one.put("Email", edtemail.getText().toString());
            jsonobject_one.put("Fax", "");
            jsonobject_one.put("MobilePhone", "");
            jsonobject_one.put("Phone", edtphn.getText().toString());
            jsonobject_one.put("Address1", add1);
            jsonobject_one.put("Address2", "");
            jsonobject_one.put("City", city);
            jsonobject_one.put("Zip", zip);
            jsonobject_one.put("Country", "");
            Log.e("jsonbojjj@@@@", jsonobject_one + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);
        Call<ResultsetForLogin> retCall = easyApi.SetContactDetails(unamee, upaasss, stcookie, jsonobject_one);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall r----->", retCall + "");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("ressbodyyyy----->", response.body().getDattta() + "");
                pDialog.dismiss();

                if (response.body().getStatusCode().equals("200")) {

                    getContactDetails();

//                    Intent iin = new Intent(getContext(), LoginWestfax.class);
//                    startActivity(iin);
//                    getActivity().finish();

                         Toast.makeText(getContext(), "Details successfully changed", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }


}
