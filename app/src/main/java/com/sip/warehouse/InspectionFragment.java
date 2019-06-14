package com.sip.warehouse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class InspectionFragment extends Fragment {

    private TextView welcome;
    private TextView jmlInspection;
    private SQLiteHandler db;
    String token;
    private RecyclerView mRecyclerView;
    private ListApproveAdapter mListadapter;
    EditText searchApproval;
    String jml_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspection, container, false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        welcome = (TextView) view.findViewById(R.id.txtWelcome2);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerApprove);
        searchApproval = (EditText) view.findViewById(R.id.searchApproval);
        jmlInspection = (TextView) view.findViewById(R.id.txtJmlInspection2);

        welcome.setText("Welcome, "+name +"!");

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        /*ArrayList data = new ArrayList<DataNote>();
        for (int i = 0; i < DataNoteImformation.id.length; i++)
        {
            data.add(
                    new DataNote
                            (
                                    DataNoteImformation.id[i],
                                    DataNoteImformation.branchArray[i],
                                    DataNoteImformation.assetcodeArray[i],
                                    DataNoteImformation.customerArray[i],
                                    DataNoteImformation.licenseplateArray[i]
                            ));
        }

        mListadapter = new InspectionFragment.ListAdapter(data);
        mRecyclerView.setAdapter(mListadapter);*/

        loadApproveList();

        searchApproval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchApproval.getText().toString().toLowerCase(Locale.getDefault());
                try {
                    mListadapter.filter(text);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return view;
    }

    public void loadApproveList(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_ASSET_APPROVE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("response",response);
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray queArray = obj.getJSONArray("data");
                            if(queArray.length() > 0) {
                                jml_data = String.valueOf(queArray.length());
                            }else{
                                jml_data = "0";
                            }
                            Log.e("queArray",jml_data);
                            //now looping through all the elements of the json array
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataApproveReceive(
                                                queObject.getString("asset_receive_id"),
                                                queObject.getString("agreement_no"),
                                                queObject.getString("customer_name"),
                                                queObject.getString("branch_name"),
                                                queObject.getString("asset_description"),
                                                queObject.getString("license_plate"),
                                                queObject.getString("manufacturing_year")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            jmlInspection.setText("Saat ini anda memiliki "+jml_data+" Approval Data.");
                            mListadapter = new ListApproveAdapter(data);
                            mRecyclerView.setAdapter(mListadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        try {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception x){
                            x.printStackTrace();
                        }
                    }
                }){

            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public class ListApproveAdapter extends RecyclerView.Adapter<ListApproveAdapter.ViewHolder>
    {
        private ArrayList<DataApproveReceive> dataList;
        private List<DataApproveReceive> filterlist = null;

        public ListApproveAdapter(ArrayList<DataApproveReceive> data)
        {
            this.dataList = data;
            this.filterlist = new ArrayList(dataList);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView appViewBranch;
            TextView appViewAsset;
            TextView appViewCustomer;
            TextView appViewLicense;
            TextView appViewId;
            Button btnApprove;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.appViewBranch = (TextView) itemView.findViewById(R.id.appbranch);
                this.appViewAsset = (TextView) itemView.findViewById(R.id.appassetcode);
                this.appViewCustomer = (TextView) itemView.findViewById(R.id.appcustomername);
                this.appViewLicense = (TextView) itemView.findViewById(R.id.applicenseplate);
                this.appViewId = (TextView) itemView.findViewById(R.id.id_receive);
                this.btnApprove = (Button) itemView.findViewById(R.id.approveBtn);
            }
        }

        @Override
        public ListApproveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerapprove_item, parent, false);

            ListApproveAdapter.ViewHolder viewHolder = new ListApproveAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListApproveAdapter.ViewHolder holder, final int position)
        {
            Log.e("error",filterlist.get(position).getLicense_plate());
            holder.appViewBranch.setText(filterlist.get(position).getBranch_name());
            holder.appViewAsset.setText(filterlist.get(position).getAsset_description());
            holder.appViewCustomer.setText(filterlist.get(position).getCustomer_name());
            holder.appViewLicense.setText(filterlist.get(position).getLicense_plate());

            holder.btnApprove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(),
                            ApproveActivity.class);
                    intent.putExtra("name",filterlist.get(position).getCustomer_name());
                    intent.putExtra("code",filterlist.get(position).getAgreement_no());
                    intent.putExtra("plat",filterlist.get(position).getLicense_plate());
                    intent.putExtra("desc",filterlist.get(position).getAsset_description());
                    intent.putExtra("year",filterlist.get(position).getManufacturing_year());
                    intent.putExtra("idreceive",filterlist.get(position).getAsset_receive_id());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return filterlist.size();
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            filterlist.clear();
            if (charText.length() == 0) {
                filterlist.addAll(dataList);
            }
            else
            {
                for (DataApproveReceive wp : dataList)
                {
                    if (wp.getCustomer_name().toLowerCase().contains(charText.toLowerCase())||wp.getAgreement_no().toLowerCase().contains(charText.toLowerCase())
                            ||wp.getAsset_description().toLowerCase().contains(charText.toLowerCase())||wp.getLicense_plate().toLowerCase().contains(charText.toLowerCase()))
                    {
                        //Toast.makeText(getActivity(), "data " + wp.getCustomer() , Toast.LENGTH_SHORT).show();
                        filterlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

}
