package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.MyRemindersAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class MyRemindersActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvMyReminder;
    LinearLayout llReminder;
    private ImageView btBack, btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reminders);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_my_reminders));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        llReminder = (LinearLayout) findViewById(R.id.llMyReminders);
        llReminder.setOnClickListener(this);
        lvMyReminder = (ListView) findViewById(R.id.lvMyReminders);
        String temp = "{\"eventId\":123,\"errorCode\":0,\"status\":200,\"message\":\"Reminders.\",\"data\":{\"reminders\":[{\"id\":\"2\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}},{\"id\":\"3\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}},{\"id\":\"4\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}}]}}\n";
        try {
            JSONObject address = new JSONObject(temp);
            myAddressFill(address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMyReminders:
                startActivity(new Intent(this, AddRemindersActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void myAddressFill(JSONObject address) {
        MyRemindersAdapter myRemindersAdapter = null;
        try {
            myRemindersAdapter = new MyRemindersAdapter(this, address.getJSONObject("data").getJSONArray("reminders"), true);
            lvMyReminder.setAdapter(myRemindersAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
