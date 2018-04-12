package com.business.admin.westfax.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import com.business.admin.westfax.Pojo.FilenameUtils;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.R;
import com.business.admin.westfax.adapter.SendFaxImageAdapter;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.permission.PermissionsChecker;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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

/**
 * Created by SONY on 14-03-2018.
 */

public class FileFaxSendFragment extends Fragment {

    int FILE_SELECT_CODE = 0, FILE_SIZE_LIMIT = 25, filesize = 1;
    String filename = "ABC";
    private static int SPLASH_TIME_OUT = 1900;
    Intent intent;
    private ArrayList<String> filePaths;
    ArrayList<String> selectedPaths;
    Bitmap bmp;
    private ImageView imgg;

    RecyclerView recsendnum, recycleforsend;
    ImageView imgaddcon, imgshow;
    EditText edtaddfile, edtaddsendnum, edit_header;
    RecyclerView.LayoutManager layoutManager, linforsend;
    SendFaxImageAdapter resAdapter;

    SendFaxUserAdapter resUserAdapter;
    ArrayList<ResExpandList> reslist;
    ArrayList<ResExpandList> resforsend;
    String PdfNameHolder, PdfPathHolder, PdfID;
    String mCurrentPhotoPath;
    Button btnsendfx;
    String imagePath;
    ImageView btnchoos;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;

    PermissionsChecker checker;
    TextView txtpath;
    ArrayList<Uri> urislist = new ArrayList<>();
    String unamee, upaasss, uprodid;

    UserSessionManager sessionManager;
    ArrayList<String> actionlist;
    Intent chooserIntent;
    Uri outputFileUri;
    public static final int RequestPermissionCode = 5;
    Uri fille;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        View rootview = inflater.inflate(R.layout.fragment_send_fax, container, false);

        EnableRuntimePermissionToAccessCamera();

        actionlist = new ArrayList<>();
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
        // ((AppCompatActivity) ).getSupportActionBar().setTitle("Send Fax");
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


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
//                browseClick();


