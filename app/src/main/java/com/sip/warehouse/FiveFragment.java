package com.sip.warehouse;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiveFragment extends Fragment implements BlockingStep {

    private SQLiteHandler db;
    String token, asset_type;
    private RecyclerView mRecyclerView, listViewPart;
    private FiveFragment.ListAdapter mListadapter;
    private ArrayList<DataAssetReceive> arraylist = new ArrayList<DataAssetReceive>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five, container, false);
        listViewPart = (RecyclerView) view.findViewById(R.id.part_acce_mobil);
        // Inflate the layout for this fragment

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        Intent intent=getActivity().getIntent();
        String kik = intent.getStringExtra("kik");
        String asset_desc = intent.getStringExtra("asset_desc");
        String plat = intent.getStringExtra("plat");
        String man_year = intent.getStringExtra("man_year");
        String colour = intent.getStringExtra("colour");
        String chasis = intent.getStringExtra("chasis");
        String machine = intent.getStringExtra("machine");
        String receive_date = intent.getStringExtra("receive_date");
        asset_type = intent.getStringExtra("asset_type");


        loadQuestionList();
        // Inflate the layout for this fragment
        return view;
    }

    public void loadQuestionList(){
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE+"?category_name=accesories&asset_type="+asset_type+"&inspection_code=SLFGR",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");

                            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listViewPart.setLayoutManager(layoutManager);
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
                            mListadapter = new FiveFragment.ListAdapter(data);
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
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public class ListAdapter extends RecyclerView.Adapter<FiveFragment.ListAdapter.ViewHolder>
    {

        private ArrayList<DataQuestionGrading> dataList;
        private List<DataQuestionGrading> filterlist = null;

        public ListAdapter(ArrayList<DataQuestionGrading> data)
        {
            this.dataList = data;
            this.filterlist = new ArrayList(dataList);
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

        @NonNull
        @Override
        public FiveFragment.ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grading, parent, false);

            FiveFragment.ListAdapter.ViewHolder viewHolder = new FiveFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FiveFragment.ListAdapter.ViewHolder holder, int position) {

            holder.idQues.setText(dataList.get(position).getIdQue());
            holder.persenQue.setText(dataList.get(position).getPercentase());
            holder.textQues.setText(dataList.get(position).getPart_name());

        }

        @Override
        public int getItemCount() {
            return filterlist.size();
        }

    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //you can do anythings you want
                callback.goToNextStep();

            }
        }, 0L);// delay open another fragment,

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Toast.makeText(getActivity(), "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //you can do anythings you want
                callback.goToPrevStep();

            }
        }, 0L);// delay open another fragment,
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

}
