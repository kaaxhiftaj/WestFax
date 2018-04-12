package com.business.admin.westfax.Pojo;

import com.business.admin.westfax.retrofit.LogEasyApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by SONY on 26-01-2018.
 */

public class Config {
    public static String LOGIN_AUTHENTICATE = "https://api2.westfax.com/REST/";

//    public static final String uname = "MPatel";
////    sarinjohn
//    public static final String usrpass = "Temp2018MPatel";
////    123456
//    public static final String prodkey = "ef6978cf-3695-479c-a5b3-48589b8882f0";
////    537acdd8-0454-4a71-99a8-e187cd60f50d
    public static final String cokkiee = "false";
    public static final String startdatee = "12/1/2017";
    public static final String fileformat = "pdf";
    public static final String filter = "Removed";


    public static final String IS_LOGGED_IN = "isLoggedIn";

    public static final String UNAME = "username";
    public static final String UPASS = "password";
    private static LogEasyApi apiService;
    private static Retrofit retrofit = null;

    public static LogEasyApi getApiService(String baseUrl) {
        LOGIN_AUTHENTICATE = baseUrl;
        apiService = getClient(LOGIN_AUTHENTICATE).create(LogEasyApi.class);
        return apiService;
    }


    public static LogEasyApi getApiService() {
        //if (apiService == null)
        apiService = getApiService(LOGIN_AUTHENTICATE);
        return apiService;
    }
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient(String baseUrl) {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

       /* retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }



}
