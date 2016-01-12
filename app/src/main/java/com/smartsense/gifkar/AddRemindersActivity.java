package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CountryCodeAdapter;
import com.smartsense.gifkar.receivers.AlarmReceiver;
import com.smartsense.gifkar.utill.AlarmUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.DateAndTimeUtil;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddRemindersActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    EditText etMyReminderAddDate, etMyReminderAddTime, etMyReminderName, etMyReminderAddRelation, etMyReminderAddRelationType, etMyReminderAddDescription;
    Button btnAddReminder;
    ImageView btBack;
    SwitchCompat switchMyReminder;
    JSONObject reminderObj;
    RadioButton rbMyReminder1Day, rbMyReminder2Day, rbMyReminder1Hour;
    Dialog alert;
    private RadioGroup rbMyReminderGroup;

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
        mCalendar = Calendar.getInstance();
        rbMyReminderGroup = (RadioGroup) findViewById(R.id.rbMyReminderGroup);
        rbMyReminder1Day = (RadioButton) findViewById(R.id.rbMyReminder1Day);
        rbMyReminder1Day.setTag(Constants.ScreenReminderCode.ONE_DAY);
        rbMyReminder2Day = (RadioButton) findViewById(R.id.rbMyReminder2Day);
        rbMyReminder2Day.setTag(Constants.ScreenReminderCode.TWO_DAY);
        rbMyReminder1Hour = (RadioButton) findViewById(R.id.rbMyReminder1Hour);
        rbMyReminder1Hour.setTag(Constants.ScreenReminderCode.ONE_HOUR);
        switchMyReminder = (SwitchCompat) findViewById(R.id.switchMyReminder);
        etMyReminderAddDate = (EditText) findViewById(R.id.etMyReminderAddDate);
        etMyReminderAddDate.setOnClickListener(this);
        etMyReminderAddTime = (EditText) findViewById(R.id.etMyReminderAddTime);
        etMyReminderAddTime.setOnClickListener(this);
        etMyReminderName = (EditText) findViewById(R.id.etMyReminderName);
        etMyReminderAddRelation = (EditText) findViewById(R.id.etMyReminderAddRelation);
        etMyReminderAddRelationType = (EditText) findViewById(R.id.etMyReminderAddRelationType);
        etMyReminderAddRelationType.setOnClickListener(this);
        etMyReminderAddDescription = (EditText) findViewById(R.id.etMyReminderAddDescription);
        btnAddReminder = (Button) findViewById(R.id.btnMyReminderAdd);
        btnAddReminder.setOnClickListener(this);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYREMINDER) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s", Locale.ENGLISH);
                mCalendar.setTime(sdf.parse(reminderObj.optString("reminderDate")));
                btnAddReminder.setText(getResources().getString(R.string.update));
                String[] parts = reminderObj.optString("reminderDate").split(" ");
                etMyReminderAddDate.setText(parts[0]);
                etMyReminderAddTime.setText(parts[1]);
                etMyReminderAddRelationType.setText(reminderObj.optJSONObject("occasion").optString("name"));
                etMyReminderAddRelationType.setTag(reminderObj.optJSONObject("occasion").optString("id"));
                etMyReminderAddRelation.setText(reminderObj.optString("relation"));
                etMyReminderName.setText(reminderObj.optString("name"));
                etMyReminderAddDescription.setText(reminderObj.optString("description"));
                switchMyReminder.setChecked(reminderObj.optInt("isActive") == 1 ? true : false);
                switch (reminderObj.getInt("alertTime")){
                    case 1:
                        rbMyReminder1Day.setChecked(true);
                        break;
                    case 2:
                        rbMyReminder2Day.setChecked(true);
                        break;
                    case 3:
                        rbMyReminder1Hour.setChecked(true);
                        break;
                    default:
                        rbMyReminder1Day.setChecked(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private Calendar mCalendar = null;
    final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    final SimpleDateFormat tf = new SimpleDateFormat("HH:mm");

    public void timePicker() {
        TimePickerDialog TimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                mCalendar.set(Calendar.MINUTE, minute);
                etMyReminderAddTime.setText(tf.format(mCalendar.getTime()));
//                etMyReminderAddTime.setText(DateAndTimeUtil.toStringReadableTime(mCalendar, getApplicationContext()));
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        TimePicker.show();
    }

    public void datePicker() {
        DatePickerDialog DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etMyReminderAddDate.setText(df.format(mCalendar.getTime()));
//                etMyReminderAddDate.setText(DateAndTimeUtil.toStringReadableDate(mCalendar));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        DatePicker.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMyReminderAdd:
                saveReminder();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.etMyReminderAddTime:
                timePicker();
                break;
            case R.id.etMyReminderAddDate:
                datePicker();
                break;
            case R.id.etMyReminderAddRelationType:
                openOccasionsPopup();
                break;
            default:
        }
    }

    public void openOccasionsPopup() {
        String tempary = "{\"eventId\":123,\"errorCode\":0,\"status\":200,\"message\":\"Occasion list.\",\"data\":{\"occasions\":[{\"id\":1,\"name\":\"Birthday\"}]}}";
        try {
            JSONObject response = new JSONObject(tempary);
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            TextView tvCityDialogHead = (TextView) dialog.findViewById(R.id.tvCityDialogHead);
            tvCityDialogHead.setText("Select Occasion");
            CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(this, response.getJSONObject("data").getJSONArray("occasions"), false);
            list_view.setAdapter(countryCodeAdapter);

            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                    JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                    etMyReminderAddRelationType.setText(getCodeObj.optString("name"));
                    etMyReminderAddRelationType.setTag(getCodeObj.optString("id"));
                    alert.dismiss();

                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveReminder() {
        if (TextUtils.isEmpty(etMyReminderAddDate.getText().toString())) {
            CommonUtil.alertBox(this, "", "Please Selete Date");
        } else if (TextUtils.isEmpty(etMyReminderAddTime.getText().toString())) {
            CommonUtil.alertBox(this, "", "Please Selete Time");
        } else if (TextUtils.isEmpty(etMyReminderName.getText().toString())) {
            etMyReminderName.setError(getString(R.string.wrn_name));
        } else if (TextUtils.isEmpty(etMyReminderAddRelation.getText().toString())) {
            etMyReminderAddRelation.setError(getString(R.string.wrn_relation));
        } else if (TextUtils.isEmpty(etMyReminderAddRelationType.getText().toString())) {
            CommonUtil.alertBox(this, "", "Please Selete Relation Type");
        } else if (TextUtils.isEmpty(etMyReminderAddDescription.getText().toString())) {
            etMyReminderAddDescription.setError(getString(R.string.wrn_desc));
        } else {
            int selectedId = rbMyReminderGroup.getCheckedRadioButtonId();
            RadioButton rB = (RadioButton) findViewById(selectedId);
            int alertTime = (int) rB.getTag();
            int isActive = switchMyReminder.isChecked() ? 1 : 0;
//            int isActive =0;
            final String tag = "ReminderAdd";
            String url;
            Map<String, String> params = new HashMap<String, String>();
            if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYREMINDER) {
                url = Constants.BASE_URL + "/mobile/reminder/update";
                params.put("reminderId", reminderObj.optString("id"));
                params.put("flag", "all");
                params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE_REMINDER));
            }else {
                url = Constants.BASE_URL + "/mobile/reminder/create";
                params.put("eventId", String.valueOf(Constants.Events.EVENT_ADD_REMINDER));
            }
            params.put("name", etMyReminderName.getText().toString());
            params.put("relation", etMyReminderAddRelation.getText().toString());
            params.put("description", etMyReminderAddDescription.getText().toString());
            params.put("isActive", "" + isActive);
            params.put("alertTime", "" + alertTime);
            params.put("time", new SimpleDateFormat("H:m:s").format(mCalendar.getTime()));
            params.put("date", new SimpleDateFormat("y-M-d").format(mCalendar.getTime()));
            params.put("occasionId", (String) etMyReminderAddRelationType.getTag());
            params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
            params.put("defaultToken", Constants.DEFAULT_TOKEN);
            Log.i("params", params.toString());
            //In Response
            try {
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                int reminderId = 0;
//                if (switchMyReminder.isChecked()) {
                    JSONObject reminderObj = new JSONObject();
                    reminderObj.put("name", etMyReminderName.getText().toString());
                    reminderObj.put("relation", etMyReminderAddRelation.getText().toString());
                    reminderObj.put("desription", etMyReminderAddDescription.getText().toString());
                    reminderObj.put("title", etMyReminderAddRelationType.getText().toString());
                    Calendar mCalendar1 = mCalendar;
                    switch ((int) rB.getTag()) {
                        case 1:
                            mCalendar1.add(Calendar.DAY_OF_MONTH, -1);
                            break;
                        case 2:
                            mCalendar1.add(Calendar.DAY_OF_MONTH, -2);
                            break;
                        case 3:
                            mCalendar1.add(Calendar.HOUR_OF_DAY, -1);
                            break;
                        default:
                    }
                    AlarmUtil.setAlarm(getApplicationContext(), alarmIntent, reminderId, reminderObj, mCalendar);
                    AlarmUtil.setAlarm(getApplicationContext(), alarmIntent, reminderId, reminderObj, mCalendar1);
//                    Log.i("date", DateAndTimeUtil.toStringReadableDate(mCalendar1));
//                    Log.i("time", DateAndTimeUtil.toStringReadableTime(mCalendar1, getApplicationContext()));
//                }else{
//                    AlarmUtil.cancelAlarm(getApplicationContext(), alarmIntent, reminderId);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CommonUtil.showProgressDialog(this, "Wait...");
            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
        }

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getApplicationContext(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_ADD_REMINDER:
                            alert.setTitle("Success!");
                            alert.setMessage("Reminder Successfully Added.");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            });
                            alert.show();
                            break;
                        case Constants.Events.EVENT_UPDATE_REMINDER:
                            alert.setTitle("Success!");
                            alert.setMessage("Reminder Successfully Updated.");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            });
                            alert.show();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
