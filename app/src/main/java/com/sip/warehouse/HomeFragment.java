package com.sip.warehouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleAdapter;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private TextView welcome;
    private TextView jmlInspection;
    private RecyclerView mRecyclerView;
    private ListAdapter mListadapter;
    EditText searchHome;
    String token;
    String jml_data;
    private ArrayList<DataAssetReceive> arraylist = new ArrayList<DataAssetReceive>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        token = user.get("token");

        welcome = (TextView) view.findViewById(R.id.txtWelcome);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        searchHome = (EditText) view.findViewById(R.id.searchHome);
        jmlInspection = (TextView) view.findViewById(R.id.txtJmlInspection);

        //jml_data = 0;
        welcome.setText("Welcome, "+name +"!");


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //ArrayList data = new ArrayList<DataNote>();
        /*for (int i = 0; i < DataNoteImformation.id.length; i++)
        {
            DataNote wp = new DataNote(
                    DataNoteImformation.id[i],
                    DataNoteImformation.branchArray[i],
                    DataNoteImformation.assetcodeArray[i],
                    DataNoteImformation.customerArray[i],
                    DataNoteImformation.licenseplateArray[i]
            );
            arraylist.add(wp);
        }

        mListadapter = new ListAdapter(arraylist);
        mRecyclerView.setAdapter(mListadapter);*/

        loadAssetList();

        searchHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length()>0) {
                    String text = searchHome.getText().toString().toLowerCase(Locale.getDefault());
                    mListadapter.filter(text);
                }
            }
        });

        return view;
    }

    public void loadAssetList(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_ASSET_RECEIVE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
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
                            //now looping through all the elements of the json array
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataAssetReceive(
                                                queObject.getString("warehouse_order_id"),
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
                            jmlInspection.setText("Saat ini anda memiliki "+jml_data+" Data.");
                            mListadapter = new ListAdapter(data);
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

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private ArrayList<DataAssetReceive> dataList;
        private List<DataAssetReceive> filterlist = null;

        public ListAdapter(ArrayList<DataAssetReceive> data)
        {
            this.dataList = data;
            this.filterlist = new ArrayList(dataList);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView idWarehouse;
            TextView textViewAssetDetail;
            TextView textViewAgreement;
            TextView textViewCustomer;
            TextView textViewLicense;
            Button btnReceive;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.idWarehouse = (TextView) itemView.findViewById(R.id.id_warehous);
                this.textViewAssetDetail = (TextView) itemView.findViewById(R.id.assetDetail);
                this.textViewAgreement = (TextView) itemView.findViewById(R.id.agreementNo);
                this.textViewCustomer = (TextView) itemView.findViewById(R.id.customername);
                this.textViewLicense = (TextView) itemView.findViewById(R.id.licenseplate);
                this.btnReceive = (Button) itemView.findViewById(R.id.receiveBtn);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            holder.textViewAgreement.setText(filterlist.get(position).getAgreement_no());
            holder.textViewAssetDetail.setText(filterlist.get(position).getAsset_description() +" - "+filterlist.get(position).getManufacturing_year());
            holder.textViewCustomer.setText(filterlist.get(position).getCustomer_name());
            holder.textViewLicense.setText(filterlist.get(position).getLicense_plate());

            holder.btnReceive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                    //setFlagging(filterlist.get(position).getWarehouse_order_id());
                    Intent intent = new Intent(getActivity(),
                            ReceiveActivity.class);
                    intent.putExtra("name",filterlist.get(position).getCustomer_name());
                    intent.putExtra("code",filterlist.get(position).getAgreement_no());
                    intent.putExtra("plat",filterlist.get(position).getLicense_plate());
                    intent.putExtra("desc",filterlist.get(position).getAsset_description());
                    intent.putExtra("year",filterlist.get(position).getManufacturing_year());
                    intent.putExtra("idwarehouse",filterlist.get(position).getWarehouse_order_id());
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
                for (DataAssetReceive wp : dataList)
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

    private void setFlagging(final String idwarehouse) {

        String tag_string_req = "req_flagging";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FLAG_RECEIVE_ASSET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Flaging Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "obj: " + jObj.toString());
                    String error = jObj.getString("status");
                    Log.d(TAG, "obj: " + error);
                    // Check for error node in json
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Flagging Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Flagging Failed", Toast.LENGTH_LONG).show();
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

}
