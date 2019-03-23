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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class InspectionFragment extends Fragment {


    private SQLiteHandler db;
    private TextView welcome;
    private TextView jmlInspection;
    private RecyclerView mRecyclerView;
    private InspectionFragment.ListAdapter mListadapter;
    EditText searchApproval;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspection, container, false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerApprove);
        searchApproval = (EditText) view.findViewById(R.id.searchApproval);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList data = new ArrayList<DataNote>();
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
        mRecyclerView.setAdapter(mListadapter);

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
                mListadapter.filter(text);
            }
        });

        return view;
    }

    public class ListAdapter extends RecyclerView.Adapter<InspectionFragment.ListAdapter.ViewHolder>
    {
        private ArrayList<DataNote> dataList;
        private List<DataNote> filterlist = null;

        public ListAdapter(ArrayList<DataNote> data)
        {
            this.dataList = data;
            this.filterlist = new ArrayList(dataList);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewBranch;
            TextView textViewAsset;
            TextView textViewCustomer;
            TextView textViewLicense;
            Button btnReceive;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewBranch = (TextView) itemView.findViewById(R.id.branch);
                this.textViewAsset = (TextView) itemView.findViewById(R.id.assetcode);
                this.textViewCustomer = (TextView) itemView.findViewById(R.id.customername);
                this.textViewLicense = (TextView) itemView.findViewById(R.id.licenseplate);
                this.btnReceive = (Button) itemView.findViewById(R.id.receiveBtn);
            }
        }

        @Override
        public InspectionFragment.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerapprove_item, parent, false);

            InspectionFragment.ListAdapter.ViewHolder viewHolder = new InspectionFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(InspectionFragment.ListAdapter.ViewHolder holder, final int position)
        {
            holder.textViewBranch.setText(filterlist.get(position).getBranch());
            holder.textViewAsset.setText(filterlist.get(position).getAssetcode());
            holder.textViewCustomer.setText(filterlist.get(position).getCustomer());
            holder.textViewLicense.setText(filterlist.get(position).getLisenceplate());

            holder.btnReceive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(getActivity(),
                            AssetReceiveActivity.class);
                    startActivity(intent);*/
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
                for (DataNote wp : dataList)
                {
                    if (wp.getCustomer().toLowerCase().contains(charText.toLowerCase()))
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
