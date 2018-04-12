package com.business.admin.westfax.retrofit;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by SONY on 26-01-2018.
 */

public interface LogEasyApi {
    //////// for Login
    @FormUrlEncoded
    @POST("Authenticate/JSON2")
    Call<ResultsetForLogin> userLogin(@Field("Username") String usrname,
                                      @Field("Password") String password,
                                      @Field("Cookies") String cookie);


    /////////////for GetFaxForwardSender
    @FormUrlEncoded
    @POST("GetFaxForwardSenders/JSON2")
    Call<ResultRet> userFaxForwardSender(@Field("Username") String usr,
                                         @Field("Password") String pass,
                                         @Field("Cookies") String cook,
                                         @Field("ProductId") String prod);

    /////////////for GetFaxForwardReceivers
    @FormUrlEncoded
    @POST("GetFaxForwardReceivers/JSON2")
    Call<ResultRet> userFaxForwordReceiver(@Field("Username") String usrname,
                                           @Field("Password") String password,
                                           @Field("Cookies") String cookie,
                                           @Field("ProductId") String prodctid);


    ////////////////////////// for get Product list
    @FormUrlEncoded
    @POST("GetProductList/JSON2")
    Call<ResultRet> getProductIDlist(@Field("Username") String usrname,
                                     @Field("Password") String password,
                                     @Field("Cookies") String cookie);


    ////////////////////////// for get All Faxes
    @FormUrlEncoded
    @POST("GetFaxIdentifiers/JSON2")
    Call<ResultRet> getAllFaxes(@Field("Username") String usrname,
                                @Field("Password") String password,
                                @Field("Cookies") String cookie, @Field("ProductId") String prodctid,
                                @Field("StartDate") String startdate);

    ////////////////////////// for get All Faxes
    @FormUrlEncoded
    @POST("GetFaxIdentifiers/JSON2")
    Call<ResultRet> getAllFaxesInbox(@Field("Username") String usrname,
                                     @Field("Password") String password,
                                     @Field("Cookies") String cookie, @Field("ProductId") String prodctid,
                                     @Field("StartDate") String startdate);

    ////////////////////////// for get All Faxes
    @FormUrlEncoded
    @POST("GetFaxIdentifiers/JSON2")
    Call<ResultRet> getAllFaxesOutbox(@Field("Username") String usrname,
                                      @Field("Password") String password,
                                      @Field("Cookies") String cookie, @Field("ProductId") String prodctid,
                                      @Field("StartDate") String startdate);

    ////////////////////////// for get All Faxes Description
    @FormUrlEncoded
    @POST("GetFaxDescriptionsUsingIds/JSON2")
    Call<ResultRet> getFaxesDescription(@Field("Username") String usrname,
                                        @Field("Password") String password,
                                        @Field("Cookies") String cookie,
                                        @Field("ProductId") String prodctid,
                                        @Field("FaxIds1") ArrayList<ResExpandList> newflist);

    ////////////////////////// for get All Faxes Description
    @FormUrlEncoded
    @POST("GetFaxDescriptionsUsingIds/JSON2")
    Call<ResultRet> getFaxesDescriptionforInbox(@Field("Username") String usrname,
                                                @Field("Password") String password,
                                                @Field("Cookies") String cookie,
                                                @Field("ProductId") String prodctid,
                                                @Field("FaxIds1") JSONObject fax1);

    ////////////////////////// for get All Faxes Description
    @FormUrlEncoded
    @POST("GetFaxDescriptionsUsingIds/JSON2")
    Call<ResultRet> getFaxesDescriptionforOutbox(@Field("Username") String usrname,
                                                 @Field("Password") String password,
                                                 @Field("Cookies") String cookie,
                                                 @Field("ProductId") String prodctid,
                                                 @Field("FaxIds1") JSONObject fax1);

    ///////////////////////////for contact details
    @FormUrlEncoded
    @POST("GetContact/JSON2")
    Call<JsonObjCust> getContactDetails(@Field("Username") String usrname,
                                        @Field("Password") String password,
                                        @Field("Cookies") String cookie);


    ///////////////////////////for TollFreeNumber
//    @Multipart
    @FormUrlEncoded
    @POST("SearchInboundNumbers/JSON2")
    Call<SignToll> gettollfree(@Field("MethodParams1") JSONObject fromtime, @Field("Cookies") String Cookie);


    ///////////////////////////for Areacode Number
//    @Multipart
    @FormUrlEncoded
    @POST("SearchInboundNumbers/JSON2")
    Call<SignToll> getAreaCodeNumber(@Field("MethodParams1") JSONObject method1, @Field("MethodParams2") JSONObject method2, @Field("Cookies") String Cookie);


