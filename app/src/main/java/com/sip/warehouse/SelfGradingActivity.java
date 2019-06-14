package com.sip.warehouse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class SelfGradingActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private static final String TAG = SelfGradingActivity.class.getSimpleName();
    RecyclerView listGradingPart;
    private ProgressDialog pDialog;
    private Button btnSimpan, btnCancel;
    Intent intent;
    String token, asset_type, id;
    private GradingAdapter mListadapter;
    private TextView idGrading,assetDesc,platKendaraan,manYear,kmGrading,warna,chassisNo,machineNo,receiveDate;
    JSONArray jsonChek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_grading);

        Fabric.with(this, new Crashlytics());

        listGradingPart = (RecyclerView) findViewById(R.id.part_self_grading);
        btnSimpan = (Button) findViewById(R.id.gradingsimpan);
        btnCancel = (Button) findViewById(R.id.gradingCancel);
        idGrading = (TextView) findViewById(R.id.idGrading);
        assetDesc = (TextView) findViewById(R.id.assetDesc);
        platKendaraan = (TextView) findViewById(R.id.platKendaraan);
        manYear = (TextView) findViewById(R.id.manYear);
        kmGrading = (TextView) findViewById(R.id.kmGrading);
        warna = (TextView) findViewById(R.id.warna);
        chassisNo = (TextView) findViewById(R.id.chassisNo);
        machineNo = (TextView) findViewById(R.id.machineNo);
        receiveDate = (TextView) findViewById(R.id.receiveDate);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        listGradingPart.setNestedScrollingEnabled(false);
        listGradingPart.setLayoutFrozen(true);

        // SqLite database handler
        db = new SQLiteHandler(this.getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        HashMap<String,String> grading = db.getGradingDetails();
        String db_id = grading.get("id_select");
        String db_kik = grading.get("kik");
        String db_asset_desc = grading.get("asset_desc");
        String db_plat = grading.get("plat");
        String db_man_year = grading.get("man_year");
        String db_colour = grading.get("colour");
        String db_chasis = grading.get("chasis");
        String db_machine = grading.get("machine");
        String db_receive_date = grading.get("receive_date");
        String db_asset_type = grading.get("asset_type");

        Intent intent = getIntent();
        id = db_id;
        String kik = db_kik;
        String asset_desc = db_asset_desc;
        String plat = db_plat;
        String man_year = db_man_year;
        String colour = db_colour;
        String chasis = db_chasis;
        String machine = db_machine;
        String receive_date = db_receive_date;
        asset_type = db_asset_type;

        idGrading.setText(kik);
        assetDesc.setText(asset_desc);
        platKendaraan.setText(plat);
        manYear.setText(man_year);
        kmGrading.setText("");
        warna.setText(colour);
        chassisNo.setText(chasis);
        machineNo.setText(machine);
        receiveDate.setText(receive_date);

        loadQuestionList();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCheck(id);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Cancel for back to list", Toast.LENGTH_SHORT).show();
    }

    private void cancelCheck(final String idwarehouse) {

        String tag_string_req = "req_cancel";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CANCEL_GRADING, new Response.Listener<String>() {

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
                        db.deleteGrading();
                        Intent intent = new Intent(SelfGradingActivity.this,
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
                Intent intent = new Intent(SelfGradingActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idwarehouse);
                Log.e(TAG, "id: " + idwarehouse);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE2+"?asset_type="+asset_type+"&inspection_name=Self Grading",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(SelfGradingActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listGradingPart.setLayoutManager(layoutManager);
                            //now looping through all the elements of the json array
                            Log.e("data parts",queArray.toString());
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataQuestionGrading(
                                                queObject.getString("id"),
                                                queObject.getString("part_name"),
                                                queObject.getString("part_code"),
                                                queObject.getString("amount"),
                                                queObject.getString("percentage")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }

                            mListadapter = new GradingAdapter(data);
                            listGradingPart.setAdapter(mListadapter);

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

    public class GradingAdapter extends RecyclerView.Adapter<GradingAdapter.ViewHolder>
    {
        private ArrayList<DataQuestionGrading> dataList;


        public GradingAdapter(ArrayList<DataQuestionGrading> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textQues;
            TextView idQues;
            TextView persenQue;
            EditText txtPart;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textQues = (TextView) itemView.findViewById(R.id.gradingPart);
                this.idQues = (TextView) itemView.findViewById(R.id.idGrading);
                this.persenQue = (TextView) itemView.findViewById(R.id.persenGrading);
                this.txtPart = (EditText) itemView.findViewById(R.id.noteGrading);
            }
        }

        @Override
        public GradingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grading, parent, false);

            GradingAdapter.ViewHolder viewHolder = new GradingAdapter.ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final GradingAdapter.ViewHolder holder, final int position)
        {

            holder.idQues.setText(dataList.get(position).getIdQue());
            holder.persenQue.setText(dataList.get(position).getPercentase());
            holder.textQues.setText(dataList.get(position).getPart_name());

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jsonChek = new JSONArray();
                    for (int i = 0; i < dataList.size(); i++) {
                        View view = listGradingPart.getChildAt(i);
                        EditText notes = (EditText) view.findViewById(R.id.noteGrading);
                        TextView ids = (TextView) view.findViewById(R.id.idGrading);
                        TextView persens = (TextView) view.findViewById(R.id.persenGrading);
                        Spinner kondisi = (Spinner) view.findViewById(R.id.kondisiGrading);

                        String persen = persens.getText().toString();
                        String pilihkondisi = kondisi.getSelectedItem().toString();

                        String cek;

                        if (pilihkondisi.equals("OK")) {
                            cek = "1";
                        } else if (pilihkondisi.equals("RUSAK RINGAN")) {
                            cek = "2";
                        } else if (pilihkondisi.equals("RUSAK BERAT")){
                            cek = "3";
                        } else {
                            cek = "4";
                        }

                        String note = notes.getText().toString();
                        String not;
                        if (note.matches("")) {
                            not = "-";
                        } else {
                            not = note;
                        }
                        String id = ids.getText().toString();

                        JSONObject part = new JSONObject();
                        try {
                            part.put("id", id);
                            part.put("condition", cek);
                            part.put("notes", not);
                            part.put("percentage", persen);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        jsonChek.put(part);
                    }

                    Log.e("json1", jsonChek.toString());


                    new AlertDialog.Builder(SelfGradingActivity.this)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    sendChecklist(id,jsonChek.toString());
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

    private void sendChecklist(String id, String jsonChek){

        String tag_string_req = "req_senddata";

        pDialog.setMessage("Loading ...");
        showDialog();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postparams.put("parts",jsonChek);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("postparam",postparams.toString());
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_POST_GRADING,postparams,
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
                            db.deleteGrading();
                            Intent intent = new Intent(SelfGradingActivity.this,
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
