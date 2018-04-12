package com.business.admin.westfax;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.business.admin.westfax.fragment.SignupFragment;
import com.business.admin.westfax.model.MyPagerAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by SONY on 21-01-2018.
 */

public class RegisterActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerviewpager);

        Fragment fragment = new SignupFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainfrm, fragment);
        transaction.commit();
//        ViewPager vpPager = (ViewPager) findViewById(R.id.regview_pager);
//        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
//        vpPager.setAdapter(adapterViewPager);

    }



    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                Toast.makeText(MainActivity.this,"You clicked yes
//                                        button",Toast.LENGTH_LONG).show();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//            Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {

            // only show dialog while there's back stack entry
            getFragmentManager().popBackStack();

        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();

        }



//        if(getFragmentManager().getBackStackEntryCount() > 0){
//            getFragmentManager().popBackStackImmediate();
//        }
//        else{
//            super.onBackPressed();
//        }

    }
//
//

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        if ( progressDialog!=null && progressDialog.isShowing() ){
//            progressDialog.cancel();
//        }
//    }
    }