                selectImage();
//takePicture();
//                startActivityForResult(getPickImageChooserIntent(), 210);
            }
        });

        btnsendfx.setOnClickListener(buttnsend);


        return rootview;
    }

    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Files", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Attach File!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    takePicture();

                } else if (options[item].equals("Choose from Files"))

                {
                    showImagePopup();

//                    startActivityForResult(getPickImageChooserIntent(), 210);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public void EnableRuntimePermissionToAccessCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    View.OnClickListener buttnsend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("action size", actionlist.size() + "----" + resforsend.size());

            if (resforsend.size() <= 0) {

                Toast.makeText(getContext(), "Enter Any Recipients", Toast.LENGTH_SHORT).show();

            } else if (actionlist.size() <= 0) {
                Toast.makeText(getContext(), "Choose Any File", Toast.LENGTH_SHORT).show();

            } else if (edit_header.getText().toString().length() <= 0) {
                Toast.makeText(getContext(), "Please Add Header", Toast.LENGTH_SHORT).show();
            } else {

                uploadImage();

//                uploadImage(urislist);
                Log.e("button", "onClick:" + urislist);


            }
        }

    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 210) {
            if (resultCode == RESULT_OK) {
                try {
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();

                        Log.e("file uriii-->", uri + "");
                        String mimeType = getActivity().getContentResolver().getType(uri);
                        Log.e("mimee type-->", mimeType + "");

                        if (mimeType == null) {
                            String path = getPath(getContext(), uri);
                            Log.e("string path-->", path + "");

                            if (path == null) {
                                filename = FilenameUtils.getName(uri.toString());
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            Uri returnUri = data.getData();

                            Log.e("string cursontr-->", returnUri + "");

                            Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }

//                        File fileSave = getActivity().getExternalFilesDir(null);
                        String sourcePath = getActivity().getExternalFilesDir(null).toString();
                        Log.e("Acttt filee name-->", filename + "--\n sorc " + sourcePath);

                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), uri, getContext());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Uri fille = Uri.fromFile(getOutputMediaFile());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fille);

                        startActivityForResult(intent, 210);
//
//
//                        String timeStamp =
//                                new SimpleDateFormat("yyyyMMdd_HHmmss",
//                                        Locale.getDefault()).format(new Date());
//                        String imageFileName = "IMG_" + timeStamp + "_";
////                       File f = new File(getContext().getCacheDir(), imageFileName);
//                        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                        File image = File.createTempFile(
//                                imageFileName,  /* prefix */
//                                ".jpg",         /* suffix */
//                                storageDir     /* directory */
//                        );
//
//                        String imageFilePath = image.getAbsolutePath();
//
//
//                        Uri photoURI = Uri.fromFile(image);
////                                FileProvider.getUriForFile(getContext(), "com.business.admin.westfax.fileprovider", createImageFile());
////grant uri with essential permission the first arg is the The packagename you would like to allow to access the Uri.
////                        getContext().grantUriPermission("com.android.camera", photoURI,
////                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
////
//
////                    Bitmap    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
//
//                        // adding captured image in imageview.
//
//                        Uri fileuri = Uri.fromFile(getOutputMediaFile());
                        Log.e("immmg path-->", fille + "--");
//                        data.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
//
                        imgshow.setImageURI(fille);
//
////                        Picasso.with(getContext()).load(new File(photoURI.getPath())).into(imgshow);
//
//
//                        String sourcePath = getActivity().getExternalFilesDir(null).toString();
//
////                        copyFileStream(new File(sourcePath + "/" + image.getName()), photoURI, getContext());
//                        //  copyFileStream(image, photoURI, getContext());
//

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                try {

                    imgshow.setImageURI(fille);

                    Log.e("100file uriii-->", fille + "");
                    String mimeType = getActivity().getContentResolver().getType(fille);
                    Log.e("100mimee type-->", mimeType + "");

                    if (mimeType == null) {
                        String path = getPath(getContext(), fille);
                        Log.e("100string path-->", path + "");

                        if (path == null) {
                            filename = FilenameUtils.getName(fille.toString());
                        } else {
                            File file = new File(path);
                            filename = file.getName();
                        }
                    } else {
                        Uri returnUri = data.getData();

                        Log.e("100string cursontr-->", returnUri + "");

                        Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        filename = returnCursor.getString(nameIndex);
                        String size = Long.toString(returnCursor.getLong(sizeIndex));
                    }

//                        File fileSave = getActivity().getExternalFilesDir(null);
                    String sourcePath = getActivity().getExternalFilesDir(null).toString();
                    Log.e("100Acttt filee name-->", filename + "--\n sorc " + sourcePath);

                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), fille, getContext());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void takePicture() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fille = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fille);

        startActivityForResult(intent, 100);
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getContext(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().toString(), imageFileName);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void showImagePopup() {

        // File System.

        String[] mimeTypes =
                {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

//            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        startActivityForResult(Intent.createChooser(intent, "Choose File"), 210);

    }


    public Intent getPickImageChooserIntent() {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

// collect all camera intents
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            String packageName = res.activityInfo.packageName;
//            Log.e("package name", ">" + packageName);
//
//            outputFileUri = Uri.fromFile(getOutputMediaFile());
//
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
////                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//
//        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes =
                {"image/*", "application/pdf"};

        String mimeTypesStr = "";
        for (String mimeType : mimeTypes) {
            mimeTypesStr += mimeType + "|";
        }
//        galleryIntent.setType("*/*");
        galleryIntent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
//        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            String packageName = res.activityInfo.packageName;
            Log.e("package name", ">" + packageName);

            if (!packageName.equals("com.dropbox.android")) {
                Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);

                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                allIntents.add(intent);
            }
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);


