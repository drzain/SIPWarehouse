package com.sip.warehouse;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssetReceiveActivity extends AppCompatActivity {

    private static final String TAG = AssetReceiveActivity.class.getSimpleName();
    RecyclerView listViewPart,listViewCam;
    private ListAdapter mListadapter;
    private ListAdapter2 bListadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_receive);

        listViewPart = (RecyclerView) findViewById(R.id.part_receive);
        listViewCam = (RecyclerView) findViewById(R.id.camera_receive);

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
                                                queObject.getString("description"),
                                                queObject.getString("type")
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

            if(dataList.get(position).getType().equals("part")) {
                holder.textQues.setText(dataList.get(position).getQuestion());
            }

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
                                                queObject.getString("description"),
                                                queObject.getString("type")
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

        public ListAdapter2(ArrayList<DataQuestionReceive> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textCam;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textCam = (TextView) itemView.findViewById(R.id.txtQueCam);
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

            if(dataList.get(position).getType().equals("foto")) {
                holder.textCam.setText(dataList.get(position).getQuestion());
            }

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }


}
