package com.business.admin.westfax.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.business.admin.westfax.adapter.MultiSelectAdapter;
import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.R;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.retrofit.FaxCallInfoList;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultRet;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.business.admin.westfax.utilss.AlertDialogHelper;
import com.business.admin.westfax.utilss.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 17-01-2018.
 */

public class FaxItemFragment extends Fragment implements AlertDialogHelper.AlertDialogListener {
//        , SwipeRefreshLayout.OnRefreshListener {

    ActionMode mActionMode;
    Menu context_menu;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    MultiSelectAdapter multiSelectAdapter;
    boolean isMultiSelect = false;

    ArrayList<ResExpandList> user_list = new ArrayList<>();
    ArrayList<ResExpandList> user_lii ;
    ArrayList<ResExpandList> multi_lii;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialogHelper alertDialogHelper;
    ProgressDialog pDialog;
    AlertDialog alertDialog = null;
    String unamee, upaasss, uprodid,detailid;

    UserSessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_fax_list, container, false);
        sessionManager = new UserSessionManager(getContext());

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);
        // get detailid
         detailid = user.get(UserSessionManager.KEY_INBOUND);

        user_lii = new ArrayList<>();
        multi_lii = new ArrayList<>();
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_fax);


        // multiSelectAdapter = new MultiSelectAdapter(getContext(), user_list, multiselect_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.setAdapter(multiSelectAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swifeRefresh);
//        mSwipeRefreshLayout.setOnRefreshListener(getContext());
//        mSwipeRefreshLayout.post(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         mSwipeRefreshLayout.setRefreshing(true);
//
//                                         getSenderList();
//
//                                     }
//                                 }
//        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);

                                         getSenderList();

                                     }
                                 });
            }
                                                 });

        getSenderList();

        return rootview;
    }

//    @Override
//    public void onRefresh() {
//        user_list.clear();
//        multi_lii.clear();
//        user_lii.clear();
//        getSenderList();
//    }

    public void getSenderList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.show();
