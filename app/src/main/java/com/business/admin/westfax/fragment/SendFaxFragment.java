package com.business.admin.westfax.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.FileUtils;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.SendFaxImageAdapter;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.permission.PermissionsActivity;
import com.business.admin.westfax.permission.PermissionsChecker;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

import static android.app.Activity.RESULT_OK;

public class SendFaxFragment extends Fragment {
    RecyclerView recsendnum, recycleforsend;
    ImageView imgaddcon, imgshow;
    EditText edtaddfile, edtaddsendnum, edit_header;
    RecyclerView.LayoutManager layoutManager, linforsend;
    SendFaxImageAdapter resAdapter;

    SendFaxUserAdapter resUserAdapter;
    ArrayList<ResExpandList> reslist;
    ArrayList<ResExpandList> resforsend;
    String PdfNameHolder, PdfPathHolder, PdfID;

    Button btnsendfx;
    String imagePath;
    ImageView btnchoos;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;

    PermissionsChecker checker;
    TextView txtpath;
    ArrayList<Uri> urislist = new ArrayList<>();
    String unamee, upaasss, uprodid;
    Uri uridata;
    UserSessionManager sessionManager;

    public SendFaxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_send_fax, container, false);
        mContext = getContext();

        sessionManager = new UserSessionManager(getContext());

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        unamee = user.get(UserSessionManager.KEY_EMAIL);

        // pass
        upaasss = user.get(UserSessionManager.KEY_NAME);
        uprodid = user.get(UserSessionManager.KEY_PROID);

        checker = new PermissionsChecker(getContext());

        reslist = new ArrayList<>();
        resforsend = new ArrayList<>();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Send Fax");
        recsendnum = (RecyclerView) rootview.findViewById(R.id.recsendfile);
        imgaddcon = (ImageView) rootview.findViewById(R.id.imgaddcon);
        edtaddfile = (EditText) rootview.findViewById(R.id.edtaddcon);
        btnchoos = (ImageView) rootview.findViewById(R.id.btnchoos);
        btnsendfx = (Button) rootview.findViewById(R.id.btnsendfx);
        //  txtpath = (TextView) rootview.findViewById(R.id.txtpath);
        edit_header = (EditText) rootview.findViewById(R.id.edit_header);
        edtaddsendnum = (EditText) rootview.findViewById(R.id.edtaddsendnum);
        recycleforsend = (RecyclerView) rootview.findViewById(R.id.recsendnum);
        imgshow = (ImageView) rootview.findViewById(R.id.imgshow);
        recsendnum.setHasFixedSize(true);
        // The number of Columns
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recsendnum.setLayoutManager(layoutManager);
        resAdapter = new SendFaxImageAdapter(getContext(), reslist);
        recsendnum.setAdapter(resAdapter);


        recycleforsend.setHasFixedSize(true);
        // The number of Columns
        linforsend = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleforsend.setLayoutManager(linforsend);
        resUserAdapter = new SendFaxUserAdapter(getContext(), resforsend);
        recycleforsend.setAdapter(resUserAdapter);


        imgaddcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Specify the position
//                showImagePopup(view);
                String title = edtaddsendnum.getText().toString();
                if (title.matches("")) {
                    Toast.makeText(view.getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResExpandList mLog = new ResExpandList();
                mLog.setString2Value(title);
                resforsend.add(mLog);
                resUserAdapter.notifyData(resforsend);
                edtaddsendnum.setText("");
            }
        });
        btnchoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/brouchure.pdf");

                showImagePopup(view);
//                File file = new File(Environment.getExternalStorageDirectory(),
//                        "Report.pdf");
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    startActivity(pdfOpenintent);
//                } catch (ActivityNotFoundException e) {
//
//                }
//openFile();
            }
        });

        btnsendfx.setOnClickListener(buttnsend);

        return rootview;
    }

    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.

            String[] mimeTypes =
                    {"image/*", "application/pdf"};

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
            startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1010);

//            final Intent galleryIntent = new Intent();
////            galleryIntent.setType("application/pdf");
//            galleryIntent.setType("image/*");
//            galleryIntent.setAction(Intent.ACTION_PICK);
//
//            // Chooser of file system options.
//            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
//            startActivityForResult(chooserIntent, 1010);
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(getActivity(), 0, permission);
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
//                 Snackbar.make(parentView, R.string.string_unable_to_pick_image, Snackbar.LENGTH_INDEFINITE).show();