// Create a chooser from the main  intent
        chooserIntent = Intent.createChooser(mainIntent, "Select File");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));


        return chooserIntent;
    }


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "WestFax");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }

        Log.e("getcapimgoutpt urii-->", getImage + " =====" + outputFileUri);

        return outputFileUri;
    }


    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
            Log.e("> PATHS " + dest.getName(), "> " + dest.getAbsolutePath());
            Picasso.with(context).load(dest).into(imgshow);
            String extension = dest.getAbsolutePath().substring(dest.getAbsolutePath().lastIndexOf("."));
            Log.e("my extn", extension + "");


            if (extension.toLowerCase().equals(".jpg") || extension.toLowerCase().equals(".jpeg")
                    || extension.toLowerCase().equals(".png") || extension.toLowerCase().equals(".pdf") ||
                    extension.toLowerCase().equals(".doc") || extension.toLowerCase().equals(".docx")) {

                edtaddfile.setText(dest.getName());
                String title = edtaddfile.getText().toString();

                if (title.matches("")) {
                    Toast.makeText(getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }


                PdfNameHolder = dest.getName();
                PdfPathHolder = dest.getAbsolutePath();


                Uri fileuri = Uri.fromFile(dest);
                //  String extttn = fileuri.getLastPathSegment();

                actionlist.add(PdfPathHolder);
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
            } else {
                Toast.makeText(getContext(), "Choose only Image,Pdf or Doc files", Toast.LENGTH_LONG).show();
            }

            Log.e("> pdfname hol " + PdfNameHolder, "> " + PdfPathHolder);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }


    private void uploadImage() {

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
        final ArrayList<ResExpandList> newflist = new ArrayList<>();
        Map<String, ResExpandList> mapp = new LinkedHashMap<>();

        for (int i = 0; i < resforsend.size(); i++) {
            Log.e("My IDDD-->", resforsend.get(i).getString2Value());


            ResExpandList reslis = new ResExpandList();
            reslis.setMid(resforsend.get(i).getString2Value());

            newflist.add(reslis);


        }
        String numbid = "Numbers";
        for (int i = 0; i < newflist.size(); i++) {
//            mapp.put(faxid + i, newflist.get(i));
            mapp.put(numbid + (i + 1), newflist.get(i));

        }


        final ArrayList<ResExpandList> newurii = new ArrayList<>();


        for (int i = 0; i < resforsend.size(); i++) {
            Log.e("My IDDD-->", resforsend.get(i).getString2Value());


            ResExpandList reslis = new ResExpandList();
            reslis.setMid(resforsend.get(i).getString2Value());

            newflist.add(reslis);


        }
        //  Log.e("filee innn-->", file.getName() + "");
        List<MultipartBody.Part> parts = new ArrayList<>();
        String faxid = "Files";
        for (int ii = 0; ii < actionlist.size(); ii++) {
            //   parts.add(prePareFilepart("Files" + (ii + 1), uriliss.get(ii)));
            String type = null;
//            Uri path = imageUri.fromFile(file);

            String extension = MimeTypeMap.getFileExtensionFromUrl(actionlist.get(ii) + "");
            Log.e("extensioon-->", extension + "");
            Log.e("urs items-->", actionlist.get(ii) + "-----" + extension);

            String lowcase = extension.toLowerCase();

            if (extension != null) {
//                if (extension.toLowerCase().endsWith(".jpg") || extension.toLowerCase().endsWith(".jpeg") ||
//                        extension.toLowerCase().endsWith(".png") || extension.toLowerCase().endsWith(".png")) {
//
//
//
                ContentResolver cR = getActivity().getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                type = mime.getExtensionFromMimeType(cR.getType(Uri.parse(lowcase)));

//                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowcase);
                Log.e("Mime Type --->", type + "--" + lowcase);


//                }
//                image / jpeg
//                application / pdf
//                image / png
            } else {
                type = "";
            }


            Log.e("pdf holder  code-->", PdfPathHolder + "=====" + PdfNameHolder + "==uridata=");
//            pdf holder  code-->: /storage/emulated/0/Pictures/JPEG_1520527520892.jpg=====JPEG_1520527520892.jpg==uridata=content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2FJPEG_1520527520892.jpg
//  fromfragg        //  pdf holder  code-->: /storage/emulated/0/test.pdf=====test.pdf==uridata=file:///storage/emulated/0/test.pdf
            RequestBody Pdf;

            File newfile = new File(actionlist.get(ii) + "");

            if (lowcase.equals(".pdf")) {
                Pdf = RequestBody.create(MediaType.parse("application/pdf"), newfile);
            } else if (lowcase.equals(".png") || lowcase.equals(".jpeg")) {
                Pdf = RequestBody.create(MediaType.parse("multipart/form-data"), newfile);
            } else if (lowcase.equals(".doc")) {
                Pdf = RequestBody.create(MediaType.parse("application/msword"), newfile);
            } else if (lowcase.equals(".docx")) {
                Pdf = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), newfile);
            } else {
                Pdf = RequestBody.create(MediaType.parse("multipart/form-data"), newfile);
            }


            Log.e("actionnn Type --->", actionlist.get(ii) + "\n Req body======" + Pdf);
