package com.sip.warehouse;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    String token, idwarehouse, txtPICName, txtPICTitle, txtPICEmail;
    EditText txtName,txtTitle,txtEmail;
    String encodedImageKtp, encodedImageF1,encodedImageF2,encodedImageF3,encodedImageF4,encodedImageF5;
    Intent intent;
    Uri fileUri;
    Bitmap bitmap, decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

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

        Intent intent=getIntent();
        String customerName = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        String platNumber = intent.getStringExtra("plat");
        String description = intent.getStringExtra("desc");
        String year = intent.getStringExtra("year");
        idwarehouse = intent.getStringExtra("idwarehouse");

        txtCustomer.setText(customerName+" - "+code);
        txtPlat.setText(platNumber);
        txtMobil.setText(description+" - "+year);


        loadQuestionList();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

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

                viewKtp.setVisibility(View.VISIBLE);
                viewKtp.setImageBitmap(decoded);
            }else if(tipe == CAMERA_SATU){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                viewFoto1.setVisibility(View.VISIBLE);
                viewFoto1.setImageBitmap(decoded);
            }else if(tipe == CAMERA_DUA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF2 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                viewFoto2.setVisibility(View.VISIBLE);
                viewFoto2.setImageBitmap(decoded);
            }else if(tipe == CAMERA_TIGA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF3 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                viewFoto3.setVisibility(View.VISIBLE);
                viewFoto3.setImageBitmap(decoded);
            }else if(tipe == CAMERA_EMPAT){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                String encodedImageF4 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                viewFoto4.setVisibility(View.VISIBLE);
                viewFoto4.setImageBitmap(decoded);
            }else if(tipe == CAMERA_LIMA){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                byte[] byteArray = bytes.toByteArray();
                encodedImageF5 = Base64.encodeToString(byteArray, Base64.DEFAULT);

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE,
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
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataQuestionReceive(
                                                queObject.getString("id"),
                                                queObject.getString("category_id"),
                                                queObject.getString("part_name"),
                                                queObject.getString("notes")
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
                        foto1.put("file_photo",encodedImageF1);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto2 = new JSONObject();
                    try {
                        foto2.put("id", "2");
                        foto2.put("file_photo",encodedImageF2);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto3 = new JSONObject();
                    try {
                        foto3.put("id", "3");
                        foto3.put("file_photo",encodedImageF3);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto4 = new JSONObject();
                    try {
                        foto1.put("id", "4");
                        foto1.put("file_photo",encodedImageF4);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject foto5 = new JSONObject();
                    try {
                        foto1.put("id", "5");
                        foto1.put("file_photo",encodedImageF5);
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

                    JSONObject fotoObj = new JSONObject();
                    try {
                        fotoObj.put("images", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.e("datafoto",fotoObj.toString());
                    JSONArray jsonChek = new JSONArray();
                    ArrayList<String> list = new ArrayList<String>();
                    txtPICName =  txtName.getText().toString();
                    txtPICTitle = txtTitle.getText().toString();
                    txtPICEmail = txtEmail.getText().toString();

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

                    //Log.e("datatext",jsonChek.toString());

                   sendChecklist(idwarehouse,encodedImageKtp,txtPICName,txtPICTitle,txtPICEmail,jsonChek.toString(),jsonArray.toString());
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


    }

    private void sendChecklist(final String idwarehouse, final String encodedImageKtp, final String txtPICName, final String txtPICTitle, final String txtPICEmail, final String jsonChek, final String jsonArray){

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

            /*@Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("warehouse_order_id", idwarehouse);
                params.put("pic_delivery_photo_identity", encodedImageKtp);
                params.put("pic_delivery_name",txtPICName);
                params.put("pic_delivery_title", txtPICTitle);
                params.put("pic_delivery_email",txtPICEmail);
                params.put("parts",jsonChek);
                params.put("images",jsonArray);
                Log.e(TAG, "parameter: " + params);
                return params;
            }*/

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
