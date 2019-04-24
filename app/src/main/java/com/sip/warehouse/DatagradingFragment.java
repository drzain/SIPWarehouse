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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DatagradingFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private TextView jmlInspection;
    private RecyclerView mRecyclerView;
    private DatagradingFragment.ListAdapter mListadapter;
    EditText searchHome;
    String token;
    String item;
    Spinner mySpinner;
    private ArrayList<DataNote> arraylist = new ArrayList<DataNote>();
    boolean isLoading = false;
    //String[] categories={"All","Mobil","Motor"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datagrading, container, false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerGrading);
        /*searchHome = (EditText) view.findViewById(R.id.searchGrading);*/
        mySpinner = (Spinner) view.findViewById(R.id.categoryGrading);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        /*ArrayList data = new ArrayList<DataNote>();
        for (int i = 0; i < DataNoteImformation.id.length; i++)
        {
            DataNote wp = new DataNote(
                    DataNoteImformation.id[i],
                    DataNoteImformation.branchArray[i],
                    DataNoteImformation.assetcodeArray[i],
                    DataNoteImformation.customerArray[i],
                    DataNoteImformation.licenseplateArray[i],
                    DataNoteImformation.typeKendaraan[i]
            );
            arraylist.add(wp);
        }

        mListadapter = new ListAdapter(arraylist);
        mRecyclerView.setAdapter(mListadapter);*/


        loadDataGrading();

        //mListadapter.filter("All");
        /*mySpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories));*/
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {

                if(position != 0) {
                    if(mySpinner.getItemAtPosition(position).toString() != null) {
                        item = adapterView.getItemAtPosition(position).toString();
                        mListadapter.filter(item);
                    }
                }
                //Toast.makeText(getActivity(), "Selected"+item, Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*searchHome.addTextChangedListener(new TextWatcher() {
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
        });*/

        return view;
    }

    public void loadDataGrading(){

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_GRADING_LIST,
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
                            ArrayList data = new ArrayList<DataQuestionReceive>();
                            for (int i = 0; i < queArray.length(); i++) {
                                JSONObject queObject = queArray.getJSONObject(i);
                                data.add(
                                        new DataGrading(
                                                queObject.getString("company_id"),
                                                queObject.getString("warehouse_id"),
                                                queObject.getString("company_code"),
                                                queObject.getString("branch_name"),
                                                queObject.getString("kik"),
                                                queObject.getString("agreement_no"),
                                                queObject.getString("customer_name"),
                                                queObject.getString("asset_description"),
                                                queObject.getString("asset_type"),
                                                queObject.getString("manufacturing_year"),
                                                queObject.getString("license_plate"),
                                                queObject.getString("colour"),
                                                queObject.getString("serial_no_1"),
                                                queObject.getString("serial_no_2"),
                                                queObject.getString("repossession_date"),
                                                queObject.getString("days_overdue"),
                                                queObject.getString("last_installment_no"),
                                                queObject.getString("tenor"),
                                                queObject.getString("collector_name"),
                                                queObject.getString("first_unit_status"),
                                                queObject.getString("status_date"),
                                                queObject.getString("osprincipal"),
                                                queObject.getString("amount_to_be_paid"),
                                                queObject.getString("notes"),
                                                queObject.getString("inventory_date"),
                                                queObject.getString("inventory_amount"),
                                                queObject.getString("unit_status"),
                                                queObject.getString("asset_status"),
                                                queObject.getString("asset_doc_status"),
                                                queObject.getString("days_of_reposses"),
                                                queObject.getString("days_of_inventory"),
                                                queObject.getString("wh_received_date"),
                                                queObject.getString("days_of_wh_received"),
                                                queObject.getString("current_location"),
                                                queObject.getString("process_status")
                                        )
                                );
                                //getting the json object of the particular index inside the array

                            }
                            mListadapter = new DatagradingFragment.ListAdapter(data);
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

    public class ListAdapter extends RecyclerView.Adapter<DatagradingFragment.ListAdapter.ViewHolder>
    {
        private ArrayList<DataGrading> dataList;
        private List<DataGrading> filterlist = null;

        public ListAdapter(ArrayList<DataGrading> data)
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
            TextView textViewType;
            Button btnGrading;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.idWarehouse = (TextView) itemView.findViewById(R.id.grading_warehous);
                this.textViewAssetDetail = (TextView) itemView.findViewById(R.id.grading_assetDetail);
                this.textViewAgreement = (TextView) itemView.findViewById(R.id.grading_agreementNo);
                this.textViewCustomer = (TextView) itemView.findViewById(R.id.grading_customername);
                this.textViewLicense = (TextView) itemView.findViewById(R.id.grading_licenseplate);
                this.textViewType = (TextView) itemView.findViewById(R.id.grading_type);
                this.btnGrading = (Button) itemView.findViewById(R.id.gradingBtn);
            }
        }

        @Override
        public DatagradingFragment.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_grading, parent, false);

            DatagradingFragment.ListAdapter.ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DatagradingFragment.ListAdapter.ViewHolder holder, final int position)
        {
            holder.textViewAgreement.setText(filterlist.get(position).getAgreement_no());
            holder.textViewCustomer.setText(filterlist.get(position).getCustomer_name());
            holder.textViewAssetDetail.setText(filterlist.get(position).getBranch_name());
            holder.textViewLicense.setText(filterlist.get(position).getLicense_plate());
            holder.textViewType.setText(filterlist.get(position).getAsset_type());

            holder.btnGrading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),
                            GradingActivity.class);
                    intent.putExtra("kik",filterlist.get(position).getKik());
                    intent.putExtra("asset_desc",filterlist.get(position).getAsset_description());
                    intent.putExtra("plat",filterlist.get(position).getLicense_plate());
                    intent.putExtra("man_year",filterlist.get(position).getManufacturing_year());
                    intent.putExtra("colour",filterlist.get(position).getColour());
                    intent.putExtra("chasis",filterlist.get(position).getSerial_no_1());
                    intent.putExtra("machine",filterlist.get(position).getSerial_no_2());
                    intent.putExtra("receive_date",filterlist.get(position).getInventory_date());
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
            if (charText.equals("all")) {
                filterlist.addAll(dataList);
            }
            else
            {
                for (DataGrading wp : dataList)
                {
                    if (wp.getAsset_type().toLowerCase().contains(charText.toLowerCase())||wp.getCustomer_name().toLowerCase().contains(charText.toLowerCase())||wp.getLicense_plate().toLowerCase().contains(charText.toLowerCase())
                            ||wp.getBranch_name().toLowerCase().contains(charText.toLowerCase())||wp.getAgreement_no().toLowerCase().contains(charText.toLowerCase()))
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
