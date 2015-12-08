package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.NotificationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btBack;
    private LinearLayout llNotification;
    private ListView lvNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_notification));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_notification);
        lvNotification = (ListView) findViewById(R.id.lvNotification);
        llNotification = (LinearLayout) findViewById(R.id.llNotification);
        String temp = "{ \"eventId\" : 123,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"Address list.\", \"data\" :  { \"deliveryAddresses\" : [ { \"recipientName\" : \"raju bhai\",  \"recipientContact\" : \"98989898\", \"address\" : \"titanium city center\",  \"landmark\" : \"sachin tower\", \"isActive\" : \"1\",   \"area\" : { \"id\" : \"1\",   \"name\" : \"Prahlad nagar\" } },  { \"recipientName\" : \"raju bhai\",   \"recipientContact\" : \"98989898\",  \"address\" : \"titanium city center\",    \"landmark\" : \"sachin tower\",  \"isActive\" : \"1\",  \"area\" : { \"id\" : \"1\" , \"name\" : \"Prahlad nagar\" } } ] } }";
        try {
            JSONObject notification = new JSONObject(temp);
            notificationFill(notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void notificationFill(JSONObject notification) {
        NotificationAdapter notificationAdapter = null;
        try {
            notificationAdapter = new NotificationAdapter(this, notification.getJSONObject("data").getJSONArray("notifications"), true);
            lvNotification.setAdapter(notificationAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
