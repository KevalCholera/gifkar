package com.smartsense.gifkar.adapter;

/**
 * Created by Ronak on 02-12-2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.gifkar.AddRemindersActivity;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyRemindersAdapter extends BaseAdapter implements View.OnClickListener {
    JSONArray dataArray;
    private LayoutInflater inflater;
    Activity activity;

    public MyRemindersAdapter(Activity activity, JSONArray dataArray, Boolean check) {
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
            view = inflater.inflate(R.layout.element_my_reminders, parent, false);
            // TODO Auto-generated method stub
            holder.tvName = (TextView) view.findViewById(R.id.tvMyReminderElementName);
            holder.tvType = (TextView) view.findViewById(R.id.tvMyReminderElementType);
            holder.tvDate = (TextView) view.findViewById(R.id.tvMyReminderElementDate);
            holder.tvTime = (TextView) view.findViewById(R.id.tvMyReminderElementTime);
            holder.ivEdit = (ImageView) view.findViewById(R.id.ivMyReminderElementEdit);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivMyReminderElementDelete);
            holder.reminder = (SwitchCompat) view.findViewById(R.id.switchMyReminderElement);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject addressObj = dataArray.optJSONObject(position);
        String[] parts = addressObj.optString("reminderDate").split(" ");
        holder.tvDate.setText(parts[0]);
        holder.tvTime.setText(parts[1]);
        holder.tvName.setText(addressObj.optString("name"));
        holder.tvType.setText(addressObj.optJSONObject("occasion").optString("name") + " | " + addressObj.optString("relation"));// + " | " + CommonUtil.beforeAlaram(addressObj.optInt("relation"))
        holder.ivDelete.setOnClickListener(this);
        holder.ivEdit.setOnClickListener(this);
        holder.ivEdit.setTag(addressObj.toString());
        return view;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvType;
        TextView tvDate;
        TextView tvTime;
        SwitchCompat reminder;
        ImageView ivEdit;
        ImageView ivDelete;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ivMyReminderElementDelete:
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
            case R.id.ivMyReminderElementEdit:
                activity.startActivity(new Intent(activity, AddRemindersActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_MYREMINDER).putExtra("Reminder", (String) view.getTag()));
                break;
        }
    }

}
