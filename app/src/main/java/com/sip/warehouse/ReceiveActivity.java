package com.sip.warehouse;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import io.fabric.sdk.android.Fabric;

import static com.sip.warehouse.CameraUtils.getOutputMediaFile;

public class ReceiveActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private static final String TAG = ReceiveActivity.class.getSimpleName();
    private static final int CAMERA_KTP = 100;
    private static final int CAMERA_SATU = 200;
    private static final int CAMERA_DUA = 300;
    private static final int CAMERA_TIGA = 400;
    private static final int CAMERA_EMPAT = 500;
    private static final int CAMERA_LIMA = 600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private ProgressDialog pDialog;
    int bitmap_size = 40;
    int max_resolution_image = 200;
    private static String imageStoragePath;
    RecyclerView listViewPart,listViewCam;
    private ListAdapter mListadapter;
    private TextView txtCustomer,txtMobil,txtPlat;
    private ImageView viewFoto, viewKtp, btnCam1, btnCam2, btnCam3, btnCam4, btnCam5, viewFoto1, viewFoto2, viewFoto3, viewFoto4, viewFoto5;
    private Button btnSimpan;
    private Button btnCancel;
    private ImageButton btnKtp;
    private Spinner stnk_status, stnk_pemilik;
    DatePickerDialog datePickerDialog;
    String token, idwarehouse, txtPICName, txtPICTitle, txtPICEmail, stnkExp, stnkTaxDate, kmCar, carCondition, typeKendaraan, isStnk, stnkOwn;
    EditText txtName,txtTitle,txtEmail, stnkDate, stnktax, km, assetcondition;
    String encodedImageKtp, encodedImageF1,encodedImageF2,encodedImageF3,encodedImageF4,encodedImageF5;
    String fixencodedImageKtp, fixencodedImageF1,fixencodedImageF2,fixencodedImageF3,fixencodedImageF4,fixencodedImageF5;
    TextView txtPemilik, txtStnkExp, txtStnkTax;
    Intent intent;
    Uri fileUri;
    Bitmap bitmap, decoded;
    JSONArray jsonArray, jsonChek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        Fabric.with(this, new Crashlytics());

        listViewPart = (RecyclerView) findViewById(R.id.part_new_receive);
        txtCustomer = (TextView) findViewById(R.id.customerNewName);
        txtMobil = (TextView) findViewById(R.id.txtNewMobil);
        txtPlat = (TextView) findViewById(R.id.txtNewPlat);
        viewFoto = (ImageView) findViewById(R.id.viewFoto);
        btnSimpan = (Button) findViewById(R.id.receivesimpan);
        btnCancel = (Button) findViewById(R.id.receiveCancel);
        btnKtp = (ImageButton) findViewById(R.id.cmrNewKtp);
        viewKtp = (ImageView) findViewById(R.id.txtNewcmrKtp);
        viewFoto1 = (ImageView) findViewById(R.id.viewFoto1);
        viewFoto2 = (ImageView) findViewById(R.id.viewFoto2);
        viewFoto3 = (ImageView) findViewById(R.id.viewFoto3);
        viewFoto4 = (ImageView) findViewById(R.id.viewFoto4);
        viewFoto5 = (ImageView) findViewById(R.id.viewFoto5);
        btnCam1 = (ImageView) findViewById(R.id.imgCamera1);
        btnCam2 = (ImageView) findViewById(R.id.imgCamera2);
        btnCam3 = (ImageView) findViewById(R.id.imgCamera3);
        btnCam4 = (ImageView) findViewById(R.id.imgCamera4);
        btnCam5 = (ImageView) findViewById(R.id.imgCamera5);
        txtName = (EditText) findViewById(R.id.txtNewName);
        txtTitle = (EditText) findViewById(R.id.txtNewTitle);
        txtEmail = (EditText) findViewById(R.id.txtNewEmail);
        stnkDate = (EditText) findViewById(R.id.stnkExpDate);
        stnktax = (EditText) findViewById(R.id.stnkTaxDate);
        stnk_status = (Spinner) findViewById(R.id.spinner_stnk);
        stnk_pemilik = (Spinner) findViewById(R.id.spinner_pemilik);
        txtPemilik = (TextView) findViewById(R.id.txtPemilik);
        txtStnkExp = (TextView) findViewById(R.id.txtstnkexp);
        txtStnkTax = (TextView) findViewById(R.id.txtstnktax);
        km = (EditText) findViewById(R.id.km);
        assetcondition = (EditText) findViewById(R.id.assetCondition);

        listViewPart.setNestedScrollingEnabled(false);
        listViewPart.setLayoutFrozen(true);

        stnk_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = stnk_status.getSelectedItem().toString();
                if(status.equals("ADA")){
                    stnk_pemilik.setVisibility(View.VISIBLE);
                    stnkDate.setVisibility(View.VISIBLE);
                    stnktax.setVisibility(View.VISIBLE);
                    txtPemilik.setVisibility(View.VISIBLE);
                    txtStnkExp.setVisibility(View.VISIBLE);
                    txtStnkTax.setVisibility(View.VISIBLE);
                }else{
                    stnk_pemilik.setVisibility(View.GONE);
                    stnkDate.setVisibility(View.GONE);
                    stnktax.setVisibility(View.GONE);
                    txtPemilik.setVisibility(View.GONE);
                    txtStnkExp.setVisibility(View.GONE);
                    txtStnkTax.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stnkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ReceiveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                try{
                                    int month = monthOfYear + 1;
                                    String formattedMonth = "" + month;
                                    String formattedDayOfMonth = "" + dayOfMonth;

                                    if(month < 10){

                                        formattedMonth = "0" + month;
                                    }
                                    if(dayOfMonth < 10){

                                        formattedDayOfMonth = "0" + dayOfMonth;
                                    }
                                    stnkDate.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                                }catch (ParseException e1){
                                    e1.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        stnktax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ReceiveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if(month < 10){

                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                stnktax.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //txtPart = (EditText) findViewById(R.id.partEdt);
        //txtIdPart = (TextView) findViewById(R.id.idPart);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(this.getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        HashMap<String,String> receive = db.getReceiveDetails();
        String db_id = receive.get("id_warehouse");
        String db_name = receive.get("customer_name");
        String db_code = receive.get("code");
        String db_plat = receive.get("receive_plat");
        String db_desc = receive.get("receive_desc");
        String db_year = receive.get("year");
        String db_type = receive.get("receive_type");

        Log.e("receive",db_type+","+db_id);

        Intent intent=getIntent();
        String customerName = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        String platNumber = intent.getStringExtra("plat");
        String description = intent.getStringExtra("desc");
        String year = intent.getStringExtra("year");
        idwarehouse = db_id;
        typeKendaraan = db_type;

        txtCustomer.setText(db_name+" - "+db_code);
        txtPlat.setText(db_plat);
        txtMobil.setText(db_desc+" - "+db_year);


        loadQuestionList();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCheck(idwarehouse);
            }
        });

        btnKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_KTP);
            }
        });

        btnCam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_SATU);
            }
        });

        btnCam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_DUA);
            }
        });

        btnCam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_TIGA);
            }
        });

        btnCam4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_EMPAT);
            }
        });

        btnCam5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission(CAMERA_LIMA);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Cancel for back to list", Toast.LENGTH_SHORT).show();
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
                previewCapturedImage(bitmap,getResizedBitmap(bitmap, max_resolution_image),requestCode);
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
    private void previewCapturedImage(Bitmap bitmap, Bitmap bmp,int tipe) {
        try {

            if(tipe == CAMERA_KTP){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageKtp = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageKtp = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageKtp = "data:image/jpeg;base64," + encodedImageKtp.replace(" ", "").replace("\n", "");

                viewKtp.setVisibility(View.VISIBLE);
                viewKtp.setImageBitmap(decoded);
            }else if(tipe == CAMERA_SATU){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageF1 = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageF1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF1 = "data:image/jpeg;base64," + encodedImageF1.replace(" ", "").replace("\n", "");

                viewFoto1.setVisibility(View.VISIBLE);
                viewFoto1.setImageBitmap(decoded);
            }else if(tipe == CAMERA_DUA){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageF2 = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageF2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF2 = "data:image/jpeg;base64," + encodedImageF2.replace(" ", "").replace("\n", "");

                viewFoto2.setVisibility(View.VISIBLE);
                viewFoto2.setImageBitmap(decoded);
            }else if(tipe == CAMERA_TIGA){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageF3 = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageF3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF3 = "data:image/jpeg;base64," + encodedImageF3.replace(" ", "").replace("\n", "");

                viewFoto3.setVisibility(View.VISIBLE);
                viewFoto3.setImageBitmap(decoded);
            }else if(tipe == CAMERA_EMPAT){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageF4 = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageF4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF4 = "data:image/jpeg;base64," + encodedImageF4.replace(" ", "").replace("\n", "");

                viewFoto4.setVisibility(View.VISIBLE);
                viewFoto4.setImageBitmap(decoded);
            }else if(tipe == CAMERA_LIMA){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
                byte[] b = baos.toByteArray();
                encodedImageF5 = Base64.encodeToString(b,Base64.DEFAULT);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                //byte[] byteArray = bytes.toByteArray();
                //encodedImageF5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fixencodedImageF5 = "data:image/jpeg;base64," + encodedImageF5.replace(" ", "").replace("\n", "");

                viewFoto5.setVisibility(View.VISIBLE);
                viewFoto5.setImageBitmap(decoded);
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
                        CameraUtils.openSettings(ReceiveActivity.this);
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
                Log.d(TAG, "Oops! Failed create "
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


    private void cancelCheck(final String idwarehouse) {

        String tag_string_req = "req_cancel";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CANCEL_RECEIVE_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Cancel Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "obj: " + jObj.toString());
                    String error = jObj.getString("status");
                    Log.d(TAG, "obj: " + error);
                    // Check for error node in json
                    if (error != "1") {
                        // Launch main activity
                        db.deleteReceive();
                        Intent intent = new Intent(ReceiveActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cancel Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Cancel Failed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReceiveActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("warehouse_order_id", idwarehouse);
                Log.e(TAG, "warehouseid: " + idwarehouse);
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

    public void loadQuestionList(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE+"?asset_type="+typeKendaraan+"&inspection_name=Asset Receive",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(ReceiveActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listViewPart.setLayoutManager(layoutManager);
                            //now looping through all the elements of the json array
                            Log.e("data parts",queArray.toString());
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataQuestionReceive(
                                                queObject.getString("id"),
                                                queObject.getString("part_code"),
                                                queObject.getString("part_name")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            mListadapter = new ReceiveActivity.ListAdapter(data);
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
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public class ListAdapter extends RecyclerView.Adapter<ReceiveActivity.ListAdapter.ViewHolder>
    {
        private ArrayList<DataQuestionReceive> dataList;


        public ListAdapter(ArrayList<DataQuestionReceive> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textQues;
            TextView idQues;
            EditText txtPart;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textQues = (TextView) itemView.findViewById(R.id.partQue);
                this.idQues = (TextView) itemView.findViewById(R.id.idPart_);
                this.txtPart = (EditText) itemView.findViewById(R.id.partEdt);
            }
        }

        @Override
        public ReceiveActivity.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partreceive, parent, false);

            ReceiveActivity.ListAdapter.ViewHolder viewHolder = new ReceiveActivity.ListAdapter.ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final ReceiveActivity.ListAdapter.ViewHolder holder, final int position)
        {

            holder.idQues.setText(dataList.get(position).getId());
            holder.textQues.setText(dataList.get(position).getDescription());

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject foto1 = new JSONObject();
                    try {
                        foto1.put("id", "1");
                        foto1.put("file_photo",fixencodedImageF1);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto2 = new JSONObject();
                    try {
                        foto2.put("id", "2");
                        foto2.put("file_photo",fixencodedImageF2);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto3 = new JSONObject();
                    try {
                        foto3.put("id", "3");
                        foto3.put("file_photo",fixencodedImageF3);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto4 = new JSONObject();
                    try {
                        foto4.put("id", "4");
                        foto4.put("file_photo",fixencodedImageF4);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto5 = new JSONObject();
                    try {
                        foto5.put("id", "5");
                        foto5.put("file_photo",fixencodedImageF5);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    jsonArray = new JSONArray();

                    jsonArray.put(foto1);
                    jsonArray.put(foto2);
                    jsonArray.put(foto3);
                    jsonArray.put(foto4);
                    jsonArray.put(foto5);

                    JSONObject fotoObj = new JSONObject();
                    try {
                        fotoObj.put("images", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("datafoto",jsonArray.toString());
                    jsonChek = new JSONArray();
                    ArrayList<String> list = new ArrayList<String>();
                    txtPICName =  txtName.getText().toString();
                    txtPICTitle = txtTitle.getText().toString();
                    txtPICEmail = txtEmail.getText().toString();
                    stnkExp = stnkDate.getText().toString();
                    stnkTaxDate = stnktax.getText().toString();
                    kmCar = km.getText().toString();
                    carCondition = assetcondition.getText().toString();

                    isStnk = stnk_status.getSelectedItem().toString();
                    if(isStnk.equals("ADA")){
                        isStnk = "1";
                    }else{
                        isStnk = "0";
                    }
                    stnkOwn = stnk_pemilik.getSelectedItem().toString();
                    if(stnkOwn.equals("Individu")){
                        stnkOwn = "1";
                    }else{
                        stnkOwn = "2";
                    }

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

                    JSONObject chekObj = new JSONObject();
                    try {
                        chekObj.put("parts", jsonChek);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("datatext",jsonChek.toString());


                    new AlertDialog.Builder(ReceiveActivity.this)
                        .setTitle("Submit entry")
                        .setMessage("Are you sure you want to submit this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                sendChecklist(idwarehouse,stnkExp,stnkTaxDate,kmCar,carCondition,fixencodedImageKtp,txtPICName,txtPICTitle,txtPICEmail,jsonChek.toString(),jsonArray.toString(),isStnk,stnkOwn);

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                }
            });

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


    }

    private void sendChecklist(final String idwarehouse,final String stnkExp,final String stnkTaxDate,
                               final String kmCar,final String carCondition, final String encodedImageKtp,
                               final String txtPICName, final String txtPICTitle, final String txtPICEmail,
                               final String jsonChek, final String jsonArray, final String isStnk, final String stnkOwn){

        String tag_string_req = "req_senddata";

        pDialog.setMessage("Loading ...");
        showDialog();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("warehouse_order_id", idwarehouse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("is_stnk", isStnk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("stnk_status", stnkOwn);
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
            postparams.put("pic_delivery_photo_identity", encodedImageKtp);
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
                AppConfig.URL_POST_ASSET_RECEIVE,postparams,
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
                        db.deleteReceive();
                        Intent intent = new Intent(ReceiveActivity.this,
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