////                Toast.makeText(getContext(), R.string.string_unable_to_pick_image, Toast.LENGTH_SHORT).show();
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                String mimeType = null;
                Uri uri = data.getData();
                String path = getPathFromURI(uri);
                File ff = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), path);
                String mCurrentPhotoPath = ff.getAbsolutePath();

                Log.e("U N ME-->", "" + uri + "===" + mCurrentPhotoPath);
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File filenew = new File(imageUri.getPath());
//                getActivity().getContentResolver().takePersistableUriPermission(uri,
//                        Intent.FLAG_GRANT_READ_URI_PERMISSION);

                uridata = FileProvider.getUriForFile(getContext(),
                        "com.business.admin.westfax" + ".fileprovider", filenew);
                data.putExtra(MediaStore.EXTRA_OUTPUT, uridata);
//
//                    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
//                        ContentResolver cr = getActivity().getContentResolver();
//                        mimeType = cr.getType(uri);
//                    } else {
//                        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
//                                .toString());
//                        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//                                fileExtension.toLowerCase());
//                    }
//
//                    Log.e("go naugat data-->", mimeType + "--"+uri);
//
//                    File f = new File(data+"");
//
//                    uridata = FileProvider.getUriForFile(getContext(),getActivity().getPackageName() +
//                            ".provider", f);
//
////                // Add in case of if We get Uri from fileProvider.

                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                getActivity().getContentResolver().takePersistableUriPermission(uridata, takeFlags);

//                data.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    data.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    data.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {

                Log.e("go data-->", data + "----" + uridata);
                uridata = data.getData();

//                    imageUri = getPickImageResultUri(data);

            }

            Log.e("On activity Uriiiii-->", uridata + "");
//
            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;

            if (!requirePermissions) {

                Log.e("Onact ntprm imguri-->", uridata + "");
//                imgg.setImageUriAsync(imageUri);


                String uriString = uridata.toString();


                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String[] filepathh = {MediaStore.Images.Media.DATA};
                Log.e("strpathh cong name--->", filepathh + "");
//[Ljava.lang.String;@bdf6197
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {


                        cursor = getActivity().getContentResolver().query(uridata, filepathh, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {

                            imagePath = cursor.getString(cursor.getColumnIndex(filepathh[0]));
//                                OpenableColumns.DISPLAY_NAME));
                            Picasso.with(getContext()).load(new File(imagePath))
                                    .into(imgshow);

                            // ImageView.setImageURI(Uri.parse(new File("/sdcard/cats.jpg").toString()));

                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    imagePath = myFile.getName();
                }
                Log.e("dis  cong namee--->", imagePath + "");
                final File file = new File(imagePath);
                edtaddfile.setText(file.getName());
                String title = edtaddfile.getText().toString();

                if (title.matches("")) {
                    Toast.makeText(getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                PdfNameHolder = file.getName();
                PdfPathHolder = FileUtils.getPath(getContext(), uridata);

//                actionlist.add(PdfPathHolder);
                ResExpandList mLog = new ResExpandList();
                mLog.setString1Name(title);
                reslist.add(mLog);
                resAdapter.notifyData(reslist);

                edtaddfile.setText("");
                Uri uriii = Uri.parse(mLog.getString1Name());
                Log.e("lastt uriii--->", uriii + "");
                urislist.add(uriii);
                resAdapter.notifyData(reslist);

                resAdapter.notifyDataForUri(urislist);


            }
        }
    }

    public MultipartBody.Part prePareFilepart(String ss, Uri fileuri) {
//          File file = FileUtils.getFile(getContext(), fileuri);

        final File file = new File(imagePath);


        RequestBody requestBody = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileuri)), file);
