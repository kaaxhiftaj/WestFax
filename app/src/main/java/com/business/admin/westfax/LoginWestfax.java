package com.business.admin.westfax;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.ConnectivityReceiver;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultRet;
import com.business.admin.westfax.retrofit.ResultsetForLogin;

import java.util.ArrayList;
import java.util.Calendar;

import morxander.editcard.EditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 12-01-2018.
 */

public class LoginWestfax extends AppCompatActivity {
    Button btncreate, btnlogin;
    EditText logmail, logpass;
    ImageView inqlog;
    CheckBox chkremembr;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    UserSessionManager sessionManager;
    String uname, upass;
    ArrayList<ResExpandList> reslistreciver;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginfax);


        EnableRuntimePermissionToAccessCamera();

        sessionManager = new UserSessionManager(LoginWestfax.this);

        logmail = (EditText) findViewById(R.id.logmail);
        logpass = (EditText) findViewById(R.id.logpass);
        btncreate = (Button) findViewById(R.id.btncreate);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        inqlog = (ImageView) findViewById(R.id.inqlog);
        chkremembr = (CheckBox) findViewById(R.id.chkremembr);




//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            ActivityCompat.requestPermissions(LoginWestfax.this,
//                    new String[]{Manifest.permission.CAMERA},
//                    1);
//        }





//        String date = "2018-01-15T23:29:25Z";
//        StringTokenizer tk = new StringTokenizer(date);
//
//        String datee = tk.nextToken();  // <---  yyyy-mm-dd
//        String timee = tk.nextToken();

//        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//        loginPrefsEditor = loginPreferences.edit();
//
//        saveLogin = loginPreferences.getBoolean("saveLogin", false);
//        if (saveLogin == true) {
//            logmail.setText(loginPreferences.getString("username", ""));
//            logpass.setText(loginPreferences.getString("password", ""));
//            chkremembr.setChecked(true);
//        }

        //  Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();
        loginPreferences = getPreferences(Context.MODE_PRIVATE);
        //Retrieving editor
        loginPrefsEditor = loginPreferences.edit();
        chkremembr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
//                SharedPreferences.Editor editor = settings.edit();

                if (sessionManager.isLoggedIn() == true) {


                    loginPrefsEditor.putBoolean("isChecked", isChecked);


//                    user_iid = pref.getString("user_id", "");
                    uname = loginPreferences.getString("Username", "");
                    upass = loginPreferences.getString("Password", "");
                    //    Log.e("pdpdpdd===$$$", pref.getString("password", ""));


                    logmail.setText(uname);
                    logpass.setText(upass);
                    //    Log.e("uunm", usernamee + "");
//                    Log.e("my userid", user_iid + "");
                    //    Log.e("pwddsds^^^^^", passwordd + "");


//            checkBoxRememberMe.setChecked(pref.getBoolean(getString(R.string.tax_log), false));

                    loginPrefsEditor.commit();

                } else {
                    logmail.setText(logmail.getText().toString() + "");
                    logpass.setText(logpass.getText().toString() + "");
                    Toast.makeText(getApplicationContext(), "You are not logged in..", Toast.LENGTH_LONG).show();
                }

            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iin = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(iin);
                finish();
            }
        });

        inqlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginWestfax.this);
                builder.setTitle("Info");
                builder.setMessage(getString(R.string.strinfo));
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

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    if (logmail.getText().toString().length() > 0 || logpass.getText().toString().length() > 0) {
                        userLogin();
                    } else {
                        Toast.makeText(LoginWestfax.this, "Enter all credentials", Toast.LENGTH_SHORT).show();
                    }
                } else

                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginWestfax.this);
                    builder.setTitle("No internet Connection");
                    builder.setMessage("Please turn on internet connection to continue");
                    builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                }


            }
        });
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginWestfax.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
           // Toast.makeText(LoginWestfax.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(LoginWestfax.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

             //       Toast.makeText(LoginWestfax.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