//        pDialog.setCancelable(false);
        final String stcookie = Config.cokkiee;
        String startdt = Config.startdatee;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultRet> retCall = easyApi.getAllFaxes(unamee, upaasss, stcookie, uprodid, startdt);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<ResultRet>() {
            @Override
            public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {

                Log.e("ressbodyyyy----->", response.body().getSuccess() + "");

                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body().getSuccess().equals("true")) {
                    //  Toast.makeText(getContext(), "bello", Toast.LENGTH_LONG).show();

                    user_list.clear();
                    user_list = response.body().getResultpera();
                    for (int i = 0; i < user_list.size(); i++) {
                        Log.e("resultpra--->", user_list.get(i).getMid() + "");
                        Log.e("mdirection--->", user_list.get(i).getMdirection() + "");
                        ResExpandList resliss = new ResExpandList();
                        resliss.setMid(user_list.get(i).getMid());
                        resliss.setMdirection(user_list.get(i).getMdirection());
//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl(Config.LOGIN_AUTHENTICATE)
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//
                        JSONObject jsonobject_one = new JSONObject();
                        try {
                            jsonobject_one.put("Id", user_list.get(i).getMid()+"");
                            jsonobject_one.put("Direction", user_list.get(i).getMdirection()+"");
                            jsonobject_one.put("Date", user_list.get(i).getMdate()+"");
                            jsonobject_one.put("Tag", user_list.get(i).getMtag()+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    getFaxList();
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


    public void getFaxList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading");
        pDialog.show();
//        pDialog.setCancelable(false);

        String stcookie = Config.cokkiee;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.e("helllooo cong-->>???", user_list.size() + "");

        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);

        final ArrayList<ResExpandList> newflist = new ArrayList<>();


        Map<String, ResExpandList> mapp = new LinkedHashMap<>();

        for (int i = 0; i < user_list.size(); i++) {
//            Log.e("My lisss-->", user_list.get(i).getMdirection() + "\n IDDD??" + user_list.get(i).getMid());
            ResExpandList reslis = new ResExpandList();
            reslis.setMid(user_list.get(i).getMid()+"");
            reslis.setMdirection(user_list.get(i).getMdirection()+"");
            reslis.setMdate(user_list.get(i).getMdate()+"");
            reslis.setMtag(user_list.get(i).getMtag()+"");
            newflist.add(reslis);
//          body = MultipartBody.Part.createFormData("Files" +i,newflist+"",null);


        }
        String faxid = "FaxIds";
        for (int i = 0; i < newflist.size(); i++) {
//            mapp.put(faxid + i, newflist.get(i));
            mapp.put(faxid + (i + 1), newflist.get(i));

        }

        for (int i = 0; i < mapp.size(); i++) {
            Log.e("faxiddd--->>>>>>", "" +
                    new Gson().toJson(mapp.get(faxid + i)));
//                    Log.e("faxiddd--->>>>>>", i+"" +mapp.get(faxid+i));
        }

        //        MultipartBody.Part body=null ;

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultRet> retCall = easyApi.getFaxesNeww(uname, upass, cooki, uprodctkey, mapp);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
        Log.e("rescall----->", retCall + "");

        retCall.enqueue(new Callback<ResultRet>() {
            @Override
            public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {
                Log.e("getmsg----->", response.body().getMessagee());
                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body().getMessagee().equals("OK")) {
//                    Toast.makeText(getContext(), "get respo", Toast.LENGTH_LONG).show();
//                    user_lii.clear();
//                    multi_lii.clear();
                    List<ResExpandList> jsonArray = response.body().getResultpera();
//                    user_lii.clear();
//                    multi_lii.clear();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ResExpandList itemflt = new ResExpandList();
                        itemflt.setMid(jsonArray.get(i).getMid()+"");
                        itemflt.setMdate(jsonArray.get(i).getMdate()+"");
                        itemflt.setMdirection(jsonArray.get(i).getMdirection()+"");
                        itemflt.setMtag(jsonArray.get(i).getMtag()+"");
                        itemflt.setPageCount(jsonArray.get(i).getPageCount()+"");

                        List<FaxCallInfoList> Orignumb = new ArrayList<FaxCallInfoList>();
                        FaxCallInfoList mLog = new FaxCallInfoList();
                        mLog.setOrigNumber(jsonArray.get(i).getFaxCallInfoList().get(0).getOrigNumber()+"");
                        mLog.setTermNumber(jsonArray.get(i).getFaxCallInfoList().get(0).getTermNumber()+"");
                        mLog.setOrigCSID(jsonArray.get(i).getFaxCallInfoList().get(0).getOrigCSID()+"");
                        Orignumb.add(mLog);
                        itemflt.setFaxCallInfoList(Orignumb);

//                        Log.e("getOrigi----->", jsonArray.get(i).getFaxCallInfoList().get(0).getCallId() + "");
//                        Log.e("getTermno----->", jsonArray.get(i).getFaxCallInfoList().get(0).getTermNumber() + "");
                        user_lii.add(itemflt);

                        multiSelectAdapter = new MultiSelectAdapter(getContext(), user_lii, multi_lii);
                        recyclerView.setAdapter(multiSelectAdapter);
//                        Log.e("getID----->", response.body().getData().get(i).getId() + "");
//                        Log.e("getFaxqaulidty----->", response.body().getData().get(i).getFaxQuality() + "");
                    }
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            if (isMultiSelect) {
                                multi_select(position);
                            }
                            else {
                              //  Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }

                        }


                        @Override
                        public void onItemLongClick(View view, int position) {
//                            mSwipeRefreshLayout.setRefreshing(false);

                      Log.e("itemmm long",position+"==milis"+multi_lii+""+user_lii);
                            if (!isMultiSelect) {
                                multi_lii = new ArrayList<ResExpandList>();
                                isMultiSelect = true;

                                if (mActionMode == null) {
                                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                                }

                            }

                            multi_select(position);

                        }
                    }));
                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultRet> call, Throwable t) {
                Log.e("faillier----->", call + "");
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void multi_select(int position) {


        if (mActionMode != null) {
            if (multi_lii.contains(user_lii.get(position)))
                multi_lii.remove(user_lii.get(position));
            else
                multi_lii.add(user_lii.get(position));

            if (multi_lii.size() > 0)
                mActionMode.setTitle("" + multi_lii.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public void refreshAdapter() {
        multiSelectAdapter.selected_usersList = multi_lii;
        multiSelectAdapter.usersList = user_lii;
        multiSelectAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    //alertDialogHelper.showAlertDialog("","Delete Contact","DELETE","CANCEL",1,false);
                    showAlertDialog("", "Delete Fax?", "DELETE", "CANCEL", 1, false);

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multi_lii = new ArrayList<ResExpandList>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions
    public void showAlertDialog(String title, String message, String positive, String negative, final int from, boolean isCancelable) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        if (!TextUtils.isEmpty(title))
            alertDialogBuilder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(positive)) {
            alertDialogBuilder.setPositiveButton(positive,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            onPositiveClick(from);
                            alertDialog.dismiss();
                        }
                    });
        }
//        if (!TextUtils.isEmpty(neutral)) {
//            alertDialogBuilder.setNeutralButton(neutral,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            onNeutralClick(from);
//                            alertDialog.dismiss();
//                        }
//                    });
//        }
        if (!TextUtils.isEmpty(negative)) {
            alertDialogBuilder.setNegativeButton(negative,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            onNegativeClick(from);
                            alertDialog.dismiss();
                        }
                    });
        } else {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Button negative_button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        negative_button.setVisibility(View.GONE);

                        Looper.loop();

                    }
                }.start();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        alertDialogBuilder.setCancelable(isCancelable);


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multi_lii.size() > 0) {
                for (int i = 0; i < multi_lii.size(); i++) {
                    Log.e("My lisss-->", multi_lii.get(i).getMdirection() + "\n IDDD??" + multi_lii.get(i).getMid());
                    user_lii.remove(multi_lii.get(i));
//user_lii.remove(multiselect_list.get(i).getMid());


                    multiSelectAdapter.notifyDataSetChanged();
//
                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Loading");
                    //        pDialog.show();

                    String stcookie = Config.cokkiee;
                    String filter = Config.filter;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Config.LOGIN_AUTHENTICATE)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    JSONObject jsonobject_one = new JSONObject();
                    try {

                        jsonobject_one.put("Id", multi_lii.get(i).getMid());
                        jsonobject_one.put("Direction", multi_lii.get(i).getMdirection());
                        jsonobject_one.put("Date", multi_lii.get(i).getMdate());
                        jsonobject_one.put("Tag", multi_lii.get(i).getMtag());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

                    Call<ResultsetForLogin> retCall = easyApi.deleteFax(stcookie, jsonobject_one, filter);
//                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
                    Log.e("rescall----->", retCall + "");

                    final int finalI = i;
                    retCall.enqueue(new Callback<ResultsetForLogin>() {
                        @Override
                        public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                            Log.e("getmsg----->", response.body().getDattta());

                            pDialog.dismiss();

                            if (response.body().getDattta().equals("true")) {

                                Toast.makeText(getContext(), "Removed", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                            Log.e("faillier----->", call + "");

                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

                        }
                    });


                }

                if (mActionMode != null) {
                    mActionMode.finish();
                }


                Toast.makeText(getContext(), "Delete Click", Toast.LENGTH_SHORT).show();
            }
        } else if (from == 2) {
            if (mActionMode != null) {
                mActionMode.finish();
            }

            ResExpandList mSample = new ResExpandList("Name" + user_lii.size(), "Designation" + user_lii.size());
            user_lii.add(mSample);
            Log.e("My lisss-->", mSample.getMdirection() + "\n IDDD??" + mSample.getMid());

            multiSelectAdapter.notifyDataSetChanged();


        }

