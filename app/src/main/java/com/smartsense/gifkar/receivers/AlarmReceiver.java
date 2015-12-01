package com.smartsense.gifkar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.smartsense.gifkar.utill.NotificationUtil;

import org.json.JSONArray;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getIntExtra("NOTIFICATION_ID", 0);
        JSONArray reminderList=new JSONArray();
        NotificationUtil.createNotification(context, reminderList.optJSONObject(0));
        // Update lists in tab fragments
        updateLists(context);
    }

    public void updateLists(Context context) {
        Intent intent = new Intent("BROADCAST_REFRESH");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}