    ///////////////////////////for Username validation
    @FormUrlEncoded
    @POST("CheckUserName/JSON2")
    Call<ResultsetForLogin> getUsernameUnique(@Field("Cookies") String Cookie, @Field("Username") String usrname);


    ///////////////////////////for Get AreaCode
//    @Multipart
    @FormUrlEncoded
    @POST("SearchUnusedInboundAreaCodes/JSON2")
    Call<SignToll> getAreaCode(@Field("Cookies") String Cookie);

    ///////////////////////////for Create Account

    @FormUrlEncoded
    @POST("CreateFFFreeTrial/JSON2")
    Call<ResultsetForLogin> createAccount(@Field("Cookies") String Cookie,
                                          @Field("CrmContact") JSONObject crmobj,
                                          @Field("PmtCard") JSONObject pmtobj,
                                          @Field("MethodParams1") JSONObject mthdoneobj,
                                          @Field("MethodParams2") JSONObject mthdtwoobj,
                                          @Field("MethodParams3") JSONObject mthdthreobj,
                                          @Field("MethodParams4") JSONObject mthdforobj,
                                          @Field("MethodParams5") JSONObject mthdfivobj,
                                          @Field("MethodParams6") JSONObject mthdsixobj,
                                          @Field("MethodParams7") JSONObject mthdsevnobj);

    @FormUrlEncoded
    @POST("CreateFFFreeTrial/JSON2")
    Call<ResultsetForLogin> createAccountForFive(@Field("Cookies") String Cookie,
                                                 @Field("CrmContact") JSONObject crmobj,
                                                 @Field("MethodParams1") JSONObject mthdoneobj,
                                                 @Field("MethodParams2") JSONObject mthdtwoobj,
                                                 @Field("MethodParams3") JSONObject mthdthreobj,
                                                 @Field("MethodParams4") JSONObject mthdforobj,
                                                 @Field("MethodParams5") JSONObject mthdfivobj,
                                                 @Field("MethodParams6") JSONObject mthdsixobj,
                                                 @Field("MethodParams7") JSONObject mthdsevnobj);

    ///////////////////////////for Get FaxDocument  Details

    @FormUrlEncoded
    @POST("GetFaxDocuments/JSON2")
    Call<Dataforall> GetDocPdf(@Field("Username") String usrname,
                               @Field("Password") String password,
                               @Field("Cookies") String cookie,
                               @Field("ProductId") String prodctid,
                               @Field("FaxIds1") JSONObject fax1,
                               @Field("Format") String format);

    ///////////////////////////for Get FaxDocument  Details

    @FormUrlEncoded
    @POST("SendFaxAsEmail/JSON2")
    Call<ResultsetForLogin> SendFaxasEmail(@Field("Username") String usrname,
                                           @Field("Password") String password,
                                           @Field("Cookies") String cookie,
                                           @Field("ProductId") String prodctid,
                                           @Field("FaxIds1") JSONObject fax1,
                                           @Field("FeedbackEmail") String mail);

    ///////////////////////////for Check Allow Sender

    @FormUrlEncoded
    @POST("CheckAllowedSender/JSON2")
    Call<ResultsetForLogin> CheckAllowSender(@Field("Cookies") String cookie,
                                             @Field("FeedbackEmail") String mail);

    ///////////////////////////for Delete Fax

    @FormUrlEncoded
    @POST("ChangeFaxFilterValue/JSON2")
    Call<ResultsetForLogin> deleteFax(@Field("Cookies") String cookies,
                                      @Field("FaxIds1") JSONObject fax1,
                                      @Field("Filter") String filter);

    ///////////////////////////for Change Password

    @FormUrlEncoded
    @POST("SetUserPassword/JSON2")
    Call<ResultsetForLogin> changepass(@Field("Username") String usrname,
                                       @Field("Password") String password,
                                       @Field("Cookies") String cookies,
                                       @Field("MethodParams1") JSONObject fax1);

    ///////////////////////////for Update Details

    @FormUrlEncoded
    @POST("SetContact/JSON2")
    Call<ResultsetForLogin> SetContactDetails(@Field("Username") String usrname,
                                              @Field("Password") String password,
                                              @Field("Cookies") String cookies,
                                              @Field("CrmContact") JSONObject fax1);


