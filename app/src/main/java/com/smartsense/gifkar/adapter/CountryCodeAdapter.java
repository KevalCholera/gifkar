package com.smartsense.gifkar.adapter;

/**
 * Created by Ronak on 02-12-2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsense.gifkar.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class CountryCodeAdapter extends BaseAdapter {
    JSONArray dataArray;
    private LayoutInflater inflater;
    Boolean check;

    public CountryCodeAdapter(Context context, JSONArray dataArray, Boolean check) {
        this.check = check;
        this.dataArray = dataArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.element_country_code, parent, false);
            // TODO Auto-generated method stub
            holder.tvCountryCode = (TextView) view.findViewById(R.id.tvCountryCode);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject testJson = dataArray.optJSONObject(position);
//        Log.i("array", testJson.toString());
        if(check)
        holder.tvCountryCode.setText("+"+testJson.optString("code") + " " + testJson.optString("name"));
        else
            holder.tvCountryCode.setText(testJson.optString("name"));

        return view;
    }

    class ViewHolder {

        TextView tvCountryCode;

    }

}
