package com.business.admin.westfax;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.ConnectivityReceiver;
import com.business.admin.westfax.adapter.MultiSelectAdapter;
import com.business.admin.westfax.fragment.AccountSettingFragment;
import com.business.admin.westfax.fragment.FaxItemFragment;
import com.business.admin.westfax.fragment.FileFaxSendFragment;
import com.business.admin.westfax.fragment.InboxFragment;
import com.business.admin.westfax.fragment.MyProfileFragment;
import com.business.admin.westfax.fragment.NewSendFragment;
import com.business.admin.westfax.fragment.OutBoundFragment;
import com.business.admin.westfax.fragment.SendFaxFragment;
import com.business.admin.westfax.model.SampleModel;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.retrofit.FaxCallInfoList;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultRet;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.business.admin.westfax.utilss.AlertDialogHelper;
import com.business.admin.westfax.utilss.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    //        implements AlertDialogHelper.AlertDialogListener{
    Toolbar toolbar;
    DrawerLayout mDrawer;
    private NavigationView navigationView;
    android.support.v7.app.ActionBarDrawerToggle toggle;

    ActionMode mActionMode;
    Menu context_menu;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    MultiSelectAdapter multiSelectAdapter;
    boolean isMultiSelect = false;

    ArrayList<SampleModel> user_list = new ArrayList<>();
    ArrayList<SampleModel> multiselect_list = new ArrayList<>();

    AlertDialogHelper alertDialogHelper;
    Fragment fragment = null;
    String unamee, upaasss, uprodid, detailid;
    SharedPreferences pref;
    UserSessionManager sessionManager;
    String mains;
    private final int CAMERA_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        checkConnection();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }




        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(navigationView);
//        checkRunTimePermission();
        sessionManager = new UserSessionManager(MainActivity.this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);
        detailid = user.get(UserSessionManager.KEY_INBOUND);
//        SharedPreferences pref = getSharedPreferences("loginPrefs",MODE_PRIVATE);
//        unamee = pref.getString("Username", null);
//        upaasss = pref.getString("Password", null);

//       Intent iin = getIntent();
//        unamee = iin.getStringExtra("username");
//        upaasss = iin.getStringExtra("password");
        Log.e("sessionnn-+++++++??", unamee + "===" + upaasss + "=========" + uprodid + detailid);
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 152);
        }




        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {


            /** Called when drawer is closed */
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawer.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String s = detailid;
        String s1 = s.substring(0, 3);
        String s2 = s.substring(3, 6);
        String s3 = s.substring(6, 10);
        mains = s1 + ")" + s2 + "-" + s3;

        getSupportActionBar().setTitle("All Faxes" + "(" + mains);


        Fragment fragment = new FaxItemFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mfff, fragment)
                .commit();


    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
//    private void checkRunTimePermission() {
//        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permissionArrays, 11111);
//        } else {
//            // if already permition granted
//            // PUT YOUR ACTION (Like Open cemara etc..)
//        }
//    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message = null;
        int color = 0;
        if (isConnected) {


//            message = "Good! Connected to Internet";
//            color = Color.WHITE;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkConnection();
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);


//            Snackbar snackbar = Snackbar
//                    .make(findViewById(R.id.mainFrame), message, Snackbar.LENGTH_LONG);
//
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(color);
//            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        selectDrawerItem(menuItem);

                        return true;
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {

            //noinspection SimplifiableIfStatement

            return true;
        }

        String title = getString(R.string.app_name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (id) {
            case R.id.menu_myproff:
                fragment = new MyProfileFragment();

                fragmentManager.beginTransaction()
                        .replace(R.id.mfff, fragment)
                        .commit();
                title = "My Profile";
                return true;
            case R.id.menu_faxset:
                fragment = new AccountSettingFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.mfff, fragment)
                        .commit();
                title = "Fax Settings";
                return true;
            case R.id.menu_logout:
                title = "Logout";
                logout();
                return true;
            case R.id.menu_faq:

                final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading");
                pDialog.show();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Config.LOGIN_AUTHENTICATE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                String stcookie = Config.cokkiee;

                LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

                Call<ResultRet> retCall = easyApi.getAppConfig(unamee,upaasss,stcookie,uprodid,Config.startdatee);


                Log.e("rescall----->", retCall + "");

                retCall.enqueue(new Callback<ResultRet>() {
                    @Override
                    public void onResponse(Call<ResultRet> call, Response<ResultRet> response) {
                        Log.e("getmsg----->", response.body().getStatusCode());
                        pDialog.dismiss();
                        if (response.body().getStatusCode().equals("200")) {
                            List<ResExpandList> jsonArray = response.body().getResultpera();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                ResExpandList itemflt = new ResExpandList();
                                itemflt.setmName(jsonArray.get(i).getmName() + "");
                                itemflt.setmValue(jsonArray.get(i).getmValue() + "");
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse(jsonArray.get(i).getmValue()+""));
                                startActivity(viewIntent);
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<ResultRet> call, Throwable t) {
                        Log.e("faillier----->", call + "");
                        pDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });

//                Intent viewIntent =
//                        new Intent("android.intent.action.VIEW",
//                                Uri.parse("http://westfax.com/broadcast-fax-faq/"));
//                startActivity(viewIntent);
                return true;

            case R.id.menu_exit:
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Do you want to Exit?")
//                        .setConfirmText(getString(R.string.alright))
//                        .setCancelText(getString(R.string.cancel))
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {

                finish();
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
                return true;
        }
        return super.

                onOptionsItemSelected(item);

    }

    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Logout");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to logout?");

        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logoutUser();
                Intent i = new Intent(getApplicationContext(), LoginWestfax.class);
                // Closing all the Activities from stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                startActivity(i);
                finish();
