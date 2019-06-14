package com.sip.warehouse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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


public class OneFragment extends Fragment implements BlockingStep {

    private SQLiteHandler db;
    String token, asset_type, pilihkondisi;
    private RecyclerView mRecyclerView, listViewPart;
    private OneFragment.ListAdapter mListadapter;
    private ArrayList<DataQuestionReceive> arraylist;
    JSONArray jsonChek1 = new JSONArray();
    Spinner kondisi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        listViewPart = (RecyclerView) view.findViewById(R.id.part_exte_mobil);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        //listViewPart.setNestedScrollingEnabled(false);


        /*final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);*/

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
        return view;
    }

    public void loadQuestionList(){
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_QUESTION_RECEIVE+"?category_name=exterior&asset_type="+asset_type+"&inspection_code=SLFGR",
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
                            mListadapter = new OneFragment.ListAdapter(data);
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

    public class ListAdapter extends RecyclerView.Adapter<OneFragment.ListAdapter.ViewHolder>
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
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grading, parent, false);

            OneFragment.ListAdapter.ViewHolder viewHolder = new OneFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder,final int position) {

            holder.idQues.setText(dataList.get(position).getIdQue());
            holder.persenQue.setText(dataList.get(position).getPercentase());
            holder.textQues.setText(dataList.get(position).getPart_name());
            /*ViewGroup rootView = (ViewGroup) getView();
            for (int i = 0; i < dataList.size(); i++){
                View view = rootView.getChildAt(i);
                EditText notes = (EditText) view.findViewById(R.id.noteGrading);
                TextView ids = (TextView) view.findViewById(R.id.idGrading);
                TextView persens = (TextView) view.findViewById(R.id.persenGrading);
                kondisi = (Spinner) view.findViewById(R.id.kondisiGrading);

                //String pilihkondisi = kondisi.getSelectedItem().toString();
                kondisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {

                        if(position != 0) {
                            if(kondisi.getItemAtPosition(position).toString() != null) {
                                pilihkondisi = adapterView.getItemAtPosition(position).toString();
                            }
                        }
                        //Toast.makeText(getActivity(), "Selected"+item, Toast.LENGTH_SHORT).show();


                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                String cek;

                if(pilihkondisi.equals("OKE")){
                    cek = "1";
                }else if (pilihkondisi.equals("BERMASALAH")){
                    cek = "2";
                }else{
                    cek = "3";
                }

                String note = notes.getText().toString();
                String not;
                if(note.matches("")){
                    not = "-";
                }else{
                    not = note;
                }
                String id = ids.getText().toString();
                String persen = persens.getText().toString();

                JSONObject part = new JSONObject();
                try {
                    part.put("id", id);
                    part.put("condition",not);
                    part.put("percentage",persen);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                jsonChek1.put(part);
            }*/

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
        //callback.getStepperLayout().showProgress("Send data in progress, please wait...");
        Log.e("cekdata",jsonChek1.toString());

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

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
