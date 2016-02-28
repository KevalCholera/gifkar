package com.smartsense.gifkar.utill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mpt.storage.SharedPreferenceUtil;
import com.parse.ParsePushBroadcastReceiver;
import com.smartsense.gifkar.GifkarActivity;
import com.smartsense.gifkar.NotificationActivity;
import com.smartsense.gifkar.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ronak on 2/11/2016.
 */
public class CustomNotificationReceiver extends ParsePushBroadcastReceiver {

    NotificationCompat.Builder mBuilder;
    Intent resultIntent;
    public final static int mNotificationId = 1001;
    Uri notifySound;
    NotificationManager notificationManager;

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        // deactivate standard notification
        return null;
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        // Implement
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {

        JSONObject jsonObject = getDataFromIntent(intent);
        if (jsonObject.isNull("subject")) {
            Toast.makeText(context, "Notification Data Null", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, null))) {

                notifySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//                mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                mBuilder.setContentTitle(jsonObject.optString("subject"));
                // mBuilder.setContentText(alert);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,startAppropriateActivity(context, jsonObject.optString("subject"), jsonObject.optString("message"), jsonObject.optString("sentBy")), 0);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setContentText(jsonObject.optString("message"));
                mBuilder.setSound(notifySound);
                // mBuilder.addAction(R.drawable.accept, "Accept", pIntent);
                // mBuilder.addAction(R.drawable.cancel, "Cancel", pIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                notificationManager.notify(mNotificationId, mBuilder.build());
            }
        }
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getStringExtra("com.parse.Data"));
            Log.i("PUSH", data.toString());
        } catch (JSONException e) {
        }
        return data;
    }

    private Intent startAppropriateActivity(Context context, String subject, String message, String sentBy) {
        // TODO startAppropriateActivity
        Intent intent = null;
        switch (sentBy) {
            case "system":
                intent = new Intent(context, GifkarActivity.class).putExtra("subject", subject).putExtra("message", message).putExtra("check", true);
                break;
            case "admin":
                intent = new Intent(context, NotificationActivity.class).putExtra("subject", subject).putExtra("message", message).putExtra("check", true);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
        return intent;
    }

}

