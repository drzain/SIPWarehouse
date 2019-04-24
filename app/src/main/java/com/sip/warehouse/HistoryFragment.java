package com.sip.warehouse;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private TextView welcome;
    private TextView jmlInspection;
    private RecyclerView mRecyclerView;
    private HistoryFragment.ListAdapter mListadapter;
    EditText searchHome;
    String token;
    private ArrayList<DataNote> arraylist = new ArrayList<DataNote>();
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerHistory);
        searchHome = (EditText) view.findViewById(R.id.searchHistory);

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
                    DataNoteImformation.licenseplateArray[i]
            );
            arraylist.add(wp);
        }*/

        populateData();
        initAdapter();
        initScrollListener();

        //mListadapter = new ListAdapter(arraylist);
        //mRecyclerView.setAdapter(mListadapter);

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

    private void populateData() {
        for (int i = 0; i < 5; i++)
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
    }

    private void initAdapter() {

        mListadapter = new ListAdapter(arraylist);
        mRecyclerView.setAdapter(mListadapter);
    }

    private void initScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arraylist.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        arraylist.add(null);
        mListadapter.notifyItemInserted(arraylist.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arraylist.remove(arraylist.size() - 1);
                int scrollPosition = arraylist.size();
                mListadapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 5;
                Log.e("currentSize",""+currentSize);
                Log.e("nextLimit",""+nextLimit);
                if(nextLimit <= DataNoteImformation.id.length) {
                    while (currentSize + 1 <= nextLimit) {
                        DataNote wp2 = new DataNote(
                                DataNoteImformation.id[currentSize],
                                DataNoteImformation.branchArray[currentSize],
                                DataNoteImformation.assetcodeArray[currentSize],
                                DataNoteImformation.customerArray[currentSize],
                                DataNoteImformation.licenseplateArray[currentSize],
                                DataNoteImformation.typeKendaraan[currentSize]
                        );
                        arraylist.add(wp2);
                        currentSize++;
                    }
                }else {
                    while (currentSize + 1 <= DataNoteImformation.id.length) {
                        DataNote wp2 = new DataNote(
                                DataNoteImformation.id[currentSize],
                                DataNoteImformation.branchArray[currentSize],
                                DataNoteImformation.assetcodeArray[currentSize],
                                DataNoteImformation.customerArray[currentSize],
                                DataNoteImformation.licenseplateArray[currentSize],
                                DataNoteImformation.typeKendaraan[currentSize]
                        );
                        arraylist.add(wp2);
                        currentSize++;
                    }
                }
                mListadapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    public static class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private ArrayList<DataNote> dataList;
        private List<DataNote> filterlist = null;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        public ListAdapter(ArrayList<DataNote> data)
        {
            this.dataList = data;
            this.filterlist = new ArrayList(dataList);
        }

        /*public class ViewHolder extends RecyclerView.ViewHolder
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
        }*/

        private class LoadingViewHolder extends RecyclerView.ViewHolder {

            ProgressBar progressBar;

            public LoadingViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.loading);
            }
        }

        @Override
        public int getItemViewType (int position) {
            if(isPositionFooter (position)) {
                return VIEW_TYPE_LOADING;
            }
            return VIEW_TYPE_ITEM;
        }

        private boolean isPositionFooter (int position) {
            return position == filterlist.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
                return new ItemViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_loading, parent, false);
                return new LoadingViewHolder(view);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView idWarehouse;
            TextView textViewAssetDetail;
            TextView textViewAgreement;
            TextView textViewCustomer;
            TextView textViewLicense;
            Button btnReceive;

            public ItemViewHolder(@NonNull View itemView) {
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
        {
            if (holder instanceof ItemViewHolder) {
                populateItemRows((ItemViewHolder) holder, position);
            } else if (holder instanceof LoadingViewHolder) {
                showLoadingView((LoadingViewHolder) holder, position);
            }
        }

        private void showLoadingView(LoadingViewHolder viewHolder, int position) {
            //ProgressBar would be displayed

        }

        private void populateItemRows(ItemViewHolder viewHolder, int position) {

            viewHolder.textViewAgreement.setText(filterlist.get(position).getAssetcode());
            viewHolder.textViewCustomer.setText(filterlist.get(position).getCustomer());
            viewHolder.textViewAssetDetail.setText(filterlist.get(position).getBranch());
            viewHolder.textViewLicense.setText(filterlist.get(position).getLisenceplate());

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
                    if (wp.getCustomer().toLowerCase().contains(charText.toLowerCase())||wp.getLisenceplate().toLowerCase().contains(charText.toLowerCase())
                            ||wp.getBranch().toLowerCase().contains(charText.toLowerCase())||wp.getAssetcode().toLowerCase().contains(charText.toLowerCase()))
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
