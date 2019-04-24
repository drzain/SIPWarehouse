package com.sip.warehouse;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AssetReceiveActivity extends AppCompatActivity {

    private static final String TAG = AssetReceiveActivity.class.getSimpleName();
    RecyclerView listViewPart,listViewCam;
    private ListAdapter mListadapter;
    private ListAdapter2 bListadapter;
    private TextView txtCustomer,txtMobil,txtPlat;
    private ImageView viewFoto;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_receive);

        listViewPart = (RecyclerView) findViewById(R.id.part_receive);
        listViewCam = (RecyclerView) findViewById(R.id.camera_receive);
        txtCustomer = (TextView) findViewById(R.id.customerName);
        txtMobil = (TextView) findViewById(R.id.txtMobil);
        txtPlat = (TextView) findViewById(R.id.txtPlat);
        viewFoto = (ImageView) findViewById(R.id.viewFoto);

        Intent intent=getIntent();
        String customerName = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        String platNumber = intent.getStringExtra("plat");
        String description = intent.getStringExtra("desc");
        String year = intent.getStringExtra("year");

        txtCustomer.setText(customerName+" - "+code);
        txtPlat.setText(platNumber);
        txtMobil.setText(description+" - "+year);


        loadQuestionList();
        loadCameraList();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(AssetReceiveActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listViewPart.setLayoutManager(layoutManager);
                            //now looping through all the elements of the json array
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataQuestionReceive(
                                                queObject.getString("id"),
                                                queObject.getString("question"),
                                                queObject.getString("description")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            mListadapter = new ListAdapter(data);
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

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private ArrayList<DataQuestionReceive> dataList;

        public ListAdapter(ArrayList<DataQuestionReceive> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textQues;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textQues = (TextView) itemView.findViewById(R.id.partQue);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partreceive, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {

            /*if(dataList.get(position).getType().equals("part")) {
                holder.textQues.setText(dataList.get(position).getQuestion());
            }*/

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    public void loadCameraList(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_CAMERA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(AssetReceiveActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listViewCam.setLayoutManager(layoutManager);
                            //now looping through all the elements of the json array
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataQuestionReceive(
                                                queObject.getString("id"),
                                                queObject.getString("question"),
                                                queObject.getString("description")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            bListadapter = new ListAdapter2(data);
                            listViewCam.setAdapter(bListadapter);

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

    public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ViewHolder>
    {
        private ArrayList<DataQuestionReceive> dataList;
        private ArrayList<DataQuestionReceive> dataListFiltered;

        public ListAdapter2(ArrayList<DataQuestionReceive> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textCam;
            ImageView btnCam;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textCam = (TextView) itemView.findViewById(R.id.txtQueCam);
                this.btnCam = (ImageView) itemView.findViewById(R.id.imgCamera);
            }
        }

        @Override
        public ListAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_camerareceive, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter2.ViewHolder holder, final int position)
        {

            /*if(dataList.get(position).getType().equals("foto")) {
                holder.textCam.setText(dataList.get(position).getQuestion());
                holder.btnCam.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        //Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                        //Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        //startActivityForResult(i, position);
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            captureImage(position);
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    }
                });
            }*/

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


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

                            if (type == MEDIA_TYPE_IMAGE) {
                                Intent intent = new Intent(AssetReceiveActivity.this,
                                        AssetReceiveActivity.class);
                                startActivity(intent);
                            }

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

    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage(Integer position) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Saving stored image path to saved instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                previewCapturedImage();
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

    /**
     * Display image from gallery
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            viewFoto.setVisibility(View.VISIBLE);

            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            viewFoto.setImageBitmap(bitmap);

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
                        CameraUtils.openSettings(AssetReceiveActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }



}
