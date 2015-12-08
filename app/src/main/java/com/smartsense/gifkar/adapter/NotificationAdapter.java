package com.smartsense.gifkar.adapter;

/**
 * Created by Ronak on 02-12-2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.gifkar.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificationAdapter extends BaseAdapter implements View.OnClickListener {
    JSONArray dataArray;
    private LayoutInflater inflater;
    Activity activity;

    public NotificationAdapter(Activity activity, JSONArray dataArray, Boolean check) {
        this.activity = activity;
        this.dataArray = dataArray;
        inflater = LayoutInflater.from(activity);
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
            view = inflater.inflate(R.layout.element_my_address, parent, false);
            // TODO Auto-generated method stub
            holder.tvTitle = (TextView) view.findViewById(R.id.tvMyAddressElementName);
            holder.tvDec = (TextView) view.findViewById(R.id.tvMyAddressElementNo);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivMyAddressElementAddressDelete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject notificationObj = dataArray.optJSONObject(position);
        if (notificationObj.optString("isSeen").equals("0"))
            view.setBackgroundColor(activity.getResources().getColor(R.color.activity_bg));
        else
            view.setBackgroundColor(activity.getResources().getColor(R.color.textcolorwhite));
        holder.tvTitle.setText(notificationObj.optJSONObject("notification").optString("subject"));
        holder.tvDec.setText(notificationObj.optJSONObject("notification").optString("message"));
        holder.ivDelete.setOnClickListener(this);
        holder.ivDelete.setTag(notificationObj.toString());
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvDec;
        ImageView ivDelete;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ivMyAddressElementAddressDelete:
                AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                alertbox.setCancelable(true);
                alertbox.setMessage("Are you sure you want to delete ?");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        JSONObject objAddress = (JSONObject) view.getTag();
//                        String deleteId=objAddress.optString("");
                    }

                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alertbox.show();
                break;
        }
    }

}
