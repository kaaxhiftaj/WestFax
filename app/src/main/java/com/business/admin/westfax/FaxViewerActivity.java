package com.business.admin.westfax;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.ConnectivityReceiver;
import com.business.admin.westfax.Pojo.FileUtils;
import com.business.admin.westfax.Sticker.ImageEntity;
import com.business.admin.westfax.Sticker.Layer;
import com.business.admin.westfax.Sticker.MotionEntity;
import com.business.admin.westfax.Sticker.MotionView;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.fragment.AccountSettingFragment;
import com.business.admin.westfax.fragment.MyProfileFragment;
import com.business.admin.westfax.Pojo.UserSessionManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 27-01-2018.
 */

public class FaxViewerActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        OnPageChangeListener, OnLoadCompleteListener, View.OnTouchListener, View.OnDragListener, View.OnClickListener {
    SignaturePad signaturePad;
    Button saveButton, clearButton;
    String iid, direct, datt, tagg, orignumb, pagecount, newdt, newtim;
    ProgressDialog pDialog;
    PDFView pdfView;
    Integer pageNumber = 0;
    TextView txtpagfx, txttimefx, txtdattfx;
    FrameLayout rellav;
    //    StickerView stickerView;
    MotionView stickerView;
    public static final int DRAG = 1;
    public static final int NONE = 0;
    private static final String TAG = "Touch";
    public static final int ZOOM = 2;
    public static PointF mid = new PointF();
    public static int mode = 0;
    float d = 0.0F;
    Matrix savedMatrix = new Matrix();
    Matrix matrix = new Matrix();
    PointF start = new PointF();
    private float oldDist = 1f;
    private ScaleGestureDetector scaleGestureDetector;
    byte[] decodedString;
    String allbase;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    android.support.v7.app.ActionBarDrawerToggle toggle;
    UserSessionManager sessionManager;
    String unamee, upaasss, uprodid;
    Fragment fragment = null;
    File someFilePdf;
    String pdfFileName;
    int position = -1;
    float dX;
    float dY;
    int lastAction;
    //    ImageView imgsig;
    private ImageView imgsig;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    boolean boolean_save;
    private RelativeLayout.LayoutParams params;
    Bitmap bitmapnew;
    FloatingActionButton fab, fab1, fab2;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faxviewer);
        checkConnection();

        sessionManager = new UserSessionManager(FaxViewerActivity.this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);


        Intent ii = getIntent();
        iid = ii.getStringExtra("Id");
        direct = ii.getStringExtra("Direction");
        datt = ii.getStringExtra("Date");
        tagg = ii.getStringExtra("Tag");
        orignumb = ii.getStringExtra("OrigNumber");
        newdt = ii.getStringExtra("Newdate");
        newtim = ii.getStringExtra("Newtime");
        pagecount = ii.getStringExtra("Pagecount");

        Log.e("iiddd>>", iid + "");
        Log.e("directt>>", direct + "--newdt-" + newdt + "--newtim" + newtim);
        Log.e("datt>>", datt + "---pagcont---" + pagecount);
        Log.e("tagg>>", tagg + "--" + orignumb);

        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            /**
             * Called when drawer is closed
             */
            public void onDrawerClosed(View drawerView) {

                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                finish();
            }
        };
//        mDrawer.setDrawerListener(toggle);


//       mDrawer.setDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(orignumb + "");
        toggle.setDrawerIndicatorEnabled(false);

        txtpagfx = (TextView) findViewById(R.id.txtpagfx);
        txttimefx = (TextView) findViewById(R.id.txttimefx);
        txtdattfx = (TextView) findViewById(R.id.txtdattfx);

        pdfView = (PDFView) findViewById(R.id.pfgview);
        position = getIntent().getIntExtra("position", -1);

        imgsig = (ImageView) findViewById(R.id.imgsig);
//        imgsig.setOnTouchListener(touchh);
        txtpagfx.setText(pagecount + " pages");
        txttimefx.setText(newtim);
        txtdattfx.setText(newdt);
        rellav = (FrameLayout) findViewById(R.id.rellav);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        stickerView = (MotionView) findViewById(R.id.stickerView);

        fab = (FloatingActionButton) findViewById(R.id.faba);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1a);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2a);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

