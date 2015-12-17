package com.smartsense.gifkar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.smartsense.gifkar.utill.NotificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        JSONObject reminderList = null;
        try {
            reminderList = new JSONObject(intent.getStringExtra("NOTIFICATION_ID"));
            NotificationUtil.createNotification(context, reminderList);
            // Update lists in tab fragments
            updateLists(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateLists(Context context) {
        Intent intent = new Intent("BROADCAST_REFRESH");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}