//                "multipart/form-data"), file);
        Log.e("fprepare -->", fileuri + file.getName() + "====" + requestBody);

        return MultipartBody.Part.createFormData(ss, file.getName(), requestBody);

    }

    View.OnClickListener buttnsend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!TextUtils.isEmpty(imagePath)) {
//                    uploadImage();
                uploadImage(urislist);

                Log.e("button", "onClick:" + urislist);

            } else {
                Toast.makeText(getContext(), R.string.string_message_to_attach_file, Toast.LENGTH_SHORT).show();

            }
        }

    };

    /**
     * Upload Image Client Code
     */
    private void uploadImage(List<Uri> uriliss) {

        /**
         * Progressbar to Display if you need
         */
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
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
        final File file = new File(imagePath);
        Log.e("filee innn-->", file.getName() + "");
//        Map<String, ResExpandList> mapp = new LinkedHashMap<>();
//        String faxid = "Numbers";
//
//        for (int i = 0; i < resforsend.size(); i++) {
//            Log.e("mul numb innn-->", new Gson().toJson(resforsend.get(i)) + "");
//            mapp.put(faxid + i, resforsend.get(i));
//
//        }
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

        //  Log.e("filee innn-->", file.getName() + "");
        List<MultipartBody.Part> parts = new ArrayList<>();
//
//        for (int ii = 0; ii < uriliss.size(); ii++) {
//            parts.add(prePareFilepart("Files" + (ii + 1), uriliss.get(ii)));
//
//
//            Log.e("partss-->", new Gson().toJson(parts.get(ii)) + "");
//
//        }
        PdfNameHolder = file.getName();

        PdfPathHolder = FileUtils.getPath(getContext(), uridata);
        Log.e("pdf holder  code-->", PdfPathHolder + "=====" + PdfNameHolder + "==uridata=" + uridata);

//        filee innn-->: IMG_20180224_211432.jpg
//        My IDDD-->: 7324004069
//        fprepare -->: IMG_20180224_211432.jpg====okhttp3.RequestBody$3@636f821
//        partss-->: {"headers":{"namesAndValues":["Content-Disposition","form-data; name\u003d\"Files1\"; filename\u003d\"IMG_20180224_211432.jpg\""]}}
//        button: onClick:[IMG_20180224_211432.jpg]


        // create RequestBody instance from file
        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");

        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);
        RequestBody ujob = RequestBody.create(MediaType.parse("text/plain"), "Test Job");
        RequestBody uheadr = RequestBody.create(MediaType.parse("text/plain"), edit_header.getText().toString());

        RequestBody ubillcod = RequestBody.create(MediaType.parse("text/plain"), "Customer Code 1234");
        //   RequestBody unumb = RequestBody.create(MediaType.parse("text/plain"), "7324004069");

        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);

        RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
        RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
        RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");

        RequestBody Pdf = RequestBody.create(MediaType.parse("application/pdf"), PdfPathHolder);


        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("Files1", file.getName(), Pdf);


//
//        RequestBody Pdf=RequestBody.create(MediaType.parse("multipart/form-data"),PdfPathHolder);
//========================================================================================================= mapp twoo
//        Call<ResultsetForLogin> resultCall = service.uploadImage(uname, upass, cooki, uprodctkey, ujob, uheadr,
//                ubillcod, mapp, body, ucsid, uani, ustartdt, uquality, ufeedmail);


        Call<ResultsetForLogin> resultCall = service.uploadImage(uname, upass, cooki, uprodctkey, ujob, uheadr,
                ubillcod, mapp, parts, ucsid, uani, ustartdt, uquality, ufeedmail);
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

                        // textView.setText(response.body().getDattta());
//                        Snackbar.make(parentView, R.string.string_upload_success, Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Fax Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                        //         Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), R.string.string_upload_fail, Toast.LENGTH_SHORT).show();

                }

                /**
                 * Update Views
                 */
                imagePath = "";
//                textView.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "error get", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");


            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            }
            //           else if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
//                // Word document
//                intent.setDataAndType(uri, "application/msword");
//            }
//            else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
//                // Powerpoint file
//                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
//                // Excel file
//                intent.setDataAndType(uri, "application/vnd.ms-excel");
//            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
//                // WAV audio file
//                intent.setDataAndType(uri, "application/x-wav");
//            } else if (url.toString().contains(".rtf")) {
//                // RTF file
//                intent.setDataAndType(uri, "application/rtf");
//            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
//                // WAV audio file
//                intent.setDataAndType(uri, "audio/x-wav");
//            } else if (url.toString().contains(".gif")) {
//                // GIF file
//                intent.setDataAndType(uri, "image/gif");
//            }

//            else if (url.toString().contains(".txt")) {
//                // Text file
//                intent.setDataAndType(uri, "text/plain");
//            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
//                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//                // Video files
//                intent.setDataAndType(uri, "video/*");
//            } else {
//                intent.setDataAndType(uri, "*/*");
//            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }
}