//                SharedPreferences.Editor editor = pref.edit();
//            //    editor.putBoolean(Config.IS_LOGGED_IN, false);
//                editor.putString(sessionManager.KEY_EMAIL, "");
//                editor.putString(sessionManager.KEY_NAME, "");
//                editor.apply();
//
//                Intent i = new Intent(getApplicationContext(), LoginWestfax.class);
//
//                // Closing all the Activities from stack
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                // Add new Flag to start new Activity
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                // Staring Login Activity
//                startActivity(i);
//                finish();
                dialog.cancel();
                // Write your code here to invoke YES event
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                dispatchTakenPictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
//        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
//        switch (paramInt) {
//        }
//        do {
//            return;
//        }
//        while (((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == 0)) || (paramArrayOfInt.length <= 0) || (paramArrayOfInt[0] != -1));

    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    public boolean selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        int id = menuItem.getItemId();


//        Fragment fragment = null;
        Intent i = null;
        String title = getString(R.string.app_name);

        if (id == R.id.itm_all) {
            fragment = new FaxItemFragment();
            mDrawer.closeDrawers();
            title = "All Faxes" + "(" + mains;
        } else if (id == R.id.itm_inbox) {
            fragment = new InboxFragment();
            mDrawer.closeDrawers();
            title = "Inbox" + "(" + mains;
        } else if (id == R.id.itm_sent) {
            fragment = new OutBoundFragment();
            mDrawer.closeDrawers();
            title = "Sent Items" + "(" + mains;
        } else if (id == R.id.itm_sendfax) {
            fragment = new FileFaxSendFragment();
            mDrawer.closeDrawers();
            title = "Send Fax" + "(" + mains;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mfff, fragment)
                    .commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
            mDrawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

//    public void data_load() {
//        String name[] = {"Gokul", "Rajesh", "Ranjith", "Madhu", "Ameer", "Sonaal"};
//        String posting[] = {"Manager", "HR", "Android Developer", "iOS Developer", "Team Leader", "Designer"};
//
//        for (int i = 0; i < name.length; i++) {
//            SampleModel mSample = new SampleModel(name[i], posting[i]);
//            user_list.add(mSample);
//        }
//    }
//
//
//    public void multi_select(int position) {
//        if (mActionMode != null) {
//            if (multiselect_list.contains(user_list.get(position)))
//                multiselect_list.remove(user_list.get(position));
//            else
//                multiselect_list.add(user_list.get(position));
//
//            if (multiselect_list.size() > 0)
//                mActionMode.setTitle("" + multiselect_list.size());
//            else
//                mActionMode.setTitle("");
//
//            refreshAdapter();
//
//        }
//    }
//
//
//    public void refreshAdapter()
//    {
//        multiSelectAdapter.selected_usersList=multiselect_list;
//        multiSelectAdapter.usersList=user_list;
//        multiSelectAdapter.notifyDataSetChanged();
//    }
//
//    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            // Inflate a menu resource providing context menu items
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.menu_multi_select, menu);
//            context_menu = menu;
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false; // Return false if nothing is done
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_delete:
//                    alertDialogHelper.showAlertDialog("","Delete Contact","DELETE","CANCEL",1,false);
//                    return true;
//                default:
//                    return false;
//            }
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mActionMode = null;
//            isMultiSelect = false;
//            multiselect_list = new ArrayList<SampleModel>();
//            refreshAdapter();
//        }
//    };
//
//    // AlertDialog Callback Functions
//
//    @Override
//    public void onPositiveClick(int from) {
//        if(from==1)
//        {
//            if(multiselect_list.size()>0)
//            {
//                for(int i=0;i<multiselect_list.size();i++)
//                    user_list.remove(multiselect_list.get(i));
//
//                multiSelectAdapter.notifyDataSetChanged();
//
//                if (mActionMode != null) {
//                    mActionMode.finish();
//                }
//                Toast.makeText(getApplicationContext(), "Delete Click", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else if(from==2)
//        {
//            if (mActionMode != null) {
//                mActionMode.finish();
//            }
//
//            SampleModel mSample = new SampleModel("Name"+user_list.size(),"Designation"+user_list.size());
//            user_list.add(mSample);
//            multiSelectAdapter.notifyDataSetChanged();
//
//        }
//    }
//
//    @Override
//    public void onNegativeClick(int from) {
//
//    }
//
//    @Override
//    public void onNeutralClick(int from) {
//
//    }
}