//                    Toast.makeText(LoginWestfax.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void userLogin() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.show();

        uname = logmail.getText().toString();
//                 Config.uname;
        upass = logpass.getText().toString();
//        Config.usrpass;
        final String[] prodid = new String[1];
        final String[] inbounnum = new String[1];
        final String stcookie = "false";

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<ResultsetForLogin> retCall = easyApi.userLogin(uname, upass, stcookie);
        Log.e("rescall----->", retCall + "");


        retCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
                Log.e("responss----->", response + "");
                pDialog.dismiss();
                if (response.body().getStatus().equals("true")) {
                    Toast.makeText(getApplicationContext(), "Successfully Login ", Toast.LENGTH_LONG).show();

                    //   if (chkremembr.isChecked()){


//                    editor.commit();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(logmail.getWindowToken(), 0);


                    LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

                    Call<ResultRet> retCall = easyApi.getProductIDlist(uname, upass, stcookie);
                    Log.e("rescall----->", retCall + "");


                    retCall.enqueue(new Callback<ResultRet>() {
                        @Override
                        public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {
                            pDialog.dismiss();
                            if (response.body().getSuccess().equals("true")) {
                                reslistreciver = response.body().getResultpera();

                                prodid[0] = reslistreciver.get(0).getMid() + "";
                                inbounnum[0] = reslistreciver.get(0).getDetail() + "";
                                Log.e("ssssssssssssss----->", uname + "---" + upass + "------" + prodid + "===" + inbounnum +
                                        reslistreciver.get(0).getDetail());
//
                                if (chkremembr.isChecked()) {

                                    sessionManager.createLoginSession(uname, upass, prodid[0], inbounnum[0], "true");

//                        loginPrefsEditor.putBoolean(Config.IS_LOGGED_IN, true);
//                        loginPrefsEditor.putString(sessionManager.KEY_EMAIL, uname);
//                        loginPrefsEditor.putString(sessionManager.KEY_NAME, upass);
//                        loginPrefsEditor.commit();
                                    Intent iin = new Intent(getApplicationContext(), MainActivity.class);
//                    SharedPreferences.Editor editor = pref.edit();
//                    //  editor.putBoolean(Config.IS_LOGGED_IN, true);
//                    editor.putString(Config.UNAME, uname);
//                    editor.putString(Config.UPASS, upass);
//                    editor.apply();
                                    iin.putExtra("Username", uname);
//                        iin.putExtra("password", upass);
                                    startActivity(iin);
                                    finish();
                                } else {
                                    Log.e("ssssssssssssss----->", uname + "---" + upass + "------" + prodid + inbounnum[0]);
//
//                                    logmail.setText("");
//                                    logpass.setText("");
//
//                                    loginPrefsEditor.clear();
//                                    loginPrefsEditor.commit();
                                    sessionManager.createLoginSession(uname, upass, prodid[0], inbounnum[0], "false");

                                    Intent iin = new Intent(getApplicationContext(), MainActivity.class);
//                    SharedPreferences.Editor editor = pref.edit();
//                    //  editor.putBoolean(Config.IS_LOGGED_IN, true);
//                    editor.putString(Config.UNAME, uname);
//                    editor.putString(Config.UPASS, upass);
//                    editor.apply();
                                    iin.putExtra("Username", uname);
//                        iin.putExtra("Password", upass);
                                    startActivity(iin);
                                    finish();
                                }




                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultRet> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Server Error,Please Retry", Toast.LENGTH_LONG).show();
                        }
                    });


//                }
//
//                    else {
//
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putBoolean(Config.IS_LOGGED_IN, false);
//                        editor.putString(Config.UNAME, uname);
//                        editor.putString(Config.UPASS, upass);
//                        editor.apply();
//                        Intent iin = new Intent(getApplicationContext(), MainActivity.class);
////                    iin.putExtra("username", uname);
////                    iin.putExtra("password", upass);
//                        startActivity(iin);
//                        finish();
//
//
//                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


}
