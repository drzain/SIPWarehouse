package com.sip.warehouse;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    private SQLiteHandler db;
    private TextView welcome;
    private TextView jmlInspection;
    private RecyclerView mRecyclerView;
    private ListAdapter mListadapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");

        welcome = (TextView) view.findViewById(R.id.txtWelcome);
        jmlInspection = (TextView) view.findViewById(R.id.txtJmlInspection);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        welcome.setText("Welcome, "+name +"!");
        jmlInspection.setText("Saat ini anda memiliki 6 inspection.");

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

        mListadapter = new ListAdapter(data);
        mRecyclerView.setAdapter(mListadapter);

        return view;
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private ArrayList<DataNote> dataList;

        public ListAdapter(ArrayList<DataNote> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewBranch;
            TextView textViewAsset;
            TextView textViewCustomer;
            TextView textViewLicense;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewBranch = (TextView) itemView.findViewById(R.id.branch);
                this.textViewAsset = (TextView) itemView.findViewById(R.id.assetcode);
                this.textViewCustomer = (TextView) itemView.findViewById(R.id.customername);
                this.textViewLicense = (TextView) itemView.findViewById(R.id.licenseplate);
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
            holder.textViewBranch.setText(dataList.get(position).getBranch());
            holder.textViewAsset.setText(dataList.get(position).getAssetcode());
            holder.textViewCustomer.setText(dataList.get(position).getCustomer());
            holder.textViewLicense.setText(dataList.get(position).getLisenceplate());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

}