//        if (from == 1) {
//            if (multiselect_list.size() > 0) {
//                for (int i = 0; i < multiselect_list.size(); i++)
//                    user_lii.remove(multiselect_list.get(i));
//
//                multiSelectAdapter.notifyDataSetChanged();
//
//                if (mActionMode != null) {
//                    mActionMode.finish();
//                }
//                Toast.makeText(getContext(), "Delete Click", Toast.LENGTH_SHORT).show();
//            }
//        } else if (from == 2) {
//            if (mActionMode != null) {
//                mActionMode.finish();
//            }
//
//            ResExpandList mSample = new ResExpandList("Name" + user_lii.size(), "Designation" + user_lii.size());
//            user_lii.add(mSample);
//            multiSelectAdapter.notifyDataSetChanged();
//
//        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }


    //    class AttemptLogin extends AsyncTask<String, String, String> {
//
//        String responsee;
//        String imagess;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage("Please Wait...");
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            pDialog.dismiss();
//            Log.d("Logpost", s + "");
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
////            StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, "https://api2.westfax.com/REST/GetFaxForwardReceivers/JSON2", new com.android.volley.Response.Listener<String>() {
////                @Override
////                public void onResponse(String response) {
////                }
////            }, new com.android.volley.Response.ErrorListener() {
////                @Override
////                public void onErrorResponse(VolleyError error) {
////                    Log.d("---->", "onErrorResponse: " + error.getMessage());
////                }
////            }) {
////                @Override
////                protected Map<String, String> getParams() {
////                    Map<String, String> params = new HashMap<String, String>();
////                    params.put("Username", "MPatel");
////                    params.put("Password", "Temp2018MPatel");
////                    params.put("Cookies", "false");
////                    params.put("ProductId", "ef6978cf-3695-479c-a5b3-48589b8882f0");
////                    Log.d("---->", "maro repso: " + params + "");
////                    return params;
////                }
////
////                @Override
////                public Map<String, String> getHeaders() throws AuthFailureError {
////                    Map<String, String> header = new HashMap<String, String>();
////                    header.put("Content-Type", "application/json; charset=utf-8");
////                    header.put("User-agent", "My useragent");
////                    return header;
////                }
////            };
//
//
////            HashMap<String, String> paramshash = new HashMap<String, String>();
////            paramshash.put("Id", "ed75b047-de4a-48fb-ae2b-2ff990f3f97d");
////            paramshash.put("Direction", "Inbound");
////            paramshash.put("Date", "2018-01-16T22:04:07Z");
////            paramshash.put("Tag", "None");
////
//            JSONObject js = new JSONObject();
//            try {
//
//                js.put("Username", "MPatel");
//                js.put("Password", "Temp2018MPatel");
//                js.put("Cookies", "false");
//                js.put("ProductId", "ef6978cf-3695-479c-a5b3-48589b8882f0");
//
//
//                JSONObject jsonobject_one = new JSONObject();
//                jsonobject_one.put("Id", "1b6c05f2-aa98-4aab-a465-ef14bc2656d5");
//                jsonobject_one.put("Direction", "Inbound");
//                jsonobject_one.put("Date", "2018-01-15T23:29:25Z");
//                jsonobject_one.put("Tag", "None");
//
//                JSONObject jsonobject_two= new JSONObject();
//                jsonobject_two.put("Id", "4bdd40d2-0358-4ab3-82d3-3be30445350d");
//                jsonobject_two.put("Direction", "Outbound");
//                jsonobject_two.put("Date", "2017-12-11T21:37:38Z");
//                jsonobject_two.put("Tag", "Retrieved");
//                js.put("FaxIds1", jsonobject_one);
//                js.put("FaxIds2", jsonobject_two);
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            //  JSONObject postparams = new JSONObject(paramshash);
//            Log.e("json objjjjjjjj---->", js + "");
////            try {
////                postparams.put("Id", "ed75b047-de4a-48fb-ae2b-2ff990f3f97d");
////                postparams.put("Direction", "Inbound");
////                postparams.put("Date", "2018-01-16T22:04:07Z");
////                postparams.put("Tag", "None");
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//
//            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
//                    "https://api2.westfax.com/REST/GetFaxForwardReceivers/JSON2",
//                    null, new com.android.volley.Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
////                        parseResponse(response);
//                    Log.e("respp-->", response + "");
//                    try {
//                        JSONObject data = response.getJSONObject("data");
//
//                        try {
//
//
//                        } catch (Exception e) {
//                            Log.e("MainAcvtivity", " exoplayer error " + e.toString());
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            },
//
//                    new com.android.volley.Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("---->", "onErrorResponse: " + error.getMessage());
//                            if (pDialog != null)
//                                pDialog.dismiss();
//                        }
//                    })
//
//            {
////                @Override
//
//                /**
//                 * Passing user parameters to our server
//                 * @return
//                 */
////                public byte[] getBody() throws AuthFailureError {
////                    //        Map<String, String> params = getParams();
////                    Map<String, String> params = new HashMap<String, String>();
////                    params.put("id", "1");
////                    params.put("name", "myname");
////                    if (params != null && params.size() > 0) {
////                        return encodeParameters(params, getParamsEncoding());
////                    }
////                    return params;
////                }
//
//                @Override
//                public Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Username", "MPatel");
//                    params.put("Password", "Temp2018MPatel");
//                    params.put("Cookies", "false");
//                    params.put("ProductId", "ef6978cf-3695-479c-a5b3-48589b8882f0");
//
////                    JSONObject postparams1 = new JSONObject();
////                    try {
////                        postparams1.put("Id", "ed75b047-de4a-48fb-ae2b-2ff990f3f97d");
////                        postparams1.put("Direction", "Inbound");
////                        postparams1.put("Date", "2018-01-16T22:04:07Z");
////                        postparams1.put("Tag", "None");
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////
////
//                    Log.e("", "Posting params:-------> " + params.toString());
//////                    params.put("Content-Type", "application/json; charset=utf-8");
////
//                    return params;
//                }
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> header = new HashMap<String, String>();
//                    header.put("Content-Type", "application/json; charset=utf-8");
//                    header.put("User-agent", "My useragent");
//                    return header;
//                }
//
//            };
//
//            // Adding request to request queue
//            MyApplication.getInstance().addToRequestQueue(strReq);
//
//            return responsee;
//        }
//    }

//    public void data_load() {
//        String name[] = {"303-506-9652", "123-456-6897", "011-999-4412", "112-855-9658", "568-854-7552", "666-889-7745"};
//        String posting[] = {"Inbound", "Outbound", "Outbound", "Inbound", "Inbound", "Outbound"};
//
//        for (int i = 0; i < name.length; i++) {
//            SampleModel mSample = new SampleModel(name[i], posting[i]);
//            user_list.add(mSample);
//        }
//    }


}
