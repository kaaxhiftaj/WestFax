package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.ReceiverFaxAdapter;
import com.business.admin.westfax.adapter.SenderResAdapter;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultRet;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AccountSettingFragment extends Fragment {
    RecyclerView recfax_sendr, recfax_reciver;
    ArrayList<ResExpandList> reslist;
    ArrayList<ResExpandList> reslistreciver;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager recivlaymanager;
    SenderResAdapter resAdapter;
    ReceiverFaxAdapter receiAdapter;
    ProgressDialog pDialog;
    ImageView btnaddsendr, btnaddreciv;
    private Random mRandom = new Random();
    EditText edtsendr, edtrecv;
    String unamee, upaasss, uprodid;
    String tofaxmail, fromfaxmail;
    UserSessionManager sessionManager;
    Button btnsendadd, btnreciveadd;
    ImageView queallsend, quenotem;
    CheckBox chkalsend, chkalreciv;
    String emailPattern;

    public AccountSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_account_setting, container, false);
        reslist = new ArrayList<>();
        reslistreciver = new ArrayList<>();

        sessionManager = new UserSessionManager(getContext());

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Fax Settings");
        recfax_sendr = (RecyclerView) rootview.findViewById(R.id.recfax_sendr);
        btnaddsendr = (ImageView) rootview.findViewById(R.id.btnaddsendr);
        edtsendr = (EditText) rootview.findViewById(R.id.edtsendr);
        chkalsend = (CheckBox) rootview.findViewById(R.id.chkalsend);
        chkalreciv = (CheckBox) rootview.findViewById(R.id.chkalreciv);
        recfax_reciver = (RecyclerView) rootview.findViewById(R.id.recfax_reciver);
        btnaddreciv = (ImageView) rootview.findViewById(R.id.btnaddreciv);
        edtrecv = (EditText) rootview.findViewById(R.id.edtrecv);
        btnsendadd = (Button) rootview.findViewById(R.id.btnsendadd);
        btnreciveadd = (Button) rootview.findViewById(R.id.btnreciveadd);
        queallsend = (ImageView) rootview.findViewById(R.id.queallsend);
        quenotem = (ImageView) rootview.findViewById(R.id.quenotem);

        queallsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Info");
                builder.setMessage("Get a FeedBack email notice every time the user sends an email to the forwarding address.");
                builder.setNegativeButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);

            }
        });
        quenotem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Info");
                builder.setMessage("Do not use the ‘Include Attachment’ option for the sensitive data of HIPAA compliance");
                builder.setNegativeButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);

            }
        });


        btnsendadd.setOnClickListener(buttonsetsender);

        btnreciveadd.setOnClickListener(buttonsetReciver);

        recfax_sendr.setHasFixedSize(true);
        // The number of Columns
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recfax_sendr.setLayoutManager(layoutManager);

        btnaddsendr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Specify the position
                tofaxmail = edtsendr.getText().toString();
                if (tofaxmail.matches("")) {
                    Toast.makeText(getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                } else {


                    emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
// onClick of button perform this simplest code.
                    if (tofaxmail.matches(emailPattern)) {
                        CheckSenderAllow();

                    } else {
                        Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        recfax_reciver.setHasFixedSize(true);
        // The number of Columns
        recivlaymanager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recfax_reciver.setLayoutManager(recivlaymanager);
        btnaddreciv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Specify the position
                fromfaxmail = edtrecv.getText().toString();
                if (fromfaxmail.matches("")) {
                    Toast.makeText(view.getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }


                emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

// onClick of button perform this simplest code.
                if (fromfaxmail.matches(emailPattern)) {
                    ResExpandList mLog = new ResExpandList();
                    mLog.setString2Value(fromfaxmail);
                    mLog.setItemTypeName("FFReceiver");
                    mLog.setString1Name("EmailName");
                    mLog.setString1Value("");
                    mLog.setString2Name("EmailAddress");
                    mLog.setString3Name("DeliveryType");
                    mLog.setString3Value("Pdf");
                    mLog.setString4Name("AddressType");
                    mLog.setString4Value("To");
                    reslistreciver.add(mLog);
                    receiAdapter.notifyData(reslistreciver);
                    edtrecv.setText("");


                } else {
                    Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        new AttemptLogin().execute();
        getSenderList();
        getReceiverList();
        return rootview;
    }


    public void getSenderList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultRet> retCall = easyApi.userFaxForwardSender(unamee, upaasss, stcookie, uprodid);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<ResultRet>() {
            @Override
            public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {
                Log.e("ressbodyyyy----->", response.body().getSuccess() + "");
                pDialog.dismiss();
                if (response.body().getSuccess().equals("true")) {
                    // Toast.makeText(getContext(), "bello", Toast.LENGTH_LONG).show();
                    reslist = response.body().getResultpera();

                    for (int i = 0; i < reslist.size(); i++) {

                        ResExpandList mLog = new ResExpandList();
                        mLog.setItemTypeName(reslist.get(i).getItemTypeName() + "");
                        mLog.setString1Value(reslist.get(i).getString1Value() + "");
                        mLog.setString1Name(reslist.get(i).getString1Name() + "");
                        mLog.setString2Value(reslist.get(i).getString2Value() + "");
                        mLog.setString2Name(reslist.get(i).getString2Name() + "");
                        mLog.setString3Name(reslist.get(i).getString3Name() + "");
                        mLog.setString3Value(reslist.get(i).getString3Value() + "");
                        mLog.setString4Name(reslist.get(i).getString4Name() + "");
                        mLog.setString4Value(reslist.get(i).getString4Value() + "");

                    }
                    resAdapter = new SenderResAdapter(getContext(), reslist);
                    recfax_sendr.setAdapter(resAdapter);
                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultRet> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CheckSenderAllow() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String stcookie = Config.cokkiee;
        tofaxmail = edtsendr.getText().toString();
        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.CheckAllowSender(stcookie, tofaxmail);
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, final Response<ResultsetForLogin> response) {
                Log.e("get conggp----->", response.body().getMessag() + "--------" + response.body().getStatusCode());

                pDialog.dismiss();

                if (response.body().getStatusCode().equals("200")) {


                    ResExpandList mLog = new ResExpandList();
                    mLog.setString1Value(tofaxmail + "");
                    mLog.setItemTypeName("FFSender");
                    mLog.setString1Name("EmailAddress");
                    if (chkalsend.isChecked()) {
                        mLog.setString2Value("True");

                    } else {
                        mLog.setString2Value("False");
                    }
                    mLog.setString2Name("SendFeedback");
                    mLog.setString3Name("");
                    mLog.setString3Value("");
                    mLog.setString4Name("");
                    mLog.setString4Value("");

                    reslist.add(mLog);
                    resAdapter.notifyData(reslist);
                    edtsendr.setText("");


                } else {
                    Toast.makeText(getContext(), "Email is already used", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Log.e("faillier----->", call + "");

                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });

    }

    View.OnClickListener buttonsetsender = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetfaxForwordSendrList();
        }
    };
    View.OnClickListener buttonsetReciver = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetreciverListt();
        }
    };

    public void getReceiverList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultRet> retCall = easyApi.userFaxForwordReceiver(unamee, upaasss, stcookie, uprodid);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall r----->", retCall + "");

        retCall.enqueue(new Callback<ResultRet>() {
            @Override
            public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {
                Log.e("ressbodyyyy----->", response.body().getSuccess() + "");
                pDialog.dismiss();
                if (response.body().getSuccess().equals("true")) {
                    //   Toast.makeText(getContext(), "bcccc", Toast.LENGTH_LONG).show();
                    reslistreciver = response.body().getResultpera();

                    for (int i = 0; i < reslistreciver.size(); i++) {

                        ResExpandList mLog = new ResExpandList();
                        mLog.setItemTypeName(reslistreciver.get(i).getItemTypeName() + "");
                        mLog.setString1Value(reslistreciver.get(i).getString1Value() + "");
                        mLog.setString1Name(reslistreciver.get(i).getString1Name() + "");
                        mLog.setString2Value(reslistreciver.get(i).getString2Value() + "");
                        mLog.setString2Name(reslistreciver.get(i).getString2Name() + "");
                        mLog.setString3Name(reslistreciver.get(i).getString3Name() + "");
                        mLog.setString3Value(reslistreciver.get(i).getString3Value() + "");
                        mLog.setString4Name(reslistreciver.get(i).getString4Name() + "");
                        mLog.setString4Value(reslistreciver.get(i).getString4Value() + "");
                        receiAdapter = new ReceiverFaxAdapter(getContext(), reslistreciver);
                        recfax_reciver.setAdapter(receiAdapter);

                    }
                    Log.e("pqeweqwr----->", response.body().getResultpera() + "");

                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultRet> call, Throwable t) {
                //    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }

    public void SetfaxForwordSendrList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.show();


        final ArrayList<ResExpandList> newflist = new ArrayList<>();


        Map<String, ResExpandList> mapp = new LinkedHashMap<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < reslist.size(); i++) {
            Log.e("My sendr--> Stri2Value", reslist.get(i).getString2Value() +
                    "\n ItemTypeName" + reslist.get(i).getItemTypeName() +
                    "\n String1Value" + reslist.get(i).getString1Value() +
                    "\n String1Name" + reslist.get(i).getString1Name() +
                    "\n String2Name" + reslist.get(i).getString2Name() +
                    "\n String3Name" + reslist.get(i).getString3Name() +
                    "\n String3Value" + reslist.get(i).getString3Value() +
                    "\n String4Name" + reslist.get(i).getString4Name() +
                    "\n String4Value" + reslist.get(i).getString4Value()

            );


            ResExpandList reslis = new ResExpandList();
            reslis.setItemTypeName(reslist.get(i).getItemTypeName() + "");
            reslis.setString1Name("EmailName");
            reslis.setString1Value("");
            reslis.setString2Value(reslist.get(i).getString1Value() + "");

            reslis.setString2Name("EmailAddress");
            reslis.setString3Name("SendFeedback");
            reslis.setString3Value(reslist.get(i).getString2Value());
            reslis.setString4Name("AddressType");
            reslis.setString4Value("To");
//            My liss-- > Stri2Value:True


//            ItemTypeName FFSender
//            String1Valuechad @chadmatheson.com
//            String1Name EmailAddress
//            String2Name SendFeedback
//            String3 Namenull
//            String3Value null
//            String4Name null
//            String4Value null

            newflist.add(reslis);

        }

        String faxid = "ItemContainers";
        for (int i = 0; i < newflist.size(); i++) {
//            mapp.put(faxid + i, newflist.get(i));
            mapp.put(faxid + (i + 1), newflist.get(i));
            Log.e("faxiddd--->>>>>>", i + "" + new Gson().toJson(newflist.get(i)));
        }
        Log.e("list sizeeee--->>>>>>", newflist.size() + "----mapp---" + mapp.size());

        for (int i = 0; i < mapp.size(); i++) {
            Log.e("congoo--->>>>>>", "" +
                    new Gson().toJson(mapp.get(i)));
//                    Log.e("faxiddd--->>>>>>", i+"" +mapp.get(faxid+i));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.SetFaxForwordSender(uname, upass, cooki, uprodctkey, mapp);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("ressbodyyyy----->", response.body().getDattta() + "");
                pDialog.dismiss();
                if (response.body().getDattta().equals("true")) {
                     Toast.makeText(getContext(), "Record updated successfully", Toast.LENGTH_LONG).show();
                    getSenderList();
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


    public void SetreciverListt() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
//        pDialog.show();


        final ArrayList<ResExpandList> newflist = new ArrayList<>();


        Map<String, ResExpandList> mapp = new LinkedHashMap<>();

        for (int i = 0; i < reslistreciver.size(); i++) {
            Log.e("My recive--> Stri2Value", reslistreciver.get(i).getString2Value() +
                    "\n ItemTypeName" + reslistreciver.get(i).getItemTypeName() +
                    "\n String1Value" + reslistreciver.get(i).getString1Value() +
                    "\n String1Name" + reslistreciver.get(i).getString1Name() +
                    "\n String2Name" + reslistreciver.get(i).getString2Name() +
                    "\n String3Name" + reslistreciver.get(i).getString3Name() +
                    "\n String3Value" + reslistreciver.get(i).getString3Value() +
                    "\n String4Name" + reslistreciver.get(i).getString4Name() +
                    "\n String4Value" + reslistreciver.get(i).getString4Value()
            );


            ResExpandList reslis = new ResExpandList();
            reslis.setItemTypeName(reslistreciver.get(i).getItemTypeName() + "");
            reslis.setString1Name(reslistreciver.get(i).getString1Name() + "");
            reslis.setString1Value(reslistreciver.get(i).getString1Value() + "");
            reslis.setString2Value(reslistreciver.get(i).getString2Value() + "");

            reslis.setString2Name(reslistreciver.get(i).getString2Name() + "");
            reslis.setString3Name(reslistreciver.get(i).getString3Name() + "");
            reslis.setString3Value(reslistreciver.get(i).getString3Value() + "");
            reslis.setString4Name(reslistreciver.get(i).getString4Name() + "");
            reslis.setString4Value(reslistreciver.get(i).getString4Value() + "");

            newflist.add(reslis);

        }

        String faxid = "ItemContainers";
        for (int i = 0; i < newflist.size(); i++) {
//            mapp.put(faxid + i, newflist.get(i));
            mapp.put(faxid + (i + 1), newflist.get(i));
            Log.e("ress faxiddd--->>>>>>", i + "" + new Gson().toJson(newflist.get(i)));
        }
        Log.e("reseive sizeee--->>>>>>", newflist.size() + "----mapp---" + mapp.size());

        for (int i = 0; i < mapp.size(); i++) {
            Log.e("receive congoo--->>>>>>", "" +
                    new Gson().toJson(mapp.get(faxid + i)));
//                    Log.e("faxiddd--->>>>>>", i+"" +mapp.get(faxid+i));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.SetFaxForwordRecive(uname, upass, cooki, uprodctkey, mapp);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");

        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("ressbodyyyy----->", response.body().getDattta() + "");
                pDialog.dismiss();
                if (response.body().getDattta().equals("true")) {
                    Toast.makeText(getContext(), "Record updated successfully", Toast.LENGTH_LONG).show();

                    // Toast.makeText(getContext(), "bello", Toast.LENGTH_LONG).show();
                    getReceiverList();
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


//    class AttemptLogin extends AsyncTask<String, String, String> {
//
//        String responsee;
//        String imagess;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage("Please Wait...");
//            pDialog.setCancelable(false);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////            StringRequest strreq = new StringRequest(Request.Method.POST,
////                    Config.LOGIN_AUTHENTICATE + "GetProductList/JSON",
////                    new Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String Response) {
////                            // get response
////                            Toast.makeText(getContext(),"congooo",Toast.LENGTH_LONG).show();
////                            Log.e("respp-->", Response + "");
////                        }
////                    }, new Response.ErrorListener() {
////                @Override
////                public void onErrorResponse(VolleyError e) {
////
////                    Log.e("errrroorr-->", responsee + "");
//////                    NetworkResponse response = e.networkResponse;
//////                    if (e instanceof ServerError && response != null) {
//////                        try {
//////                            String res = new String(response.data,
//////                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//////                            // Now you can use any deserializer to make sense of data
//////                         //   Log.e("jobjjjjjj-->", obj + "");
//////                            Log.e("strrrrrrr-->", res + "");
//////
//////                            JSONObject obj = new JSONObject(res);
//////                           } catch (UnsupportedEncodingException e1) {
//////                            // Couldn't properly decode data to string
//////                            e1.printStackTrace();
//////                        } catch (JSONException e2) {
//////                            // returned data is not JSONObject?
//////                            e2.printStackTrace();
//////                        }
//////                    }
////                    e.printStackTrace();
////                }
////            }) {
////
//////                @Override
//////                public String getBodyContentType() {
//////                    return "application/json; charset=UTF-8";
//////                }
////
////                @Override
////                public Map<String, String> getParams() {
////                    Map<String, String> params = new HashMap<>();
////                    params.put("Username", "MPatel");
////                    params.put("Password", "Temp2018MPatel");
////                    params.put("Cookies", "false");
////
////
////                    //  params.put("ProductId", Config.prodkey);
////                    Log.e("paramssssssssssssss-->", params + "");
////
////
////                    return params;
////                }
//////
////                public Map<String, String> getHeaders() throws AuthFailureError {
////                    HashMap<String, String> header = new HashMap<String, String>();
////                    header.put("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
////                    header.put("User-agent","My useragent");
////                    return header;
////                }
////
////            };
////
////            // Adding request to request queue
////            MyApplication.getInstance().addToRequestQueue(strreq);
////            Log.e("strreqqqqqqqqqqqqqq-->", strreq + "");
////
//////            Volley.getInstance(this).addToRequestQueue(strreq);
////            // AppController.getInstance().addToRequestQueue(sr);
////            return responsee;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            pDialog.dismiss();
//            Log.e("postttt --->", s + "");
//
//        }
//    }


}