//Sticker
        //  String pdfff = "JVBERi0xLjINCg0KNCAwIG9iag0KPDwNCi9FIDExNjgyDQovSCBbIDk2MyAxMzkgXQ0KL0wgMTIwMTcNCi9MaW5lYXJpemVkIDENCi9OIDENCi9PIDcNCi9UIDExODg3DQo+PiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQplbmRvYmoNCg0KeHJlZg0KNCA2DQowMDAwMDAwMDEyIDAwMDAwIG4NCjAwMDAwMDA4NjkgMDAwMDAgbg0KMDAwMDAwMDk2MyAwMDAwMCBuDQowMDAwMDAxMTAzIDAwMDAwIG4NCjAwMDAwMDEzMDkgMDAwMDAgbg0KMDAwMDAwMTQxNiAwMDAwMCBuDQp0cmFpbGVyDQo8PA0KL0FCQ3BkZiA2MTA3DQovSUQgWyA8ODc1NzM3Q0Y2MUQ2Q0U4RjQ4RjJGMTMwNzk0NzI2RjQ+DQo8ODI2QTRFRjMyMDQ3QUM2RkYzQTk2MUI5QkVDQTA2MEM";


//        //Convert binary image file to byte array to base64 encoded string
//        FileInputStream mFileInputStream = null;
//        try {
//            mFileInputStream = new FileInputStream("data/inputImage.png");
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int bytesRead = 0;
//            while ((bytesRead = mFileInputStream.read(b)) != -1) {
//                bos.write(b, 0, bytesRead);
//            }
////            byte[] ba = bos.toByteArray();
////            pdfff = Base64.encodeBytes(ba);
////
////            //Convert String data to binary image file
////            Base64.decodeToFile(encoded, "data/outputImage.png");
////
////
////            //Convert base64 encoded String data file to binary image file
////            Base64.decodeFileToFile("data/encodedImage.txt","data/outputImage.png");
//
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        getSenderList();
        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);


        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FaxViewerActivity.this, "Signature Saved", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message = null;
        int color = 0;
        if (isConnected) {

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


    public void getSenderList() {
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage("Loading");
//        pDialog.show();

        String stcookie = Config.cokkiee;
        String fileformat = Config.fileformat;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGIN_AUTHENTICATE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject jsonobject_one = new JSONObject();
        try {

            jsonobject_one.put("Id", iid);
            jsonobject_one.put("Direction", direct);
            jsonobject_one.put("Date", datt);
            jsonobject_one.put("Tag", tagg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);

        Call<Dataforall> retCall = easyApi.GetDocPdf(unamee, upaasss, stcookie, uprodid, jsonobject_one, fileformat);

        retCall.enqueue(new Callback<Dataforall>() {
            @Override
            public void onResponse(Call<Dataforall> call, final Response<Dataforall> response) {
                Log.e("conggp--->", response.body().getMessage() + "--------" + response.body().getStatusCode());

                pDialog.dismiss();

                if (response.body().getMessage().equals("OK")) {
                    List<Datum> jsonArray = response.body().getData();
                    Datum itemflt = new Datum();

                    for (int i = 0; i < jsonArray.size(); i++) {

                        String idd = response.body().getData().get(i).getId();
                        String direct = response.body().getData().get(i).getDirection();
                        String dateee = response.body().getData().get(i).getDate();

                        if (jsonArray.get(i).getStatus().equals("Ok")) {

                            List<FaxCallInfoList> Orignumb = new ArrayList<FaxCallInfoList>();
                            FaxCallInfoList mLog = new FaxCallInfoList();


                            mLog.setmFileContents(response.body().getData().get(i).getmFaxfiles().get(i).getmFileContents() + "");
                            Orignumb.add(mLog);


                            itemflt.setId(idd);
                            itemflt.setDirection(direct);
                            itemflt.setDate(dateee);
                            itemflt.setmFaxfiles(Orignumb);

                            decodedString = Base64.decode(response.body().getData().get(i).getmFaxfiles().get(i).getmFileContents(), Base64.DEFAULT);
                            allbase = response.body().getData().get(i).getmFaxfiles().get(i).getmFileContents();

                            File dir = Environment.getExternalStorageDirectory();
                            String filename = "Westfax.pdf";
                            someFilePdf = new File(dir, filename);
                            try {
                                someFilePdf.createNewFile();
                                OutputStream outStream = new FileOutputStream(someFilePdf);
                                outStream.write(decodedString);
                                outStream.flush();
                                outStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String imagePath = null;
                            try {
                                imagePath = new String(decodedString, "UTF-8");
                                Log.e("decode stringg---???", decodedString + "");
//            decodedString = Base64.decode(base64.toString(), Base64.DEFAULT);

                                pdfView.fromBytes(decodedString).defaultPage(pageNumber)
                                        .enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .onPageChange(FaxViewerActivity.this)
                                        .enableAnnotationRendering(true)
                                        .onLoad(FaxViewerActivity.this)
                                        .scrollHandle(new DefaultScrollHandle(FaxViewerActivity.this))
                                        .load();


//                                someFilePdf = new File("Fax1.pdf");
//                                FileOutputStream fos = null;
//                                try {
//                                    fos = new FileOutputStream(someFilePdf);
//                                    fos.write(decodedString);
//                                    fos.flush();
//                                    fos.close();
//                                    Log.e("fille",fos+"");
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                Log.e("fille ff",fos+"");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(FaxViewerActivity.this);
                            builder1.setMessage("File Not Found.");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            if (!FaxViewerActivity.this.isFinishing()) {
                                alert11.show();

                            }

//                            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_LONG).show();

                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Dataforall> call, Throwable t) {
                Log.e("faillier----->", call + "");

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }


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

            Log.e("hiiiiiiiii pddf----->", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
        //    onBackPressed();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ediit) {
            Log.e("decc====", decodedString + "");


            //below is the different part


//            bitmapnew = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//
            Intent i = new Intent(FaxViewerActivity.this, CaptureSignature.class);
            startActivityForResult(i, 0);
        }
//        else if (id == R.id.action_forword) {
//
////            imgsig.setVisibility(View.VISIBLE);
////            pdfView.setDrawingCacheEnabled(true);
////
////            pdfView.buildDrawingCache();
////
////
////            rellav.setDrawingCacheEnabled(true);
////
////            rellav.buildDrawingCache();
////
////            Bitmap bitmap =rellav.getDrawingCache();
////
////            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////            bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
//
//
//            Log.e("muyy --->", "------------" + decodedString);
//            String sttr = Base64.encodeToString(decodedString, Base64.DEFAULT);
//
//            byte[] decString = Base64.decode(sttr, Base64.DEFAULT);
//
////            decodedString + "--bitmp-"+bitmap+"--byttarray---"+
//            Intent inn = new Intent(FaxViewerActivity.this, SendFaxForwardActivity.class);
//            inn.putExtra("Id", iid);
//            inn.putExtra("Direction", direct);
//            inn.putExtra("Date", datt);
//            inn.putExtra("Tag", tagg);
//            inn.putExtra("OrigNumber", orignumb);
//            inn.putExtra("Newdate", newdt);
//            inn.putExtra("Newtime", newtim);
//            inn.putExtra("Pagecount", pagecount);
//            inn.putExtra("vallstr", sttr);
//            inn.putExtra("bytedecodestr", decString);
//            startActivity(inn);
//            finish();
//
////            LayoutInflater li = LayoutInflater.from(FaxViewerActivity.this);
////            //Creating a view to get the dialog box
////            final View dialog = li.inflate(R.layout.forwordasemail, null);
////            final EditText txttonum;
////            final TextView txtfromnum;
////            final Button btncancl, btnfrwrd;
////            RecyclerView recyforword;
////            ImageView addforword;
////
////            final ArrayList<ResExpandList> reslist = new ArrayList<>();
////            RecyclerView.LayoutManager layoutManager;
////            RecyclerView.LayoutManager recivlaymanager;
////            final SendFaxUserAdapter resAdapter;
////
////            txttonum = (EditText) dialog.findViewById(R.id.txttonum);
////            txtfromnum = (TextView) dialog.findViewById(R.id.txtfromnum);
////            btncancl = (Button) dialog.findViewById(R.id.btncancl);
////            btnfrwrd = (Button) dialog.findViewById(R.id.btnfrwrd);
////            addforword = (ImageView) dialog.findViewById(R.id.addforword);
////            recyforword = (RecyclerView) dialog.findViewById(R.id.recyforword);
////
////            recyforword.setHasFixedSize(true);
////            // The number of Columns
////            layoutManager = new LinearLayoutManager(FaxViewerActivity.this, LinearLayoutManager.VERTICAL, false);
////            recyforword.setLayoutManager(layoutManager);
////            resAdapter = new SendFaxUserAdapter(FaxViewerActivity.this, reslist);
////            recyforword.setAdapter(resAdapter);
////
////
////            txtfromnum.setText(orignumb);
////            //Creating an alertdialog builder
////            AlertDialog.Builder alert = new AlertDialog.Builder(FaxViewerActivity.this);
////
////            //Adding our dialog box to the view of alert dialog
////            alert.setView(dialog);
////            alert.setCancelable(false);
////            //Creating an alert dialog
////            final AlertDialog alertDialog = alert.create();
////            //Displaying the alert dialog
////            alertDialog.show();
////            btnfrwrd.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    if (txttonum.getText().toString().equals("") || txttonum.getText().toString().equals(null)) {
////                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                        imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);
////                        alertDialog.dismiss();
////                        Toast.makeText(getApplicationContext(), "A destination faxnumber required", Toast.LENGTH_LONG).show();
////
////                    } else {
////
////                        pdfView.setDrawingCacheEnabled(true);
////
////                        pdfView.buildDrawingCache();
////
////                        Bitmap bm = pdfView.getDrawingCache();
////                        Intent iin = new Intent(FaxViewerActivity.this, SendFaxForwardActivity.class);
////                        iin.putExtra("bmpdf", bm);
////                        startActivity(iin);
////                        finish();
////
////                        Retrofit retrofit = new Retrofit.Builder()
////                                .baseUrl(Config.LOGIN_AUTHENTICATE)
////                                .addConverterFactory(GsonConverterFactory.create())
////                                .build();
////
////                        JSONObject jsonobject_one = new JSONObject();
////                        try {
////
////                            jsonobject_one.put("Id", iid);
////                            jsonobject_one.put("Direction", direct);
////                            jsonobject_one.put("Date", datt);
////                            jsonobject_one.put("Tag", tagg);
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                        String stcookie = Config.cokkiee;
////                        String tofaxmail = txttonum.getText().toString();
////                        LogEasyApi easyApi = retrofit.create(LogEasyApi.class);
////
////                        Call<ResultsetForLogin> retCall = easyApi.SendFaxasEmail(unamee, upaasss, stcookie, uprodid, jsonobject_one, tofaxmail);
//////                ("MPatel", "Temp2018MPatel", "false", "ef6978cf-3695-479c-a5b3-48589b8882f0");
////                        Log.e("rescall----->", retCall + "");
////
////                        retCall.enqueue(new Callback<ResultsetForLogin>() {
////                            @Override
////                            public void onResponse(Call<ResultsetForLogin> call, final Response<ResultsetForLogin> response) {
////                                Log.e("get conggp----->", response.body().getMessag() + "--------" + response.body().getStatusCode());
////
////                                pDialog.dismiss();
////
////                                if (response.body().getDattta().equals("true")) {
////
////                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                                    imm.hideSoftInputFromWindow(btnfrwrd.getWindowToken(), 0);
////                                    alertDialog.dismiss();
////                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
////
////                                }
////                            }
////
////                            @Override
////                            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
////                                Log.e("faillier----->", call + "");
////
////                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
////
////                            }
////                        });
////
////
////                    }
////
////                }
////            });
////
////            btncancl.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.hideSoftInputFromWindow(btncancl.getWindowToken(), 0);
////                    alertDialog.dismiss();
////                }
////            });
//
//
//        }


//        else if (id == R.id.action_sett) {
//            Log.e("decodee strr", allbase + "");
////            if (boolean_save) {
////
//            Intent inn = new Intent(getApplicationContext(), PDFViewActivity.class);
//            inn.putExtra("Id", iid);
//            inn.putExtra("Direction", direct);
//            inn.putExtra("Date", datt);
//            inn.putExtra("Tag", tagg);
//            inn.putExtra("OrigNumber", orignumb);
//            inn.putExtra("Newdate", newdt);
//            inn.putExtra("Newtime", newtim);
//            inn.putExtra("Pagecount", pagecount);
//            inn.putExtra("alldecode", allbase);
//            startActivity(inn);
//
////            } else {
////
////                progressDialog = new ProgressDialog(FaxViewerActivity.this);
////                progressDialog.setMessage("Please wait");
////                bitmap = loadBitmapFromView(rellav, rellav.getWidth(), rellav.getHeight());
////                createPdf();
//////                        saveBitmap(bitmap);
////            }
////
////            createPdf();
////            try {
////
////
//////                FileOutputStream fos=new FileOutputStream(photo.getPath());
//////
//////                fos.write(jpeg[0]);
//////                fos.close();
////                OutputStream out = new FileOutputStream("out.pdf");
////                out.write(decodedString);
////                out.close();
//////                ObjectInputStream ois = new ObjectInputStream(bis);
//////                File fileFromBytes = (File) ois.readObject();
//////                bis.close();
//////                ois.close();
////                Log.e("fileee-->", out + "");
//////
//////                System.out.println(fileFromBytes);
////
////
//////
//////            File outputFile = new File(Environment.getExternalStoragePublicDirectory
//////                    (Environment.DIRECTORY_DOWNLOADS), "ref Number from Quotation.pdf");
//////                Uri uri = Uri.fromFile(fileFromBytes);
//////
//////                Intent share = new Intent();
//////                share.setAction(Intent.ACTION_SEND);
//////                share.setType("application/pdf");
//////                share.putExtra(Intent.EXTRA_STREAM, uri);
////////                share.setPackage("com.whatsapp");
//////                startActivity(share);
////
////            } catch (IOException e) {
////                Log.e("fileee-->", "not workinnggg");
////                e.printStackTrace();
////            }
////            catch (ClassNotFoundException e) {
////                e.printStackTrace();
////            }
////            catch (FileNotFoundException e) {
////
////                System.out.println("File not found" + e);
////
////            }
//
//
////            Log.e("hiii","------>");
////            String title = getString(R.string.app_name);
////            FragmentManager fragmentManager = getSupportFragmentManager();
////            switch (id) {
////                case R.id.edt_myproff:
////                    fragment = new MyProfileFragment();
////
////                    fragmentManager.beginTransaction()
////                            .replace(R.id.mfff, fragment)
////                            .commit();
////                    title = "My Profile";
////                    return true;
////                case R.id.edt_faxset:
////                    fragment = new AccountSettingFragment();
////                    fragmentManager.beginTransaction()
////                            .replace(R.id.mfff, fragment)
////                            .commit();
////                    title = "Fax Settings";
////                    return true;
////                case R.id.edt_logout:
////                    title = "Logout";
////                    logout();
////                    return true;
////                case R.id.edt_faq:
////                    Intent viewIntent =
////                            new Intent("android.intent.action.VIEW",
////                                    Uri.parse("http://westfax.com/"));
////                    startActivity(viewIntent);
////                    return true;
////
////                case R.id.edt_exit:
////                    finish();
////            }
//        }
        else {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.faba:

                animateFAB();
                break;
            case R.id.fab1a:

                LayoutInflater li = LayoutInflater.from(FaxViewerActivity.this);
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
                layoutManager = new LinearLayoutManager(FaxViewerActivity.this, LinearLayoutManager.VERTICAL, false);
                recyforword.setLayoutManager(layoutManager);
                resAdapter = new SendFaxUserAdapter(FaxViewerActivity.this, reslist);
                recyforword.setAdapter(resAdapter);

                txtfilefor.setText(someFilePdf.getName() + "");
                txtfromnum.setText(orignumb);
                //Creating an alertdialog builder
                AlertDialog.Builder alert = new AlertDialog.Builder(FaxViewerActivity.this);

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
                            progressDialog = new ProgressDialog(FaxViewerActivity.this);
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

                            Log.e("filee innn-->", someFilePdf.getName() + "");
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

//                            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), fileneww);

                            RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
                            RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
                            RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");
                            Uri uri = Uri.parse("file://" + someFilePdf.getAbsolutePath());

                            String PdfNameHolder = someFilePdf.getName();

                            String PdfPathHolder = FileUtils.getPath(getApplicationContext(), uri);

                            RequestBody Pdf = RequestBody.create(MediaType.parse("application/pdf"), someFilePdf);


                            // MultipartBody.Part is used to send also the actual file name
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("Files1", someFilePdf.getName(), Pdf);


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
            case R.id.fab2a:
                Uri uri = Uri.parse("file://" + someFilePdf.getAbsolutePath());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setType("application/pdf");
                startActivity(Intent.createChooser(share, "Share image File"));

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

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }


//    public void onsetFaxClick(MenuItem item) {
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        String title = getString(R.string.app_name);
//        // If red color selected
//        if (item.getItemId() == R.id.edt_myproff) {
//            // Checked the red color item
//            item.setChecked(true);
//            // Set the text view text color to red
//            fragment = new MyProfileFragment();
//
//            fragmentManager.beginTransaction()
//                    .replace(R.id.mfff, fragment)
//                    .commit();
//            title = "My Profile";
//
//        } else if (item.getItemId() == R.id.edt_faxset) {
//            // Checked the green color item
//            item.setChecked(true);
//            fragment = new AccountSettingFragment();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.mfff, fragment)
//                    .commit();
//            title = "Fax Settings";
//        } else if (item.getItemId() == R.id.edt_logout) {
//            // Checked the blue color item
//            item.setChecked(true);
//            logout();
//        } else if (item.getItemId() == R.id.edt_faq) {
//            item.setChecked(true);
//            Intent viewIntent =
//                    new Intent("android.intent.action.VIEW",
//                            Uri.parse("http://westfax.com/"));
//            startActivity(viewIntent);
//            // Do nothing
//        } else if (item.getItemId() == R.id.edt_exit) {
//            item.setChecked(true);
//            finish();
//            // Do nothing
//        }
//
//
//    }

    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FaxViewerActivity.this);

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
    public boolean onDrag(View v, DragEvent event) {

        View view = (View) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                int x = (int) event.getX();
                int y = (int) event.getY();

                break;

            case DragEvent.ACTION_DRAG_EXITED:

                break;

            case DragEvent.ACTION_DRAG_LOCATION:
                x = (int) event.getX();
                y = (int) event.getY();
                break;

            case DragEvent.ACTION_DRAG_ENDED:

                break;

            case DragEvent.ACTION_DROP:

                int childCountDropped = rellav.getChildCount();
                System.out.println("Child Count of Views::::::Dropped-->" + childCountDropped);

                x = (int) event.getX();
                y = (int) event.getY();
                params.leftMargin = x;
                params.topMargin = y;

                view.setLayoutParams(params);
                view.setVisibility(View.VISIBLE);

                break;
            default:
                break;
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            imgsig.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    //     Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
                    break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editfax, menu);
        //  MenuItem register = menu.findItem(R.id.action_ediit);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 1) {
            final Bitmap bimp = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);
            imgsig.setVisibility(View.VISIBLE);
            imgsig.setImageBitmap(bimp);
            imgsig.setOnTouchListener(this);
            imgsig.setOnDragListener(this);

//            ImageView imgview = new ImageView(FaxViewerActivity.this);
//            imgview.setImageBitmap(b);
            Drawable drawable = new BitmapDrawable(getResources(), imgsig.getDrawingCache());


//            stickerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    Layer layer = new Layer();
//
//                    ImageEntity entity = new ImageEntity(layer, b, stickerView.getWidth(), stickerView.getHeight());
//
//                    stickerView.addEntityAndPosition(entity);
//                }
//            });

        }
    }

}
