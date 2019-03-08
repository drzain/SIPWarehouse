package com.sip.warehouse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListQueViewAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataQuestionReceive> queItems;

    public ListQueViewAdapter(Activity activity, List<DataQuestionReceive> queItems) {
        this.activity = activity;
        this.queItems = queItems;
    }

    @Override
    public int getCount() {
        return queItems.size();
    }

    @Override
    public Object getItem(int location) {
        return queItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_asset_receive, null);

        TextView pertanyaan = (TextView) convertView.findViewById(R.id.partQue);


        // getting movie data for the row
        DataQuestionReceive m = queItems.get(position);

        // title
        pertanyaan.setText(m.getQuestion());

        return convertView;
    }

}