    @Multipart
//    @POST("upload.php")
    @POST("GetFaxDescriptionsUsingIds/JSON2")
    Call<ResultRet> getFaxesAllDesc(@Part("Username") RequestBody usrname,
                                    @Part("Password") RequestBody password,
                                    @Part("Cookies") RequestBody cookie,
                                    @Part("ProductId") RequestBody prodctid,
                                    @Part MultipartBody.Part fax1);


    @FormUrlEncoded
    @POST("GetAppConfiguration/JSON2")
    Call<ResultRet> getAppConfig(@Field("Username") String usrname,
                                 @Field("Password") String password,
                                 @Field("Cookies") String cookie,
                                 @Field("ProductId") String prodctid,
                                 @Field("StartDate") String startdt);


    @Multipart
//    @POST("upload.php")
    @POST("GetFaxDescriptionsUsingIds/JSON2")
    Call<ResultRet> getFaxesNeww(@Part("Username") RequestBody usrname,
                                 @Part("Password") RequestBody password,
                                 @Part("Cookies") RequestBody cookie,
                                 @Part("ProductId") RequestBody prodctid,
                                 @PartMap Map<String, ResExpandList> mapp);

    @Multipart
    @POST("SetFaxForwardSenders/JSON2")
    Call<ResultsetForLogin> SetFaxForwordSender(@Part("Username") RequestBody usrname,
                                                @Part("Password") RequestBody password,
                                                @Part("Cookies") RequestBody cookie,
                                                @Part("ProductId") RequestBody prodctid,
                                                @PartMap Map<String, ResExpandList> mapp);

    @Multipart
    @POST("SetFaxForwardReceivers/JSON2")
    Call<ResultsetForLogin> SetFaxForwordRecive(@Part("Username") RequestBody usrname,
                                                @Part("Password") RequestBody password,
                                                @Part("Cookies") RequestBody cookie,
                                                @Part("ProductId") RequestBody prodctid,
                                                @PartMap Map<String, ResExpandList> mapp);

    @Multipart
    @POST("SendFax/JSON2")
    Call<ResultsetForLogin> uploadImage(@Part("Username") RequestBody usrname,
                                        @Part("Password") RequestBody password,
                                        @Part("Cookies") RequestBody cookies,
                                        @Part("ProductId") RequestBody prodctid,
                                        @Part("JobName") RequestBody jobname,
                                        @Part("Header") RequestBody header,
                                        @Part("BillingCode") RequestBody billcode,
                                        @PartMap Map<String, ResExpandList> numbs,
                                        @Part List<MultipartBody.Part> files,
//                                        @Part MultipartBody.Part files,
                                        @Part("CSID") RequestBody csid,
                                        @Part("ANI") RequestBody ani,
                                        @Part("StartDate") RequestBody startdat,
                                        @Part("FaxQuality") RequestBody faxquality,
                                        @Part("FeedbackEmail") RequestBody feedbk);


    @Multipart
    @POST("SendFax/JSON2")
    Call<ResultsetForLogin> uploadImagesingle(@Part("Username") RequestBody usrname,
                                              @Part("Password") RequestBody password,
                                              @Part("Cookies") RequestBody cookies,
                                              @Part("ProductId") RequestBody prodctid,
                                              @Part("JobName") RequestBody jobname,
                                              @Part("Header") RequestBody header,
                                              @Part("BillingCode") RequestBody billcode,
                                              @Part("Numbers1") RequestBody numbs,
                                              @Part MultipartBody.Part files,
//                                            @Part MultipartBody.Part files,
                                              @Part("CSID") RequestBody csid,
                                              @Part("ANI") RequestBody ani,
                                              @Part("StartDate") RequestBody startdat,
                                              @Part("FaxQuality") RequestBody faxquality,
                                              @Part("FeedbackEmail") RequestBody feedbk);

    @Multipart
    @POST("SendFax/JSON2")
    Call<ResultsetForLogin> ForworduploadImage(@Part("Username") RequestBody usrname,
                                               @Part("Password") RequestBody password,
                                               @Part("Cookies") RequestBody cookies,
                                               @Part("ProductId") RequestBody prodctid,
                                               @Part("JobName") RequestBody jobname,
                                               @Part("Header") RequestBody header,
                                               @Part("BillingCode") RequestBody billcode,
                                               @PartMap Map<String, ResExpandList> numbs,
                                               @Part MultipartBody.Part file,
                                               @Part("CSID") RequestBody csid,
                                               @Part("ANI") RequestBody ani,
                                               @Part("StartDate") RequestBody startdat,
                                               @Part("FaxQuality") RequestBody faxquality,
                                               @Part("FeedbackEmail") RequestBody feedbk);


    //@Part List<MultipartBody.Part> files,
//    @Part MultipartBody.Part file

}
