package com.business.admin.westfax;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.FileUtils;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import org.json.JSONObject;

import java.io.File;
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

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PDFViewActivity";
    int position = -1;
    Button sharee;
    File fileneww;
    FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    String iid, direct, datt, tagg, orignumb, newdt, newtim, pagecount, allbase;
    UserSessionManager sessionManager;
    String unamee, upaasss, uprodid;
    byte[] decodedString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        sessionManager = new UserSessionManager(PDFViewActivity.this);
        sharee = (Button) findViewById(R.id.sharee);
        sharee.setOnClickListener(clickshare);
        Intent inn = getIntent();
        iid = inn.getStringExtra("Id");
        direct = inn.getStringExtra("Direction");
        datt = inn.getStringExtra("Date");
        tagg = inn.getStringExtra("Tag");
        orignumb = inn.getStringExtra("OrigNumber");
        newdt = inn.getStringExtra("Newdate");
        newtim = inn.getStringExtra("Newtime");
        pagecount = inn.getStringExtra("Pagecount");
        allbase = inn.getStringExtra("alldecode");
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        init();
    }


    private void init() {
        pdfView = (PDFView) findViewById(R.id.pdfvieww);
        position = getIntent().getIntExtra("position", -1);
          displayFromSdcard();
        decodedString = Base64.decode(allbase, Base64.DEFAULT);

        pdfView.fromBytes(decodedString).defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(PDFViewActivity.this)
                .enableAnnotationRendering(true)
                .onLoad(PDFViewActivity.this)
                .scrollHandle(new DefaultScrollHandle(PDFViewActivity.this))
                .load();

    }

    private void displayFromSdcard() {
        pdfFileName = "/sdcard/test.pdf";
        fileneww = new File(pdfFileName);
        Log.e("File path", fileneww.getAbsolutePath());


//        decodedString = Base64.decode(allbase, Base64.DEFAULT);
//
//        pdfView.fromBytes(decodedString).defaultPage(pageNumber)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .onPageChange(PDFViewActivity.this)
//                .enableAnnotationRendering(true)
//                .onLoad(PDFViewActivity.this)
//                .scrollHandle(new DefaultScrollHandle(PDFViewActivity.this))
//                .load();
//        pdfView.fromFile(fileneww)
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .load();
    }

    View.OnClickListener clickshare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                LayoutInflater li = LayoutInflater.from(PDFViewActivity.this);
                //Creating a view to get the dialog box
                final View dialog = li.inflate(R.layout.forwordasemail, null);
                final EditText txttonum, txtheadr, txtfilefor;
                final TextView txtfromnum;
                final Button btncancl, btnfrwrd;
                RecyclerView recyforword;
                ImageView addforword;

                final ArrayList<ResExpandList> reslist = new ArrayList<>();
                RecyclerView.LayoutManager layoutManager;
                RecyclerView.LayoutManager recivlaymanager;
                final SendFaxUserAdapter resAdapter;
                txtheadr = (EditText) dialog.findViewById(R.id.txtheadr);
                txtfilefor = (EditText) dialog.findViewById(R.id.txtfilefor);
                txttonum = (EditText) dialog.findViewById(R.id.txttonum);
                txtfromnum = (TextView) dialog.findViewById(R.id.txtfromnum);
                btncancl = (Button) dialog.findViewById(R.id.btncancl);
                btnfrwrd = (Button) dialog.findViewById(R.id.btnfrwrd);
                addforword = (ImageView) dialog.findViewById(R.id.addforword);
                recyforword = (RecyclerView) dialog.findViewById(R.id.recyforword);

                recyforword.setHasFixedSize(true);
                // The number of Columns
                layoutManager = new LinearLayoutManager(PDFViewActivity.this, LinearLayoutManager.VERTICAL, false);
                recyforword.setLayoutManager(layoutManager);
                resAdapter = new SendFaxUserAdapter(PDFViewActivity.this, reslist);
                recyforword.setAdapter(resAdapter);

                txtfilefor.setText(fileneww.getName() + "");
                txtfromnum.setText(orignumb);
                //Creating an alertdialog builder
                AlertDialog.Builder alert = new AlertDialog.Builder(PDFViewActivity.this);

                //Adding our dialog box to the view of alert dialog
                alert.setView(dialog);
                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                //Displaying the alert dialog
                alertDialog.show();
                btncancl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);
                        alertDialog.dismiss();

                    }
                });
                btnfrwrd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txttonum.getText().toString().equals("") || txttonum.getText().toString().equals(null)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);
                            alertDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "A destination faxnumber required", Toast.LENGTH_LONG).show();

                        } else {

                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(PDFViewActivity.this);
                            progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Config.LOGIN_AUTHENTICATE)
                                    .client(Config.okHttpClient)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            //Create Upload Server Client
                            LogEasyApi service = retrofit.create(LogEasyApi.class);

                            //File creating from selected URL

                            Log.e("filee innn-->", fileneww.getName() + "");
