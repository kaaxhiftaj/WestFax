package com.business.admin.westfax.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.ErrnoException;
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
import com.business.admin.westfax.Pojo.UserSessionManager;
import com.business.admin.westfax.R;
import com.business.admin.westfax.UriHelpers;
import com.business.admin.westfax.adapter.SendFaxImageAdapter;
import com.business.admin.westfax.adapter.SendFaxUserAdapter;
import com.business.admin.westfax.permission.PermissionsChecker;
import com.business.admin.westfax.retrofit.LogEasyApi;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultsetForLogin;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Created by SONY on 13-03-2018.
 */

public class NewSendFragment extends Fragment {
    private static int SPLASH_TIME_OUT = 1900;
    Intent intent;
    private ArrayList<String> filePaths;
    ArrayList<String> selectedPaths;

    private ImageView imgg;
    private Uri mCropImageUri;

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
//    String imagePath;
    ImageView btnchoos;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;

    PermissionsChecker checker;
    TextView txtpath;
    ArrayList<Uri> urislist = new ArrayList<>();
    String unamee, upaasss, uprodid;
    Uri imageUri;
    UserSessionManager sessionManager;
    ArrayList<String> actionlist;
    private static final int MY_REQUEST_CODE = 112;

    private static final int MY_REQUEST_CODE_STORAGE = 113;
    private static final int REQUEST_CAMERA =114;
//     File imagePath;
Uri currentImageUri;
    public NewSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        View rootview = inflater.inflate(R.layout.fragment_send_fax, container, false);
        mContext = getContext();
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


        recycleforsend.setHasFixedSize(true);
        // The number of Columns
        linforsend = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleforsend.setLayoutManager(linforsend);
        resUserAdapter = new SendFaxUserAdapter(getContext(), resforsend);
        recycleforsend.setAdapter(resUserAdapter);


        imgaddcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                onLoadImageClick(view);
            }
        });

        btnsendfx.setOnClickListener(buttnsend);
        return rootview;
    }


    View.OnClickListener buttnsend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            if (!TextUtils.isEmpty(imagePath)) {
//                uploadImage();

//                uploadImage(urislist);
//                Log.e("button", "onClick:" + urislist);


//            } else {
//                Toast.makeText(getContext(), R.string.string_message_to_attach_file, Toast.LENGTH_SHORT).show();
//
//            }
        }

    };

    public void onLoadImageClick(View view) {


        clickCamera();
//        startActivityForResult(getPickImageChooserIntent(), 200);
    }


    private void clickCamera() { // 1 for icon and 2 for attachment
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},MY_REQUEST_CODE);
        }else {
            if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_STORAGE);
            }else{
                currentImageUri = getImageFileUri();

                Log.e("currimhpic-->",currentImageUri+"");
                Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri); // set the image file name
                // start the image capture Intent
                startActivityForResult(intentPicture, REQUEST_CAMERA);  // 1 for REQUEST_CAMERA and 2 for REQUEST_CAMERA_ATT
            }
        }
    }

    private static Uri getImageFileUri(){
        // Create a storage directory for the images
        // To be safe(er), you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing getContext()

        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyProject");

        Log.e("immmg path-->",imagePath+"");
        if (! imagePath.exists()){
            if (! imagePath.mkdirs()){
                return null;
            }else{
                //create new folder
            }
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(imagePath,"West_"+ timeStamp + ".jpg");

        if(!image.exists()){
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("uri from file -->",Uri.fromFile(image)+"");

        // Create an File Uri
        return Uri.fromFile(image);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_STORAGE);
                    } else {
                        currentImageUri = getImageFileUri();

                        Log.e("Reqq curr img uri -->",currentImageUri+"");

                        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri); // set the image file name
                        // start the image capture Intent
                        startActivityForResult(intentPicture, REQUEST_CAMERA);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on getContext() permission.
                    Toast.makeText(getContext(), "Doesn't have permission... ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_REQUEST_CODE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentImageUri = getImageFileUri();
                    Log.e("Reqcurimguri for reqstore -->",currentImageUri+"");

                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri); // set the image file name
                    // start the image capture Intent
                    startActivityForResult(intentPicture, REQUEST_CAMERA);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on getContext() permission.
                    Toast.makeText(getContext(), "Doesn't have permission...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
//    public String getPathFromURI(Uri contentUri) {
//        String res = null;
//
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
//
//
//    public void openPath(Uri uri) {
//        InputStream is = null;
//        try {
//            is = getActivity().getContentResolver().openInputStream(uri);
//            //Convert your stream to data here
//            is.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
////File ff= null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                File photoFile = null;
//
//
//                Uri uri = data.getData();
//
//
//                Log.e("UN uriME-->", "" + uri + "----" + "");
//
//                if (uri == null) {
//
//                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
//                    File file = new File(imageUri.getPath());
//                    try {
//                        InputStream ims = new FileInputStream(file);
//                        imgshow.setImageBitmap(BitmapFactory.decodeStream(ims));
//                    } catch (FileNotFoundException e) {
//                        return;
//                    }
//
//                    // ScanFile so it will be appeared on Gallery
//                    MediaScannerConnection.scanFile(getContext(),
//                            new String[]{imageUri.getPath()}, null,
//                            new MediaScannerConnection.OnScanCompletedListener() {
//                                public void onScanCompleted(String path, Uri uri) {
//                                }
//                            });
//
//
//                    if (data.resolveActivity(getActivity().getPackageManager()) != null) {
//                        // Create the File where the photo should go
//                        try {
//                            photoFile = createImageFile();
//                        } catch (IOException ex) {
//                            // Error occurred while creating the File
//                            return;
//                        }
//                        // Continue only if the File was successfully created
//                        if (photoFile != null) {
//                            imageUri = FileProvider.getUriForFile(getContext(),
//                                    getContext().getPackageName() + ".fileprovider", photoFile);
//                            data.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//                            Log.e("newewewew-->", "" + imageUri);
//                            File pathforcam = UriHelpers.getFileForUri(getContext(), imageUri);
//
//                            Picasso.with(getContext()).load(pathforcam)
//                                    .into(imgshow);
//
//                        }
//                    }
//
//
//                } else {
//
//                    File myFile = new File(uri.toString());
//                    String ss = myFile.getAbsolutePath();
//                    imgshow.setImageURI(uri);
//                    Log.e("File absoultt-->", "" + myFile+"\n"+ss);
//
//
//
//
////                    File pathforutil = UriHelpers.getFileForUri(getContext(), uri);
////
////                    Log.e("File  ME-->", "" + "\n" + pathforutil);
////
////                    imageUri = FileProvider.getUriForFile(getContext(),
////                            getContext().getPackageName() + ".fileprovider", pathforutil);
////                    data.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////
////
////
////                    String uriString = imageUri.toString();
////
////                    File myfiless = new File(imageUri.getPath());
////                    String abspath = myfiless.getAbsolutePath();
////
////                    Log.e("myfilee-->", myfiless + "\n" + abspath
////                            + "\n" + imageUri);
////
//////                    Picasso.with(getContext()).load(pathforutil)
//////                            .into(imgshow);
////
////              edtaddfile.setText(pathforutil.getName());
////                    String title = edtaddfile.getText().toString();
////                 if (title.matches("")) {
////                        Toast.makeText(getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
////                        return;
////                    }
////
////
////                    PdfNameHolder = pathforutil.getName();
////                    PdfPathHolder = imageUri.getPath();
//////                            FileUtils.getPath(getContext(), imageUri);
////                    imagePath = imageUri.getPath();
////                    actionlist.add(PdfPathHolder);
////
////                    ResExpandList mLog = new ResExpandList();
////                    mLog.setString1Name(title);
////                    reslist.add(mLog);
////                    resAdapter.notifyData(reslist);
////
////                    edtaddfile.setText("");
////                    Uri uriii = Uri.parse(mLog.getString1Name());
////                    Log.e("lastt uriii--->", uriii + "");
////                    urislist.add(uriii);
////                    resAdapter.notifyData(reslist);
////
////                    resAdapter.notifyDataForUri(urislist);
////
//////                    myfilee-->: /myexternalimages/WhatsApp/Media/WhatsApp Images/IMG-20180227-WA0000.jpg
//////                            /myexternalimages/WhatsApp/Media/WhatsApp Images/IMG-20180227-WA0000.jpg
//////                    content://com.business.admin.westfax.fileprovider/myexternalimages/WhatsApp/Media/WhatsApp%20Images/IMG-20180227-WA0000.jpg
////
////                    Log.e("go naugta data-->", imageUri + "");
//
//                }
//            } else {
//
//                Log.e("go data-->", data + "");
////
//                imageUri = getPickImageResultUri(data);
//
//
//                Log.e("On activity Uriiiii-->", imageUri + "");
////
//                // For API >= 23 we need to check specifically that we have permissions to read external storage,
//                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//                boolean requirePermissions = false;
//
//
//                if (!requirePermissions) {
//
//                    Log.e("Onact ntprm imguri-->", imageUri + "");
////                imgg.setImageUriAsync(imageUri);
//
//
//                    String uriString = imageUri.toString();
//                    File myFile = new File(uriString);
//                    String path = myFile.getAbsolutePath();
//                    Log.e("abs pathh name--->", path + "");
////[Ljava.lang.St
//                    String[] filepathh = {MediaStore.Images.Media.DATA};
//                    Log.e("strpathh cong name--->", filepathh + "");
//                    if (uriString.startsWith("content://")) {
//                        Cursor cursor = null;
//                        try {
//
//                            cursor = getActivity().getContentResolver().query(imageUri, filepathh, null, null, null);
//                            if (cursor != null && cursor.moveToFirst()) {
//                                imagePath = cursor.getString(cursor.getColumnIndex(filepathh[0]));
////                                OpenableColumns.DISPLAY_NAME));
//                                Picasso.with(getContext()).load(new File(imagePath))
//                                        .into(imgshow);
//                                // ImageView.setImageURI(Uri.parse(new File("/sdcard/cats.jpg").toString()));
//                            }
//
//                        } finally {
//                            cursor.close();
//                        }
//                    } else if (uriString.startsWith("file://")) {
//                        imagePath = myFile.getName();
//                    }
//                    Log.e("dis  cong namee--->", imagePath + "");
//                    final File file = new File(imagePath);
//                    edtaddfile.setText(file.getName());
//                    String title = edtaddfile.getText().toString();
//
//                    if (title.matches("")) {
//                        Toast.makeText(getContext(), "You did not enter a Title", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    PdfNameHolder = file.getName();
//                    PdfPathHolder = FileUtils.getPath(getContext(), imageUri);
//
//                    actionlist.add(PdfPathHolder);
////                partss-->: {"headers":{"namesAndValues":["Content-Disposition","form-data; name\u003d\"Files2\"; filename\u003d\"IMG_20180224_211432.jpg\""]}}
////                partss-->: {"headers":{"namesAndValues":["Content-Disposition","form-data; name\u003d\"Files2\"; filename\u003d\"JPEG_1520527520892.jpg\""]}}
////                partss-->: {"headers":{"namesAndValues":["Content-Disposition","form-data; name\u003d\"Files2\"; filename\u003d\"JPEG_1520527520892.jpg\""]}}
//                    ResExpandList mLog = new ResExpandList();
//                    mLog.setString1Name(title);
//                    reslist.add(mLog);
//                    resAdapter.notifyData(reslist);
//
//                    edtaddfile.setText("");
//                    Uri uriii = Uri.parse(mLog.getString1Name());
//                    Log.e("lastt uriii--->", uriii + "");
//                    urislist.add(uriii);
//                    resAdapter.notifyData(reslist);
//
//                    resAdapter.notifyDataForUri(urislist);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                isUriRequiresPermissions(imageUri)) {
//
//            // request permissions and handle the result in onRequestPermissionsResult()
//            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //    imgg.setImageUriAsync(mCropImageUri);
//                Picasso.with(getContext()).load(mCropImageUri)
//                        .into(imgg);
//
//                Log.e("onrequepr cropimguri-->", mCropImageUri + "");
//
//            } else {
//                Toast.makeText(getContext(), "Required permissions are not granted", Toast.LENGTH_LONG).show();
//            }
//
//
//            mCropImageUri = imageUri;
//            Log.e("Onactvity prmsn reui-->", mCropImageUri + "");
////
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//        }
//
//
//    }
//
//
//    public Intent getPickImageChooserIntent() {
//
//// Determine Uri of camera image to  save.
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getActivity().getPackageManager();
//
//// collect all camera intents
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
////            getActivity().grantUriPermission(res.activityInfo.packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//// collect all gallery intents
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
////            getActivity().grantUriPermission(res.activityInfo.packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            // intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            allIntents.add(intent);
//        }
//
//// the main intent is the last in the  list (fucking android) so pickup the useless one
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//
//
//// Create a chooser from the main  intent
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//
//// Add all other intents
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//
//        return chooserIntent;
//    }
//
//    /**
//     * Get URI to image received from capture  by camera.
//     */
//    private Uri getCaptureImageOutputUri() {
//        Uri outputFileUri = null;
//        File getImage = getActivity().getExternalCacheDir();
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
//        }
//
//        Log.e("getcapimgoutpt urii-->", getImage + " =====" + outputFileUri);
//
//        return outputFileUri;
//    }
//
//    /**
//     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
//     * Will return the correct URI for camera  and gallery image.
//     *
//     * @param data the returned data of the  activity result
//     */
//    public Uri getPickImageResultUri(Intent data) {
//        boolean isCamera = true;
//        if (data != null && data.getData() != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        }
//        return isCamera ? getCaptureImageOutputUri() : data.getData();
//    }
//
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + ".jpg";
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
////        File storageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), imageFileName);
//////                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), imageFileName);
////        File path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File image = new File(path, imageFileName);
////        try {
////            /* Making sure the Pictures directory exist.*/
////            path.mkdir();
////            storageDir.createNewFile();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        return image;
//    }
//
//
//    /**
//     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
//     */
//    public boolean isUriRequiresPermissions(Uri uri) {
//        try {
//            ContentResolver resolver = getActivity().getContentResolver();
//            InputStream stream = resolver.openInputStream(uri);
//            stream.close();
//            return false;
//        } catch (FileNotFoundException e) {
//            if (e.getCause() instanceof ErrnoException) {
//                return true;
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//
//    //    ArrayList<Uri> uriliss
//    private void uploadImage() {
//
//        /**
//         * Progressbar to Display if you need
//         */
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Config.LOGIN_AUTHENTICATE)
//                .client(Config.okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        //Create Upload Server Client
//        LogEasyApi service = retrofit.create(LogEasyApi.class);
//        final ArrayList<ResExpandList> newflist = new ArrayList<>();
//        Map<String, ResExpandList> mapp = new LinkedHashMap<>();
//
//        for (int i = 0; i < resforsend.size(); i++) {
//            Log.e("My IDDD-->", resforsend.get(i).getString2Value());
//
//
//            ResExpandList reslis = new ResExpandList();
//            reslis.setMid(resforsend.get(i).getString2Value());
//
//            newflist.add(reslis);
//
//
//        }
//        String numbid = "Numbers";
//        for (int i = 0; i < newflist.size(); i++) {
////            mapp.put(faxid + i, newflist.get(i));
//            mapp.put(numbid + (i + 1), newflist.get(i));
//
//        }
//
//
//        final ArrayList<ResExpandList> newurii = new ArrayList<>();
//
//
//        for (int i = 0; i < resforsend.size(); i++) {
//            Log.e("My IDDD-->", resforsend.get(i).getString2Value());
//
//
//            ResExpandList reslis = new ResExpandList();
//            reslis.setMid(resforsend.get(i).getString2Value());
//
//            newflist.add(reslis);
//
//
//        }
//
//
//        //  Log.e("filee innn-->", file.getName() + "");
//        List<MultipartBody.Part> parts = new ArrayList<>();
//        String faxid = "Files";
//        for (int ii = 0; ii < actionlist.size(); ii++) {
//            //   parts.add(prePareFilepart("Files" + (ii + 1), uriliss.get(ii)));
//            String type = null;
////            Uri path = imageUri.fromFile(file);
//
//            Log.e("urs items-->", actionlist.get(ii) + "");
//            String extension = MimeTypeMap.getFileExtensionFromUrl(actionlist.get(ii) + "");
//            if (extension != null) {
//                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//                Log.e("Mime Type --->", type + "");
////                image / jpeg
////                application / pdf
////                image / png
//            } else {
//                type = "";
//            }
//
//            Log.e("pdf holder  code-->", PdfPathHolder + "=====" + PdfNameHolder + "==uridata=" + imageUri);
////            pdf holder  code-->: /storage/emulated/0/Pictures/JPEG_1520527520892.jpg=====JPEG_1520527520892.jpg==uridata=content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2FJPEG_1520527520892.jpg
////  fromfragg        //  pdf holder  code-->: /storage/emulated/0/test.pdf=====test.pdf==uridata=file:///storage/emulated/0/test.pdf
//            RequestBody Pdf;
//
//            File newfile = new File(actionlist.get(ii) + "");
//
//
//            if (type.equals("application/pdf")) {
//                Pdf = RequestBody.create(MediaType.parse("application/pdf"), newfile);
//            } else if (type.equals("image/png") || type.equals("image/jpeg")) {
//                Pdf = RequestBody.create(MediaType.parse("multipart/form-data"), newfile);
//            } else {
//                Pdf = RequestBody.create(MediaType.parse("multipart/form-data"), newfile);
//            }
//
//
//            Log.e("actionnn Type --->", actionlist.get(ii) + "\n Req body======" + Pdf);
//            Log.e("actionnnType fileee--->", "filname--" + newfile.getName() + "\n resliss" + urislist.get(ii) + "---------" + reslist.get(ii).getString1Name());
//
//            MultipartBody.Part body =
//                    MultipartBody.Part.createFormData("Files" + (ii + 1), newfile.getName(), Pdf);
//
//
//            parts.add(body);
//
//            Log.e("partss-->", new Gson().toJson(parts.get(ii)) + "");
//        }
//
//        // create RequestBody instance from file
//        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), unamee);
//        RequestBody upass = RequestBody.create(MediaType.parse("text/plain"), upaasss);
//        RequestBody cooki = RequestBody.create(MediaType.parse("text/plain"), "false");
//        RequestBody uprodctkey = RequestBody.create(MediaType.parse("text/plain"), uprodid);
//        RequestBody ujob = RequestBody.create(MediaType.parse("text/plain"), "Test Job");
//        RequestBody uheadr = RequestBody.create(MediaType.parse("text/plain"), edit_header.getText().toString());
//        RequestBody ubillcod = RequestBody.create(MediaType.parse("text/plain"), "Customer Code 1234");
//        //   RequestBody unumb = RequestBody.create(MediaType.parse("text/plain"), "7324004069");
////        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);
//        RequestBody ucsid = RequestBody.create(MediaType.parse("text/plain"), "Station Id");
//        RequestBody uani = RequestBody.create(MediaType.parse("text/plain"), "");
//        RequestBody ustartdt = RequestBody.create(MediaType.parse("text/plain"), "");
//        RequestBody uquality = RequestBody.create(MediaType.parse("text/plain"), "Fine");
//        RequestBody ufeedmail = RequestBody.create(MediaType.parse("text/plain"), "chad@westfax.com");
//
//
//        // MultipartBody.Part is used to send also the actual file name
//
//
////
////        RequestBody Pdf=RequestBody.create(MediaType.parse("multipart/form-data"),PdfPathHolder);
//
//        Call<ResultsetForLogin> resultCall = service.uploadImage(uname, upass, cooki, uprodctkey, ujob, uheadr,
//                ubillcod, mapp, parts, ucsid, uani, ustartdt, uquality, ufeedmail);
//
//        // finally, execute the request
//        resultCall.enqueue(new Callback<ResultsetForLogin>() {
//            @Override
//            public void onResponse(Call<ResultsetForLogin> call, Response<ResultsetForLogin> response) {
//
//                progressDialog.dismiss();
//                Log.e("respo  code-->", response.body().getStatusCode() + " ---" + response.body().getDattta());
//                // Response Success or Fail
//                if (response.isSuccessful()) {
//
//                    if (response.body().getStatusCode().equals("200")) {
//                        Toast.makeText(getContext(), "Fax Sent Successfully", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Invalid File", Toast.LENGTH_SHORT).show();
//                        //         Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
//                    }
//
//                } else {
//                    Toast.makeText(getContext(), R.string.string_upload_fail, Toast.LENGTH_SHORT).show();
//
//                }
//
//                /**
//                 * Update Views
//                 */
//
////                textView.setVisibility(View.VISIBLE);
////                imageView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onFailure(Call<ResultsetForLogin> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(getContext(), "error get", Toast.LENGTH_SHORT).show();
//            }
//        });
//        imagePath = "";
//        urislist.clear();
//        reslist.clear();
//        resAdapter.notifyData(reslist);
//        resAdapter.notifyDataForUri(urislist);
//        actionlist.clear();
//        resforsend.clear();
//        edit_header.setText("");
//    }
    }
}
