package com.smartsense.gifkar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.receivers.AlarmReceiver;
import com.smartsense.gifkar.utill.AlarmUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyRemindersActivity extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    ListView lvMyReminder;
    TextView tvMyReminder;
    Button btnReminder;
    LinearLayout llMyReminder, llNoMyReminder;
    private ImageView btBack, btInfo;
    Fragment fragment = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_my_reminders, container, false);
//        setContentView(R.layout.activity_my_reminders);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View v = inflater.inflate(R.layout.action_bar_center, null);
//        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
//        titleTextView.setText(getResources().getString(R.string.screen_my_reminders));
//        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
//        btBack.setOnClickListener(this);
//        getSupportActionBar().setCustomView(v);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_my_reminders));
        actionBarTitle.setBackgroundColor(getActivity().getResources().getColor(R.color.mainColor));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        tvMyReminder = (TextView) view.findViewById(R.id.tvMyReminder);
        tvMyReminder.setOnClickListener(this);
        lvMyReminder = (ListView) view.findViewById(R.id.lvMyReminders);
        btnReminder = (Button) view.findViewById(R.id.btnReminder);
        btnReminder.setOnClickListener(this);
        llMyReminder = (LinearLayout) view.findViewById(R.id.llMyReminders);
        llNoMyReminder = (LinearLayout) view.findViewById(R.id.llNoReminder);
//        String temp = "{\"eventId\":123,\"errorCode\":0,\"status\":200,\"message\":\"Reminders.\",\"data\":{\"reminders\":[{\"id\":\"2\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}},{\"id\":\"3\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}},{\"id\":\"4\",\"name\":\"raju2\",\"relation\":\"buddy\",\"description\":\"some desc2\",\"reminderDate\":\"2015-01-01 12:30:00\",\"alertTime\":\"1\",\"occasion_id\":\"1\",\"isActive\":\"1\",\"occasion\":{\"id\":\"1\",\"name\":\"Birthday\"}}]}}\n";
//        try {
//            JSONObject address = new JSONObject(temp);
//            myAddressFill(address);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        getReminder();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMyReminder:
            case R.id.btnReminder:
                startActivityForResult(new Intent(getActivity(), AddRemindersActivity.class), 0);
                break;
//            case R.id.btActionBarBack:
//                finish();
//                break;
            default:
        }
    }

    public void myReminderFill(JSONObject address) {
        MyRemindersAdapter myRemindersAdapter = null;
        try {
            if (address.getJSONObject("data").getJSONArray("reminders").length() > 0) {
                llMyReminder.setVisibility(View.VISIBLE);
                llNoMyReminder.setVisibility(View.GONE);
                myRemindersAdapter = new MyRemindersAdapter(getActivity(), address.getJSONObject("data").getJSONArray("reminders"), true);
                lvMyReminder.setAdapter(myRemindersAdapter);
            } else {
                llMyReminder.setVisibility(View.GONE);
                llNoMyReminder.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void deleteReminder(String id) {
        final String tag = "delreminder";
        String url = Constants.BASE_URL + "/mobile/reminder/delete";
        Map<String, String> params = new HashMap<String, String>();
        params.put("reminderId", id);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_REMINDER));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getReminder() {
        final String tag = "reminder";
        String url = Constants.BASE_URL + "/mobile/reminder/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&reminder=all&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_REMINDER);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void onOffReminder(final Boolean check, final JSONObject reminderObj1) {
        int isActive = check ? 1 : 0;
        final String tag = "ReminderUpdate";
        String url;
        Map<String, String> params = new HashMap<String, String>();
        url = Constants.BASE_URL + "/mobile/reminder/update";
        params.put("reminderId", reminderObj1.optString("id"));
        params.put("flag", "onOff");
        params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE_REMINDER));
        params.put("isActive", "" + isActive);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        CommonUtil.cancelProgressDialog();
                        try {

                            Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                            int reminderId = reminderObj1.optInt("id");
                            if (check) {
                                alert.setTitle("Success!");
                                alert.setMessage("Reminder Successfully On.");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                alert.show();
                                JSONObject reminderObj = new JSONObject();
                                reminderObj.put("name", reminderObj1.optString("name"));
                                reminderObj.put("relation", reminderObj1.optString("relation"));
                                reminderObj.put("desription", reminderObj1.optString("description"));
                                reminderObj.put("title", reminderObj1.optJSONObject("occasion").optString("name"));
                                SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s", Locale.ENGLISH);
                                Calendar mCalendar = null;
                                Log.i("params", reminderObj1.toString());
                                mCalendar = Calendar.getInstance();
                                mCalendar.setTime(sdf.parse(reminderObj1.optString("reminderDate")));
                                Calendar mCalendar1 = mCalendar;
                                switch (reminderObj1.optInt("alertTime")) {
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
                                AlarmUtil.setAlarm(getActivity(), alarmIntent, reminderId, reminderObj, mCalendar);
                                AlarmUtil.setAlarm(getActivity(), alarmIntent, reminderId, reminderObj, mCalendar1);
                            } else {
                                alert.setTitle("Success!");
                                alert.setMessage("Reminder Successfully Off.");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                alert.show();
                                AlarmUtil.cancelAlarm(getActivity(), alarmIntent, reminderId);
                            }
//                    Log.i("date", DateAndTimeUtil.toStringReadableDate(mCalendar1));
//                    Log.i("time", DateAndTimeUtil.toStringReadableTime(mCalendar1, getApplicationContext()));
//                }else{
//                    AlarmUtil.cancelAlarm(getApplicationContext(), alarmIntent, reminderId);
//                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtil.cancelProgressDialog();
                Log.e("Volley Request Error", error.getLocalizedMessage());

            }

        });
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_GET_REMINDER:
                            myReminderFill(response);
                            break;
                        case Constants.Events.EVENT_DEL_REMINDER:
                            getReminder();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getReminder();
    }

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
            final JSONObject addressObj = dataArray.optJSONObject(position);

            String[] parts = addressObj.optString("reminderDate").split(" ");
            holder.tvDate.setText(parts[0]);
            holder.tvTime.setText(parts[1]);
            holder.tvName.setText(addressObj.optString("name"));
            holder.tvType.setText(addressObj.optJSONObject("occasion").optString("name") + " | " + addressObj.optString("relation"));// + " | " + CommonUtil.beforeAlaram(addressObj.optInt("relation"))
            holder.reminder.setChecked(addressObj.optInt("isActive") == 1 ? true : false);
//            holder.tvType.setText("");
            holder.ivDelete.setOnClickListener(this);
            holder.ivDelete.setTag(addressObj.toString());
            holder.ivEdit.setOnClickListener(this);
            holder.ivEdit.setTag(addressObj.toString());
            holder.reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onOffReminder(b, addressObj);
                }
            });
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
                            JSONObject objReminder = null;
                            try {
                                objReminder = new JSONObject((String) view.getTag());
                                deleteReminder(objReminder.optString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });
                    alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alertbox.show();
                    break;
                case R.id.ivMyReminderElementEdit:
                    fragment.startActivityForResult(new Intent(activity, AddRemindersActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_MYREMINDER).putExtra("Reminder", (String) view.getTag()), 0);
                    break;
            }
        }

    }


}
