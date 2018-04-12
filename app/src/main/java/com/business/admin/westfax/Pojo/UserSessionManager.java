package com.business.admin.westfax.Pojo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.business.admin.westfax.LoginWestfax;
import java.util.HashMap;


/**
 * Created by SONY on 10-02-18.
 */
public class UserSessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // Username address (make variable public to access from outside)
    public static final String KEY_EMAIL = "Username";

//     User password (make variable public to access from outside)
    public static final String KEY_NAME = "Password";

//     User Productid (make variable public to access from outside)
    public static final String KEY_PROID="ProductId";

//     User DetailInound (make variable public to access from outside)
     public static final String KEY_INBOUND="Detail";

     public static final String LOG_STATUS ="logstat";

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String name, String idd,String inbounddetail,String logstat) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        // Storing productid in pref
        editor.putString(KEY_PROID, idd);
        // Storing detail in pref
        editor.putString(KEY_INBOUND, inbounddetail);
        // Storing detail in pref
        editor.putString(LOG_STATUS, logstat);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginWestfax.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user pass
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user ID
        user.put(KEY_PROID, pref.getString(KEY_PROID, null));

        // user INBOUNDDETAIL
        user.put(KEY_INBOUND, pref.getString(KEY_INBOUND, null));
        // user Loginstatus
        user.put(LOG_STATUS, pref.getString(LOG_STATUS, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginWestfax.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//        // Staring Login Activity
//        _context.startActivity(i);

    }

    public void logpass() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

//        driverLogin.finish();

//        driverLogin.onBackPressed();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}