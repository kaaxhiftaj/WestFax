package com.business.admin.westfax;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.ErrnoException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.Pojo.Config;
import com.business.admin.westfax.Pojo.FilenameUtils;
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.adapter.SendFaxImageAdapter;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.permission.PermissionsActivity;
import com.business.admin.westfax.permission.PermissionsChecker;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
//import permissions.dispatcher.NeedsPermission;
//import permissions.dispatcher.OnShowRationale;
//import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SONY on 04-03-2018.
 */
//@RuntimePermissions
public class NewSendAtivity extends AppCompatActivity {

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
    Context mContext;

    PermissionsChecker checker;
    TextView txtpath;
    ArrayList<Uri> urislist = new ArrayList<>();
    String unamee, upaasss, uprodid;

    ArrayList<String> actionlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_send_fax);

        edtaddfile = (EditText) findViewById(R.id.edtaddcon);
        btnchoos = (ImageView) findViewById(R.id.btnchoos);
        imgshow = (ImageView) findViewById(R.id.imgshow);
        btnsendfx = (Button) findViewById(R.id.btnsendfx);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


        btnchoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 210);

//                onLoadImageClick(view);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 210) {
            if (resultCode == RESULT_OK) {
                try {
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();


                        Log.e("file uriii-->", uri + "");
                    /*if (filesize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this, "The selected file is too large. Selet a new file with size less than 2mb", Toast.LENGTH_LONG).show();
                    } else {*/
                        String mimeType = getContentResolver().getType(uri);
                        Log.e("mimee type-->", mimeType + "");


                        if (mimeType == null) {
                            String path = getPath(getApplicationContext(), uri);
                            Log.e("string path-->", path + "");

                            if (path == null) {
                                filename = FilenameUtils.getName(uri.toString());
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            Uri returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }

                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();
                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), uri, getApplicationContext());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {


                        String timeStamp =
                                new SimpleDateFormat("yyyyMMdd_HHmmss",
                                        Locale.getDefault()).format(new Date());
                        String imageFileName = "IMG_" + timeStamp + "_";
                        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File image = File.createTempFile(
                                imageFileName,  /* prefix */
                                ".jpg",         /* suffix */
                                storageDir      /* directory */
                        );

                        String imageFilePath = image.getAbsolutePath();


                        Log.e("immmg path-->", image.getAbsolutePath() + "--" + image.getName());
//
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.business.admin.westfax.fileprovider", image);
//grant uri with essential permission the first arg is the The packagename you would like to allow to access the Uri.
                        getApplicationContext().grantUriPermission("com.android.camera", photoURI,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        data.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);


                        copyFileStream(image, photoURI, getApplicationContext());


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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


    public Intent getPickImageChooserIntent() {


        try {

            Uri outputFileUri = getCaptureImageOutputUri();



            Intent loCameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            loCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


            String[] lsMimeTypes = {"image/*", "application/pdf"};


            Intent loIntent = new Intent(Intent.ACTION_GET_CONTENT);

            loIntent.addCategory(Intent.CATEGORY_OPENABLE);

            loIntent.setType("*/*");

            loIntent.putExtra(Intent.EXTRA_MIME_TYPES, lsMimeTypes);

            loIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);


            List<ResolveInfo> loResolveInfoList = getPackageManager().queryIntentActivities(loIntent, 0);


            List<Intent> loTargets = new ArrayList<Intent>();


            loTargets.add(loCameraIntent);



            for (ResolveInfo loResolveInfo : loResolveInfoList) {

                String lsPackageName = loResolveInfo.activityInfo.packageName;

                if (!lsPackageName.equals("com.google.android.apps.photos") && !lsPackageName.equals("com.google.android.apps.plus")

                        && !lsPackageName.equals("com.google.android.apps.docs")

                        && !lsPackageName.equals("com.google.android.apps.docs.app.GetContentActivity")

                        && !lsPackageName.equals("com.google.android.apps.docs.app.PickEntryActivity")) {

                    Intent loChooseIntent = new Intent();

                    loChooseIntent.setType("*/*");

                    loChooseIntent.addCategory(Intent.CATEGORY_OPENABLE);

                    loChooseIntent.setAction(Intent.ACTION_GET_CONTENT);

                    loChooseIntent.putExtra(Intent.EXTRA_MIME_TYPES, lsMimeTypes);

                    loChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                    loChooseIntent.setPackage(lsPackageName);

                    loTargets.add(loChooseIntent);
                }

            }


            Intent loChooserIntent = Intent.createChooser(loTargets.remove(2), "Hazard Scout");

            loChooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,

                    loTargets.toArray(new Parcelable[loTargets.size()]));


            startActivityForResult(loChooserIntent, 210);


        } catch (Exception e) {

            Log.e("file null", String.valueOf(e));

        }


           Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            String packageName = res.activityInfo.packageName;
            Log.e("package name", ">" + packageName);
            if (!packageName.equals("com.dropbox.android")) {
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                allIntents.add(intent);
            }
        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes =
                {"image/*", "application/pdf"};

        String mimeTypesStr = "";
        for (String mimeType : mimeTypes) {
            mimeTypesStr += mimeType + "|";
        }
//        galleryIntent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));

        galleryIntent.setType("*/*");
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);


//        galleryIntent.setType("*/*");
//        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            String packageName = res.activityInfo.packageName;
            Log.e("package name", ">" + packageName);

            if (!packageName.equals("com.dropbox.android")) {
                Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//            getActivity().grantUriPermission(res.activityInfo.packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

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
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
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
            edtaddfile.setText(dest.getName());
            String title = edtaddfile.getText().toString();

            if (title.matches("")) {
                Toast.makeText(getApplicationContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
                return;
            }


            PdfNameHolder = dest.getName();
            PdfPathHolder = dest.getAbsolutePath();


            Log.e("> pdfname hol " + PdfNameHolder, "> " + PdfPathHolder);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }
}
