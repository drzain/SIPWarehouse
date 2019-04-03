package com.sip.warehouse;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApproveActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private static final String TAG = ApproveActivity.class.getSimpleName();
    RecyclerView listViewPart;
    int bitmap_size = 40;
    int max_resolution_image = 200;
    private static final int CAMERA_KTP = 100;
    private static final int CAMERA_SATU = 200;
    private static final int CAMERA_DUA = 300;
    private static final int CAMERA_TIGA = 400;
    private static final int CAMERA_EMPAT = 500;
    private static final int CAMERA_LIMA = 600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    DatePickerDialog datePickerDialog;
    private TextView appCustomer,appMobil,appPlat;
    private EditText appTitle, appEmail, appName, appstnkDate, appstnkTax, appKm, appAssetCondition;
    String token, idreceive, linkFoto1, linkFoto2, linkFoto3, linkFoto4, linkFoto5, linkKTP;
    String txtPICName, txtPICTitle, txtPICEmail, stnkExp, stnkTaxDate, kmCar, carCondition, imageKtp;;
    private ProgressDialog pDialog;
    private ListApproveAdapter mListadapter;
    private ImageView appKTP, appFoto1, appFoto2, appFoto3, appFoto4, appFoto5;
    private ImageButton btnKtp;
    private ImageView btnFot1,btnFot2,btnFot3,btnFot4,btnFot5;
    private Button btnApprove;
    Bitmap bitmap, decoded;
    String encodedImageKtp, encodedImageF1,encodedImageF2,encodedImageF3,encodedImageF4,encodedImageF5;
    String fixencodedImageKtp, fixencodedImageF1,fixencodedImageF2,fixencodedImageF3,fixencodedImageF4,fixencodedImageF5;
    Intent intent;
    Uri fileUri;
    Boolean statFotoKTP = false;
    Boolean statFoto1 = false;
    Boolean statFoto2 = false;
    Boolean statFoto3 = false;
    Boolean statFoto4 = false;
    Boolean statFoto5 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        listViewPart = (RecyclerView) findViewById(R.id.part_new_approve);
        appCustomer = (TextView) findViewById(R.id.approveName);
        appMobil = (TextView) findViewById(R.id.approveMobil);
        appPlat = (TextView) findViewById(R.id.approvePlat);
        appName = (EditText) findViewById(R.id.appDevName);
        appTitle = (EditText) findViewById(R.id.appDevTitle);
        appEmail = (EditText) findViewById(R.id.appDevEmail);
        appstnkDate = (EditText) findViewById(R.id.appstnkExpDate);
        appstnkTax = (EditText) findViewById(R.id.appstnkTaxDate);
        appKm = (EditText) findViewById(R.id.appkm);
        appAssetCondition = (EditText) findViewById(R.id.appassetCondition);
        appKTP = (ImageView) findViewById(R.id.viewAppKtp);
        appFoto1 = (ImageView) findViewById(R.id.viewAppFoto1);
        appFoto2 = (ImageView) findViewById(R.id.viewAppFoto2);
        appFoto3 = (ImageView) findViewById(R.id.viewAppFoto3);
        appFoto4 = (ImageView) findViewById(R.id.viewAppFoto4);
        appFoto5 = (ImageView) findViewById(R.id.viewAppFoto5);
        btnFot1 = (ImageView) findViewById(R.id.appCamera1);
        btnFot2 = (ImageView) findViewById(R.id.appCamera2);
        btnFot3 = (ImageView) findViewById(R.id.appCamera3);
        btnFot4 = (ImageView) findViewById(R.id.appCamera4);
        btnFot5 = (ImageView) findViewById(R.id.appCamera5);
        btnKtp = (ImageButton) findViewById(R.id.cmrAppKtp);
        btnApprove = (Button) findViewById(R.id.appsimpan);


        appstnkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ApproveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                appstnkDate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        appstnkTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ApproveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                appstnkTax.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(this.getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        Intent intent=getIntent();
        String customerName = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        String platNumber = intent.getStringExtra("plat");
        String description = intent.getStringExtra("desc");
        String year = intent.getStringExtra("year");
        idreceive = intent.getStringExtra("idreceive");

        appCustomer.setText(customerName+" - "+code);
        appPlat.setText(platNumber);
        appMobil.setText(description+" - "+year);

        loadDetailList();

        btnKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_KTP);
            }
        });

        btnFot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_SATU);
            }
        });

        btnFot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_DUA);
            }
        });

        btnFot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_TIGA);
            }
        });

        btnFot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_EMPAT);
            }
        });

        btnFot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_LIMA);
            }
        });

    }

    /**
     * Requesting permissions using Dexter library
     */
    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            openCamera(type);
                            Log.e("tipe cam :", String.valueOf(type));

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openCamera(final int type) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, type);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Log.e("reqcode",String.valueOf(requestCode));
        Log.e("resultcode", String.valueOf(resultCode));
        if (requestCode == CAMERA_KTP) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFotoKTP = true;
                Log.e("linkKTP",linkKTP);
                hapusFoto(linkKTP);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == CAMERA_SATU) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFoto1 = true;
                Log.e("linkfoto1",linkFoto1);
                hapusFoto(linkFoto1);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == CAMERA_DUA) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFoto2 = true;
                Log.e("linkfoto2",linkFoto2);
                hapusFoto(linkFoto2);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == CAMERA_TIGA) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFoto3 = true;
                Log.e("linkfoto3",linkFoto3);
                hapusFoto(linkFoto3);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == CAMERA_EMPAT) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFoto4 = true;
                Log.e("linkfoto4",linkFoto4);
                hapusFoto(linkFoto4);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == CAMERA_LIMA) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                Log.e("CAMERA", fileUri.getPath());
                bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                statFoto5 = true;
                Log.e("linkfoto5",linkFoto5);
                hapusFoto(linkFoto5);
                previewCapturedImage(getResizedBitmap(bitmap, max_resolution_image),requestCode);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * Display image from gallery
     */
    private void previewCapturedImage(Bitmap bmp,int tipe) {
        try {

            if(tipe == CAMERA_KTP){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageKtp = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageKtp = "data:image/jpeg;base64," + encodedImageKtp.replace(" ", "").replace("\n", "");

                appKTP.setImageBitmap(decoded);
            }else if(tipe == CAMERA_SATU){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF1 = "data:image/jpeg;base64," + encodedImageF1.replace(" ", "").replace("\n", "");

                appFoto1.setImageBitmap(decoded);
            }else if(tipe == CAMERA_DUA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF2 = "data:image/jpeg;base64," + encodedImageF2.replace(" ", "").replace("\n", "");

                appFoto2.setImageBitmap(decoded);
            }else if(tipe == CAMERA_TIGA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF3 = "data:image/jpeg;base64," + encodedImageF3.replace(" ", "").replace("\n", "");

                appFoto3.setImageBitmap(decoded);
            }else if(tipe == CAMERA_EMPAT){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF4 = "data:image/jpeg;base64," + encodedImageF4.replace(" ", "").replace("\n", "");

                appFoto4.setImageBitmap(decoded);
            }else if(tipe == CAMERA_LIMA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF5 = "data:image/jpeg;base64," + encodedImageF5.replace(" ", "").replace("\n", "");

                appFoto5.setImageBitmap(decoded);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Alert dialog to navigate to app settings
     * to enable necessary permissions
     */
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ApproveActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConfig.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("error", "Oops! Failed create "
                        + AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void hapusFoto(final String namafile) {

        String tag_string_req = "req_hapus";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_FOTO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "obj: " + jObj.toString());
                    String error = jObj.getString("status");
                    Log.d(TAG, "obj: " + error);
                    // Check for error node in json

                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Remove Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Delete Failed", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("file_name", namafile);
                Log.e(TAG, "file_name: " + namafile);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                Log.e(TAG, "token: " + token);
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void loadDetailList(){

        String URL_DETAIL = "https://sip.uridu.id/api/v1/asset-receives/"+idreceive+"/detail";
        final String IMAGE_URL = "http://sip.uridu.id/upload/asset-receive/";
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("respon",response);
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject queArray = obj.getJSONObject("data");
                            //Log.e("test",queArray.getString("pic_delivery_name"));
                            appName.setText(queArray.getString("pic_delivery_name"));
                            appTitle.setText(queArray.getString("pic_delivery_title"));
                            appEmail.setText(queArray.getString("pic_delivery_email"));
                            appstnkDate.setText(queArray.getString("stnk_exp_date"));
                            appstnkTax.setText(queArray.getString("stnk_exp_tax_notice"));
                            appKm.setText(queArray.getString("km"));
                            appAssetCondition.setText(queArray.getString("asset_condition"));
                            linkKTP = queArray.getString("pic_delivery_photo_identity");
                            Log.e("image",IMAGE_URL+linkKTP);
                            Picasso.get().load(IMAGE_URL+linkKTP).into(appKTP);
                            JSONArray camArray = new JSONArray(queArray.getString("images"));
                            JSONArray partsArray = new JSONArray(queArray.getString("parts"));
                            for (int i = 0; i < camArray.length(); i++) {
                                JSONObject camObject = camArray.getJSONObject(i);
                                Log.e("camera",camObject.toString());
                                String idFoto = camObject.getString("photo_id");
                                String linkFoto = camObject.getString("file_name");
                                if(idFoto.equals("1")){
                                    linkFoto1 = linkFoto;
                                }else if(idFoto.equals("2")){
                                    linkFoto2 = linkFoto;
                                }else if(idFoto.equals("3")){
                                    linkFoto3 = linkFoto;
                                }else if(idFoto.equals("4")){
                                    linkFoto4 = linkFoto;
                                }else if(idFoto.equals("5")){
                                    linkFoto5 = linkFoto;
                                }
                            }
                            Log.e("foto1",linkFoto1);
                            Picasso.get().load(IMAGE_URL+linkFoto1).into(appFoto1);
                            Log.e("foto2",linkFoto2);
                            Picasso.get().load(IMAGE_URL+linkFoto2).into(appFoto2);
                            Log.e("foto3",linkFoto3);
                            Picasso.get().load(IMAGE_URL+linkFoto3).into(appFoto3);
                            Log.e("foto4",linkFoto4);
                            Picasso.get().load(IMAGE_URL+linkFoto4).into(appFoto4);
                            Log.e("foto5",linkFoto5);
                            Picasso.get().load(IMAGE_URL+linkFoto5).into(appFoto5);

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(ApproveActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listViewPart.setLayoutManager(layoutManager);

                            //now looping through all the elements of the json array
                            ArrayList data = new ArrayList<DataDetailApprove>();
                            for (int i = 0; i < partsArray.length(); i++) {
                                JSONObject queObject = partsArray.getJSONObject(i);
                                //Log.e("parts",queObject.toString());
                                data.add(
                                        new DataDetailApprove(
                                                queObject.getString("asset_receive_id"),
                                                queObject.getString("part_id"),
                                                queObject.getString("value"),
                                                queObject.getString("notes"),
                                                queObject.getString("part_name")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            mListadapter = new ListApproveAdapter(data);
                            listViewPart.setAdapter(mListadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer "+token);
                        Log.e("token", token);
                        return headers;
                    }

                };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public class ListApproveAdapter extends RecyclerView.Adapter<ListApproveAdapter.ViewHolder>
    {
        private ArrayList<DataDetailApprove> dataList;


        public ListApproveAdapter(ArrayList<DataDetailApprove> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textQues;
            TextView idQues;
            EditText txtPart;
            CheckBox chkPart;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textQues = (TextView) itemView.findViewById(R.id.partQue);
                this.idQues = (TextView) itemView.findViewById(R.id.idPart_);
                this.txtPart = (EditText) itemView.findViewById(R.id.partEdt);
                this.chkPart = (CheckBox) itemView.findViewById(R.id.partChk);
            }
        }

        @Override
        public ListApproveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partreceive, parent, false);

            ApproveActivity.ListApproveAdapter.ViewHolder viewHolder = new ApproveActivity.ListApproveAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListApproveAdapter.ViewHolder holder, int position) {
            holder.idQues.setText(dataList.get(position).getAsset_receive_id());
            holder.textQues.setText(dataList.get(position).getPart_name());
            holder.txtPart.setText(dataList.get(position).getNotes());
            if(dataList.get(position).getValue().equals("1")){
                boolean isChecked = true;
                holder.chkPart.setChecked(isChecked);
            }

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(statFotoKTP == true){
                        imageKtp = fixencodedImageKtp;
                    }else if(statFotoKTP == false){
                        imageKtp = linkKTP;
                    }

                    txtPICName =  appName.getText().toString();
                    txtPICTitle = appTitle.getText().toString();
                    txtPICEmail = appEmail.getText().toString();
                    stnkExp = appstnkDate.getText().toString();
                    stnkTaxDate = appstnkTax.getText().toString();
                    kmCar = appKm.getText().toString();
                    carCondition = appAssetCondition.getText().toString();

                    JSONObject foto1 = new JSONObject();
                    try {
                        if(statFoto1 == true) {
                            foto1.put("id", "1");
                            foto1.put("file_photo", fixencodedImageF1);
                        }else if(statFoto1 == false){
                            foto1.put("id", "1");
                            foto1.put("file_photo", linkFoto1);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto2 = new JSONObject();
                    try {
                        if(statFoto2 == true) {
                            foto2.put("id", "2");
                            foto2.put("file_photo", fixencodedImageF2);
                        }else if(statFoto2 == false){
                            foto2.put("id", "2");
                            foto2.put("file_photo", linkFoto2);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto3 = new JSONObject();
                    try {
                        if(statFoto3 == true) {
                            foto3.put("id", "3");
                            foto3.put("file_photo", fixencodedImageF3);
                        }else if(statFoto3 == false){
                            foto3.put("id", "3");
                            foto3.put("file_photo", linkFoto3);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto4 = new JSONObject();
                    try {
                        if(statFoto4 == true) {
                            foto4.put("id", "4");
                            foto4.put("file_photo", fixencodedImageF4);
                        }else if(statFoto4 == false){
                            foto4.put("id", "4");
                            foto4.put("file_photo", linkFoto4);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto5 = new JSONObject();
                    try {
                        if(statFoto5 == true) {
                            foto5.put("id", "5");
                            foto5.put("file_photo", fixencodedImageF5);
                        }else if(statFoto5 == false){
                            foto5.put("id", "5");
                            foto5.put("file_photo", linkFoto5);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONArray jsonArray = new JSONArray();

                    jsonArray.put(foto1);
                    jsonArray.put(foto2);
                    jsonArray.put(foto3);
                    jsonArray.put(foto4);
                    jsonArray.put(foto5);

                    JSONArray jsonChek = new JSONArray();
                    for (int i = 0; i < dataList.size(); i++){
                        View view = listViewPart.getChildAt(i);
                        EditText notes = (EditText) view.findViewById(R.id.partEdt);
                        TextView ids = (TextView) view.findViewById(R.id.idPart_);
                        CheckBox ceks = (CheckBox) view.findViewById(R.id.partChk);

                        String cek;
                        if(ceks.isChecked()){
                            cek = "1";
                        }else{
                            cek = "0";
                        }

                        String note = notes.getText().toString();
                        String not;
                        if(note.matches("")){
                            not = "-";
                        }else{
                            not = note;
                        }
                        String id = ids.getText().toString();

                        JSONObject part = new JSONObject();
                        try {
                            part.put("id", id);
                            part.put("value",cek);
                            part.put("notes",not);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        jsonChek.put(part);
                    }

                    Log.e("dataapprove",imageKtp.toString());

                    sendApprove(idreceive,txtPICName,txtPICTitle,txtPICEmail,stnkExp,stnkTaxDate,kmCar,carCondition,imageKtp,jsonArray.toString(),jsonChek.toString());
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private void sendApprove(final String idreceive, final String txtPICName, final String txtPICTitle, final String txtPICEmail,
                             final String stnkExp, final String stnkTaxDate, final String kmCar, final String carCondition,
                             final String imageKtp, final String jsonArray, final String jsonChek){

        String tag_string_req = "req_senddata";

        pDialog.setMessage("Loading ...");
        showDialog();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("id", idreceive);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("stnk_exp_date", stnkExp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("stnk_exp_tax_notice", stnkTaxDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("km", kmCar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("asset_condition", carCondition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("pic_delivery_photo_identity", imageKtp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("pic_delivery_name",txtPICName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("pic_delivery_title", txtPICTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("pic_delivery_email",txtPICEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("parts",jsonChek);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("images",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("postparam",postparams.toString());

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_APPROVE_DATA,postparams,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Send Response: " + response.toString());
                        hideDialog();

                        try {
                            //JSONObject jObj = new JSONObject(response);
                            Log.d(TAG, "obj: " + response.toString());
                            String error = response.getString("status");
                            Log.d(TAG, "obj: " + error);
                            // Check for error node in json

                            // Error in login. Get the error message
                            String errorMsg = response.getString("message");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();

                            // Launch main activity
                            Intent intent = new Intent(ApproveActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Send Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Send Failed", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                headers.put("Content-Type","application/json");
                Log.e(TAG, "token: " + token);
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
