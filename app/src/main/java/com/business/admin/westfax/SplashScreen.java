package com.business.admin.westfax;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by SONY on 10-01-2018.
 */

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1900;
    Intent intent;
    UserSessionManager sessionManager;
    private ArrayList<String> filePaths;
    ArrayList<String> selectedPaths;
    Button btnsendfx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);




//
//filePaths= new ArrayList<>();
//selectedPaths= new ArrayList<>();
//        btnsendfx = (Button)findViewById(R.id.btnsendfx);
//btnsendfx.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        showFileChooser();
//    }
//});
        new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            @Override
            public void run() {
                sessionManager = new UserSessionManager(getApplicationContext());


                if (sessionManager.checkLogin())
                    finish();

                // get user data from session
                HashMap<String, String> user = sessionManager.getUserDetails();
                // get uname
                String uname = user.get(UserSessionManager.KEY_EMAIL);
                // get name
                String pass = user.get(UserSessionManager.KEY_NAME);
                // get ID
                String prodid = user.get(UserSessionManager.KEY_PROID);

                // get detailid
                String detailid = user.get(UserSessionManager.KEY_INBOUND);
                String checkstat = user.get(UserSessionManager.LOG_STATUS);

                Log.e("Sahred uu", uname + "=- usr typp--" + pass + "---\n detailid-" + detailid + "--" + prodid + "---" + checkstat);

                if (uname == "" || uname == null || uname == "null") {
//                    Toast.makeText(SplashScreen.this, "You are not logged in", Toast.LENGTH_SHORT).show();
                    //  finish();
                } else {


                    if (checkstat.equals("false")) {
                        sessionManager.logoutUser();

                        intent = new Intent(SplashScreen.this, LoginWestfax.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Username", uname);
                        intent.putExtra("Password", pass);
                        intent.putExtra("ProductId", prodid);
                        intent.putExtra("Detail", detailid);
                        startActivity(intent);
                        finish();
                   //     Toast.makeText(getApplicationContext(), "Already Login", Toast.LENGTH_SHORT).show();
                    }
                }


//                Intent intent = new Intent(SplashScreen.this, LoginWestfax.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }


        }, SPLASH_TIME_OUT);


    }


}
