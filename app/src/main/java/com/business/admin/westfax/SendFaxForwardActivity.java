package com.business.admin.westfax;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.ConnectivityReceiver;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.adapter.SendFaxImageAdapter;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.fragment.AccountSettingFragment;
import com.business.admin.westfax.fragment.MyProfileFragment;
import com.business.admin.westfax.permission.PermissionsActivity;
import com.business.admin.westfax.permission.PermissionsChecker;
import com.business.admin.westfax.retrofit.Dataforall;
import com.business.admin.westfax.retrofit.Datum;
import com.business.admin.westfax.retrofit.FaxCallInfoList;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.shockwave.pdfium.PdfDocument;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 27-02-2018.
 */

public class SendFaxForwardActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, OnPageChangeListener, OnLoadCompleteListener {
    String iid;
    ProgressDialog pDialog;
    PDFView newcrepdf;
    ImageView imgsig;

    Toolbar toolbar;
    DrawerLayout mDrawer;
    android.support.v7.app.ActionBarDrawerToggle toggle;
    UserSessionManager sessionManager;
    String unamee, upaasss, uprodid, direct, datt, tagg, orignumb, newdt, newtim, pagecount;
    Fragment fragment = null;
    String bytenew;
    Integer pageNumber = 0;
    String pdfFileName;

    ImageView fximgaddcon;
    RecyclerView recsendnumfx;
    EditText fxaddcon, fx_header, fxaddsendnum;
    Button btnsendfx;
    PermissionsChecker checker;
    String imagePath;
    SendFaxUserAdapter resUserAdapter;
    ArrayList<ResExpandList> resforsend;
    RecyclerView.LayoutManager linforsend;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    File someFile;
    byte[] decodedString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendfaxactivity);
        checkConnection();
        resforsend = new ArrayList<>();
        sessionManager = new UserSessionManager(SendFaxForwardActivity.this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);
        newcrepdf = (PDFView) findViewById(R.id.newcrepdf);

        Intent ii = getIntent();


        iid = ii.getStringExtra("Id");
        direct = ii.getStringExtra("Direction");
        datt = ii.getStringExtra("Date");
        tagg = ii.getStringExtra("Tag");
        orignumb = ii.getStringExtra("OrigNumber");
        newdt = ii.getStringExtra("Newdate");
        newtim = ii.getStringExtra("Newtime");
        pagecount = ii.getStringExtra("Pagecount");
        bytenew = ii.getStringExtra("vallstr");
        decodedString = ii.getByteArrayExtra("bytedecodestr");
        Log.e("bytee--->", bytenew + "=======" + iid + "=====" + orignumb);