//        Map<String, ResExpandList> mapp = new LinkedHashMap<>();
//        String faxid = "Numbers";
//
//        for (int i = 0; i < resforsend.size(); i++) {
//            Log.e("mul numb innn-->", new Gson().toJson(resforsend.get(i)) + "");
//            mapp.put(faxid + i, resforsend.get(i));
//
//        }


                            // create RequestBody instance from file
                            RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
                            RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
                            RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");

                            RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);
                            RequestBody ujob = RequestBody.create(MediaType.parse("text/plain"), "Test Job");
                            RequestBody uheadr = RequestBody.create(MediaType.parse("text/plain"), txtfilefor.getText().toString());

                            RequestBody ubillcod = RequestBody.create(MediaType.parse("text/plain"), "Customer Code 1234");
                            RequestBody unumb = RequestBody.create(MediaType.parse("text/plain"), txttonum.getText().toString());

                            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), fileneww);

                            RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
                            RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
                            RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");
                            Uri uri = Uri.parse("file://" + fileneww.getAbsolutePath());

                            String PdfNameHolder = fileneww.getName();

                            String PdfPathHolder = FileUtils.getPath(getApplicationContext(), uri);

                            RequestBody Pdf = RequestBody.create(MediaType.parse("application/pdf"), PdfPathHolder);


                            // MultipartBody.Part is used to send also the actual file name
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("Files1", fileneww.getName(), Pdf);


                            Log.e("pdf holder  code-->", PdfPathHolder + "=====" + PdfNameHolder + "==uridata=" + uri);

//
//        RequestBody Pdf=RequestBody.create(MediaType.parse("multipart/form-data"),PdfPathHolder);

                            Call<ResultsetForLogin> resultCall = service.uploadImagesingle(uname, upass, cooki, uprodctkey, ujob, uheadr,
                                    ubillcod, unumb, body, ucsid, uani, ustartdt, uquality, ufeedmail);
//                ("MPatel", "Temp2018MPatel", "false",
//                "ef6978cf-3695-479c-a5b3-48589b8882f0", "Test Job", "Test Header", "Customer Code 1234",
//                "7324004069", body, "Station Id", "7324004069", "1/1/2018", "Fine", "chad@westfax.com");

                            // finally, execute the request
                            resultCall.enqueue(new Callback<ResultsetForLogin>() {
                                @Override
                                public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {

                                    progressDialog.dismiss();
                                    Log.e("respo  code-->", response.body().getStatusCode() + " ---" + response.body().getDattta());
                                    // Response Success or Fail
                                    if (response.isSuccessful()) {

                                        if (response.body().getStatusCode().equals("200")) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);

                                            alertDialog.dismiss();

                                            // textView.setText(response.body().getDattta());
//                        Snackbar.make(parentView, R.string.string_upload_success, Snackbar.LENGTH_LONG).show();
                                            Toast.makeText(getApplicationContext(), "Fax Sent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();
                                            //         Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.string_upload_fail, Toast.LENGTH_SHORT).show();

                                    }

                                    /**
                                     * Update Views
                                     */
//                textView.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                                    progressDialog.dismiss();
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);
                                    alertDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "error get", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


                break;
            case R.id.fab2:
                Uri uri = Uri.parse("file://" + fileneww.getAbsolutePath());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setType("application/pdf");
                startActivity(Intent.createChooser(share, "Share image File"));

//                Log.d("Raj", "Fab 2");
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

        } else {

            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

}