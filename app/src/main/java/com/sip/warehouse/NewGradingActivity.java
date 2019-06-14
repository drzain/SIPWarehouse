package com.sip.warehouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewGradingActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private static final String TAG = NewGradingActivity.class.getSimpleName();
    RecyclerView event_recycler_view_parent, grading_child;
    RecyclerView event_recycler_view_child;
    private ProgressDialog pDialog;
    private Button btnSimpan, btnCancel;
    Intent intent;
    String token, asset_type, id;
    private NewGradingActivity.ListAdapter mListadapter;
    private TextView idGrading,assetDesc,platKendaraan,manYear,kmGrading,warna,chassisNo,machineNo,receiveDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_grading);

        grading_child = (RecyclerView) findViewById(R.id.grading_child);
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

        event_recycler_view_parent = (RecyclerView) findViewById(R.id.category_utama);

        // SqLite database handler
        db = new SQLiteHandler(this.getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String kik = intent.getStringExtra("kik");
        String asset_desc = intent.getStringExtra("asset_desc");
        String plat = intent.getStringExtra("plat");
        String man_year = intent.getStringExtra("man_year");
        String colour = intent.getStringExtra("colour");
        String chasis = intent.getStringExtra("chasis");
        String machine = intent.getStringExtra("machine");
        String receive_date = intent.getStringExtra("receive_date");
        asset_type = intent.getStringExtra("asset_type");

        idGrading.setText(kik);
        assetDesc.setText(asset_desc);
        platKendaraan.setText(plat);
        manYear.setText(man_year);
        kmGrading.setText("");
        warna.setText(colour);
        chassisNo.setText(chasis);
        machineNo.setText(machine);
        receiveDate.setText(receive_date);

        /*ArrayList data = new ArrayList<DataCategory>();
        for (int i = 0; i < DataCategoryEx.idcat.length; i++)
        {
            data.add(
                    new DataCategory
                            (
                                    DataCategoryEx.idcat[i],
                                    DataCategoryEx.category[i]
                            ));
        }*/

        loadQuestionGrading();

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
                        Intent intent = new Intent(NewGradingActivity.this,
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
                Intent intent = new Intent(NewGradingActivity.this,
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

    public class ListAdapter extends RecyclerView.Adapter<NewGradingActivity.ListAdapter.MyViewHolder> {

        //private List<Movie> moviesList;

        private DataCategoryInformation dataCategoryInformation;
        private Activity activity;

        public ListAdapter(DataCategoryInformation dataCategoryInformation,Activity activity) {
            this.dataCategoryInformation = dataCategoryInformation;
            this.activity = activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_grading, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DataCategory dataCategory = dataCategoryInformation.getCategoryList().get(position);
            holder.event_list_parent_date.setText(dataCategory.getCategory());

            LinearLayoutManager hs_linearLayout = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            holder.event_recycler_view_child.setLayoutManager(hs_linearLayout);
            holder.event_recycler_view_child.setHasFixedSize(true);
            EventListChildAdapter eventListChildAdapter = new EventListChildAdapter(this.activity,dataCategoryInformation.getCategoryList().get(position).getQuestionList(),dataCategoryInformation);
            holder.event_recycler_view_child.setAdapter(eventListChildAdapter);

        }

        @Override
        public int getItemCount() {
            return dataCategoryInformation.getCategoryList().size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView event_list_parent_date;
            public RecyclerView event_recycler_view_child;

            public MyViewHolder(View view) {
                super(view);
                event_list_parent_date = (TextView) view.findViewById(R.id.title_category);
                event_recycler_view_child = (RecyclerView)view.findViewById(R.id.grading_child);
            }
        }


        public class EventListChildAdapter extends RecyclerView.Adapter<EventListChildAdapter.MyViewHolder> {

            private DataCategoryInformation dataCategory;
            private ArrayList<DataQuestionGrading> partsArrayList;
            private Activity activity;

            public EventListChildAdapter(Activity activity,ArrayList<DataQuestionGrading> partsArrayList, DataCategoryInformation dataCategory) {
                this.partsArrayList = partsArrayList;
                this.dataCategory = dataCategory;
                this.activity = activity;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_grading, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder,final int position) {
                final DataQuestionGrading dataQuestionGrading = partsArrayList.get(position);
                holder.idQue.setText(partsArrayList.get(position).getIdQue());
                holder.persenGrading.setText(partsArrayList.get(position).getPercentase());
                holder.gradingPart.setText(partsArrayList.get(position).getPart_name());
                holder.gradingPart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),
                                "Position "+position, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public int getItemCount() {
                return partsArrayList.size();
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                public TextView idQue;
                public TextView persenGrading;
                public TextView gradingPart;
                public Spinner kondisiGrading;
                public EditText noteGrading;


                public MyViewHolder(View view) {
                    super(view);
                    idQue = (TextView) view.findViewById(R.id.idGrading);
                    persenGrading = (TextView) view.findViewById(R.id.persenGrading);
                    gradingPart = (TextView) view.findViewById(R.id.gradingPart);
                    kondisiGrading = (Spinner) view.findViewById(R.id.kondisiGrading);
                    noteGrading = (EditText) view.findViewById(R.id.noteGrading);

                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            JSONArray jsonChek = new JSONArray();
                            for(int x = 0; x < dataCategory.getCategoryList().size(); x++) {
                                for (int i = 0; i < partsArrayList.size(); i++) {
                                    View z = event_recycler_view_child.getChildAt(i);
                                    TextView idChild = (TextView) z.findViewById(R.id.idGrading);
                                    TextView persenChild = (TextView) z.findViewById(R.id.persenGrading);
                                    Spinner kondisiChild = (Spinner) z.findViewById(R.id.kondisiGrading);
                                    EditText noteChild = (EditText) z.findViewById(R.id.noteGrading);

                                    String persen = persenChild.getText().toString();
                                    String pilihkondisi = kondisiChild.getSelectedItem().toString();

                                    String cek;

                                    if (pilihkondisi.equals("OKE")) {
                                        cek = "1";
                                    } else if (pilihkondisi.equals("BERMASALAH")) {
                                        cek = "2";
                                    } else {
                                        cek = "3";
                                    }

                                    String note = noteChild.getText().toString();
                                    String not;
                                    if (note.matches("")) {
                                        not = "-";
                                    } else {
                                        not = note;
                                    }
                                    String id = idChild.getText().toString();

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
                            }
                            Log.e("json1", jsonChek.toString());

                        }


                    });

                }
            }

        }
    }

    public void loadQuestionGrading(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE+"?asset_type="+asset_type+"&inspection_name=Self Grading",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<DataCategory> categoryArrayList;
                        DataCategoryInformation dataCategoryInformation = new DataCategoryInformation();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");
                            categoryArrayList = new ArrayList<>();
                            for (int i=0;i < queArray.length();i++){
                                DataCategory dataCategory = new DataCategory();
                                JSONObject categoryName = queArray.getJSONObject(i);
                                String category_name = categoryName.getString("category_name");
                                dataCategory.setCategory(category_name);
                                JSONArray jsonArrayparts = categoryName.getJSONArray("parts");
                                ArrayList<DataQuestionGrading> partsArrayList = new ArrayList<>();
                                for (int indexPart=0;indexPart<jsonArrayparts.length();indexPart++){
                                    DataQuestionGrading dataQuestionGrading = new DataQuestionGrading();
                                    JSONObject eventObj = jsonArrayparts.getJSONObject(indexPart);
                                    dataQuestionGrading.setIdQue(eventObj.getString("id"));
                                    dataQuestionGrading.setPart_code(eventObj.getString("part_code"));
                                    dataQuestionGrading.setPart_name(eventObj.getString("part_name"));
                                    dataQuestionGrading.setAmount(eventObj.getString("amount"));
                                    dataQuestionGrading.setPercentase(eventObj.getString("percentage"));
                                    partsArrayList.add(dataQuestionGrading);
                                }
                                dataCategory.setEventsArrayList(partsArrayList);
                                categoryArrayList.add(dataCategory);
                            }
                            dataCategoryInformation.setCategoryList(categoryArrayList);
                            Log.d("message",dataCategoryInformation.toString());

                            mListadapter = new ListAdapter(dataCategoryInformation,NewGradingActivity.this);
                            event_recycler_view_parent.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            event_recycler_view_parent.setLayoutManager(mLayoutManager);
                            event_recycler_view_parent.setItemAnimator(new DefaultItemAnimator());
                            event_recycler_view_parent.setAdapter(mListadapter);
                            //now looping through all the elements of the json array
                            //Log.e("data parts",queArray.toString());
                            //ArrayList data = new ArrayList<DataQuestionReceive>();
                            /*for (int i = 0; i < queArray.length(); i++) {
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

                            }*/

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

}
