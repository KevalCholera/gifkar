package com.smartsense.gifkar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.smartsense.gifkar.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRemindersActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etMyReminderAddDate, etMyReminderAddTime, etMyReminderName, etMyReminderAddRelation, etMyReminderAddRelationType, etMyReminderAddDescription;
    Button btnAddReminder;
    ImageView btBack;
    Switch switchMyReminder;
    JSONObject reminderObj;
    RadioButton rbMyReminder1Day, rbMyReminder2Day, rbMyReminder1Hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        try {
            if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYREMINDER) {
                titleTextView.setText(getResources().getString(R.string.screen_edit_reminder));
                reminderObj = new JSONObject(getIntent().getStringExtra("Reminder"));
            } else
                titleTextView.setText(getResources().getString(R.string.screen_add_reminder));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        rbMyReminder1Day = (RadioButton) findViewById(R.id.rbMyReminder1Day);
        rbMyReminder2Day = (RadioButton) findViewById(R.id.rbMyReminder2Day);
        rbMyReminder1Hour = (RadioButton) findViewById(R.id.rbMyReminder1Hour);
        switchMyReminder = (Switch) findViewById(R.id.switchMyReminder);
        etMyReminderAddDate = (EditText) findViewById(R.id.etMyReminderAddDate);
        etMyReminderAddTime = (EditText) findViewById(R.id.etMyReminderAddTime);
        etMyReminderName = (EditText) findViewById(R.id.etMyReminderName);
        etMyReminderAddRelation = (EditText) findViewById(R.id.etMyReminderAddRelation);
        etMyReminderAddRelationType = (EditText) findViewById(R.id.etMyReminderAddRelationType);
        etMyReminderAddDescription = (EditText) findViewById(R.id.etMyReminderAddDescription);
        btnAddReminder = (Button) findViewById(R.id.btnMyReminderAdd);
        btnAddReminder.setOnClickListener(this);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYREMINDER) {
            try {
                btnAddReminder.setText(getResources().getString(R.string.update));
                String[] parts = reminderObj.optString("reminderDate").split(" ");
                etMyReminderAddDate.setText(parts[0]);
                etMyReminderAddTime.setText(parts[1]);
                etMyReminderAddRelationType.setText(reminderObj.optJSONObject("occasion").optString("name"));
                etMyReminderAddRelation.setText(reminderObj.optString("relation"));
                etMyReminderName.setText(reminderObj.optString("name"));
                etMyReminderAddDescription.setText(reminderObj.optString("description"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMyReminderAdd:

                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