//

        fx_header = (EditText) findViewById(R.id.fx_header);
        fxaddsendnum = (EditText) findViewById(R.id.fxaddsendnum);
        fximgaddcon = (ImageView) findViewById(R.id.fximgaddcon);
        recsendnumfx = (RecyclerView) findViewById(R.id.recsendnumfx);
        fxaddcon = (EditText) findViewById(R.id.fxaddcon);
        btnsendfx = (Button) findViewById(R.id.btnsendfx);


        String imagePath = null;
        try {
            imagePath = new String(decodedString, "UTF-8");

//            decodedString = Base64.decode(base64.toString(), Base64.DEFAULT);

            newcrepdf.fromBytes(decodedString).defaultPage(pageNumber)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(SendFaxForwardActivity.this)
                    .enableAnnotationRendering(true)
                    .onLoad(SendFaxForwardActivity.this)
                    .scrollHandle(new DefaultScrollHandle(SendFaxForwardActivity.this))
                    .load();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View drawerView) {

                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                finish();
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Fax Forword");
        toggle.setDrawerIndicatorEnabled(false);


        recsendnumfx.setHasFixedSize(true);
        // The number of Columns
        linforsend = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recsendnumfx.setLayoutManager(linforsend);
        resUserAdapter = new SendFaxUserAdapter(getApplicationContext(), resforsend);
        recsendnumfx.setAdapter(resUserAdapter);


        fximgaddcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Specify the position
//                showImagePopup(view);
                String title = fxaddsendnum.getText().toString();
                if (title.matches("")) {
                    Toast.makeText(view.getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResExpandList mLog = new ResExpandList();
                mLog.setString2Value(title);
                resforsend.add(mLog);
                resUserAdapter.notifyData(resforsend);
                fxaddsendnum.setText("");
            }
        });
        btnsendfx.setOnClickListener(buttnsend);


    }

    View.OnClickListener buttnsend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!TextUtils.isEmpty(imagePath)) {
//                    uploadImage();
                uploadImage();

            } else {
                Toast.makeText(getApplicationContext(), R.string.string_message_to_attach_file, Toast.LENGTH_SHORT).show();

            }
        }

    };


    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        if (resultCode == 1) {
//            Bitmap b = BitmapFactory.decodeByteArray(
//                    data.getByteArrayExtra("byteArray"), 0,
//                    data.getByteArrayExtra("byteArray").length);
//            imgsig.setImageBitmap(b);
//        } else if (resultCode == RESULT_OK && requestCode == 1010) {
//            if (data == null) {
//                Toast.makeText(getApplicationContext(), R.string.string_unable_to_pick_image, Toast.LENGTH_LONG).show();
//                return;
//            }
//            Uri selectedImageUri = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
//
//            if (cursor != null) {
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imagePath = cursor.getString(columnIndex);
//                cursor.close();
//            } else {
//                Toast.makeText(getApplicationContext(), R.string.string_unable_to_load_image, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = newcrepdf.getDocumentMeta();
        printBookmarksTree(newcrepdf.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("hiiiiiiiii pddf----->", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SendFaxForwardActivity.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.show();

        //Create Upload Server Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Create Upload Server Client
        LogEasyApi service = retrofit.create(LogEasyApi.class);
        //File creating from selected URL
        someFile = new File(imagePath);

        Log.d("fileeeeee-->", someFile.getName() + " ========" + someFile);

        // create RequestBody instance from file
        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");

        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);
        RequestBody ujob = RequestBody.create(MediaType.parse("text/plain"), "Test Job");
        RequestBody uheadr = RequestBody.create(MediaType.parse("text/plain"), fx_header.getText().toString());

        RequestBody ubillcod = RequestBody.create(MediaType.parse("text/plain"), "Customer Code 1234");
        //   RequestBody unumb = RequestBody.create(MediaType.parse("text/plain"), "7324004069");

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), someFile);

        RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
        RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
        RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");


        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("Files1", someFile.getName(), requestFile);

        final ArrayList<ResExpandList> newflist = new ArrayList<>();
        Map<String, ResExpandList> mapp = new LinkedHashMap<>();

        for (int i = 0; i < resforsend.size(); i++) {
            Log.e("My IDDD-->", resforsend.get(i).getString2Value());


            ResExpandList reslis = new ResExpandList();
            reslis.setMid(resforsend.get(i).getString2Value());

            newflist.add(reslis);


        }
        String faxid = "Numbers";
        for (int i = 0; i < newflist.size(); i++) {
//            mapp.put(faxid + i, newflist.get(i));
            mapp.put(faxid + (i + 1), newflist.get(i));

        }

        Call<ResultsetForLogin> resultCall = service.ForworduploadImage(uname, upass, cooki, uprodctkey, ujob, uheadr,
                ubillcod, mapp, body, ucsid, uani, ustartdt, uquality, ufeedmail);
//                ("MPatel", "Temp2018MPatel", "false",
//                "ef6978cf-3695-479c-a5b3-48589b8882f0", "Test Job", "Test Header", "Customer Code 1234",
//                "7324004069", body, "Station Id", "7324004069", "1/1/2018", "Fine", "chad@westfax.com");

        // finally, execute the request
        resultCall.enqueue(new Callback<ResultsetForLogin>() {
            @Override
            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {

                progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {

                    if (response.body().getDattta().equals("true")) {

                        Toast.makeText(SendFaxForwardActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendFaxForwardActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                        //         Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.string_upload_fail, Toast.LENGTH_LONG).show();
                }

                /**
                 * Update Views
                 */
                imagePath = "";
            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SendFaxForwardActivity.this, "error get", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Showing Image Picker
     */
    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
            startActivityForResult(chooserIntent, 1010);
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }
}
