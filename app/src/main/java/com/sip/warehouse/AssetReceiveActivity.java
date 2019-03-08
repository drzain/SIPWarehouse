package com.sip.warehouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
    ListView listViewPart,listViewCam;
    private List<DataQuestionReceive> quesList = new ArrayList<DataQuestionReceive>();
    private ListQueViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_receive);

        listViewPart = (ListView) findViewById(R.id.part_receive);
        listViewCam = (ListView) findViewById(R.id.camera_receive);

        loadQuestionList();
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

                            //now looping through all the elements of the json array
                            for (int i = 0; i < queArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject queObject = queArray.getJSONObject(i);
                                DataQuestionReceive question = new DataQuestionReceive();
                                question.setQuestion(queObject.getString("question"));
                                Log.d(TAG,"data" + queObject);
                                //Log.d(TAG, "object "+ queObject.getString("question"));
                                //creating a hero object and giving them the values from json object
                                //DataQuestionReceive que = new DataQuestionReceive(queObject.getString("id"), queObject.getString("question"), queObject.getString("description"),queObject.getString("type"));
                                Log.d(TAG,"dataqes" + question);

                                //adding the hero to herolist
                                quesList.add(question);
                            }

                            adapter.notifyDataSetChanged();

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