//            Log.e("actionnnType fileee--->", "filname--" + newfile.getName() + "\n resliss" + urislist.get(ii) + "---------" + reslist.get(ii).getString1Name());

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("Files" + (ii + 1), newfile.getName(), Pdf);


            parts.add(body);

            Log.e("partss-->", new Gson().toJson(parts.get(ii)) + "");
        }

        // create RequestBody instance from file
        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);
        RequestBody ujob = RequestBody.create(MediaType.parse("text/plain"), "Test Job");
        RequestBody uheadr = RequestBody.create(MediaType.parse("text/plain"), edit_header.getText().toString());
        RequestBody ubillcod = RequestBody.create(MediaType.parse("text/plain"), "Customer Code 1234");
        //   RequestBody unumb = RequestBody.create(MediaType.parse("text/plain"), "7324004069");
//        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);
        RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
        RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
        RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");


        // MultipartBody.Part is used to send also the actual file name


//
//        RequestBody Pdf=RequestBody.create(MediaType.parse("multipart/form-data"),PdfPathHolder);

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
                        Toast.makeText(getContext(), "Fax Sent Successfully", Toast.LENGTH_LONG).show();

                        imagePath = "";
                        urislist.clear();
                        reslist.clear();
                        resAdapter.notifyData(reslist);
                        resAdapter.notifyDataForUri(urislist);
                        actionlist.clear();
                        resforsend.clear();
                        resUserAdapter.notifyData(resforsend);
                        edit_header.setText("");
                    } else {

                        Toast.makeText(getContext(), "Invalid File or Recipients", Toast.LENGTH_SHORT).show();

                        imagePath = "";
                        urislist.clear();
                        reslist.clear();
                        resAdapter.notifyData(reslist);
                        resAdapter.notifyDataForUri(urislist);
                        actionlist.clear();
                        resforsend.clear();
                        resUserAdapter.notifyData(resforsend);
                        edit_header.setText("");
                        //         Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), R.string.string_upload_fail, Toast.LENGTH_SHORT).show();
                    imagePath = "";
                    urislist.clear();
                    reslist.clear();
                    resAdapter.notifyData(reslist);
                    resAdapter.notifyDataForUri(urislist);
                    actionlist.clear();
                    resforsend.clear();
                    resUserAdapter.notifyData(resforsend);
                    edit_header.setText("");
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
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                imagePath = "";
                urislist.clear();
                reslist.clear();
                resAdapter.notifyData(reslist);
                resAdapter.notifyDataForUri(urislist);
                actionlist.clear();
                resforsend.clear();
                resUserAdapter.notifyData(resforsend);
                edit_header.setText("");
            }
        });

    }

}
