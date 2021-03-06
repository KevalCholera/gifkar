package com.smartsense.gifkar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
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
        getNotification();
        lvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                view.setBackgroundColor(NotificationActivity.this.getResources().getColor(R.color.activity_bg));
                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(NotificationActivity.this);
                alert.setTitle(getCodeObj.optJSONObject("notification").optString("subject"));
                alert.setMessage(getCodeObj.optJSONObject("notification").optString("message"));
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Do something here where "ok" clicked


                    }
                });
                alert.show();
                seenNotification(false,getCodeObj.optJSONObject("notification").optString("id"));
            }
        });
        if (getIntent().getBooleanExtra("check", false)) {
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
            alert.setTitle(getIntent().getStringExtra("subject"));
            alert.setMessage(getIntent().getStringExtra("message"));
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Do something here where "ok" clicked

                }
            });
            alert.show();
            seenNotification(false, getIntent().getStringExtra("id"));
        }
//        View view = (View) inflater.inflate(R.layout.activity_notification, container, false);
//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar_gifkar);
//        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
//        actionBarTitle.setText(getResources().getString(R.string.screen_notification));
//        actionBarTitle.setBackgroundColor(this.getResources().getColor(R.color.mainColor));
//        actionBarTitle.setClickable(false);
//        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
//        btFilter.setVisibility(View.INVISIBLE);
//        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
//        btSearch.setVisibility(View.INVISIBLE);
//        String temp = "{ \"eventId\" : 123,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"Address list.\", \"data\" :  { \"deliveryAddresses\" : [ { \"recipientName\" : \"raju bhai\",  \"recipientContact\" : \"98989898\", \"address\" : \"titanium city center\",  \"landmark\" : \"sachin tower\", \"isActive\" : \"1\",   \"area\" : { \"id\" : \"1\",   \"name\" : \"Prahlad nagar\" } },  { \"recipientName\" : \"raju bhai\",   \"recipientContact\" : \"98989898\",  \"address\" : \"titanium city center\",    \"landmark\" : \"sachin tower\",  \"isActive\" : \"1\",  \"area\" : { \"id\" : \"1\" , \"name\" : \"Prahlad nagar\" } } ] } }";
//        try {
//            JSONObject notification = new JSONObject(temp);
//            notificationFill(notification);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return view;
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
            if (notification.getJSONObject("data").getJSONArray("notifications").length() > 0) {
                lvNotification.setVisibility(View.VISIBLE);
                llNotification.setVisibility(View.GONE);
                notificationAdapter = new NotificationAdapter(this, notification.getJSONObject("data").getJSONArray("notifications"), true);
                lvNotification.setAdapter(notificationAdapter);
            } else {
                lvNotification.setVisibility(View.GONE);
                llNotification.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void deleteNotification(String id) {
        final String tag = "delNotification";
        String url = Constants.BASE_URL + "/mobile/userNotification/delete";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userNotificationId", id);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_NOTIFICATION));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void seenNotification(Boolean isSeen, String id) {
        final String tag = "seenNotification";
        String url = Constants.BASE_URL + "/mobile/userNotification/update";
        Map<String, String> params = new HashMap<String, String>();
        if (isSeen)
            params.put("isSeen", "all");
        else
            params.put("notificationId", id);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_SEEN_NOTIFICATION));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
//        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getNotification() {
        final String tag = "notification";
        String url = Constants.BASE_URL + "/mobile/userNotification/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_NOTIFICATION);
        if (!getIntent().getBooleanExtra("check", false))
            CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_GET_NOTIFICATION:
                            notificationFill(response);
                            break;
                        case Constants.Events.EVENT_DEL_NOTIFICATION:
                            getNotification();
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
                view = inflater.inflate(R.layout.element_notification, parent, false);
                // TODO Auto-generated method stub
                holder.tvTitle = (TextView) view.findViewById(R.id.tvNotificationElementName);
                holder.tvDec = (TextView) view.findViewById(R.id.tvNotificationElementNo);
                holder.tvDate = (TextView) view.findViewById(R.id.tvNotificationElementDate);
                holder.ivDelete = (ImageView) view.findViewById(R.id.ivNotificationElementDelete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            JSONObject notificationObj = dataArray.optJSONObject(position);
            if (notificationObj.optString("isSeen").equals("0"))
                view.setBackgroundColor(activity.getResources().getColor(R.color.activity_bg));
            else
                view.setBackgroundColor(activity.getResources().getColor(R.color.textcolorwhite));
//            if (!notificationObj.optString("notification").equalsIgnoreCase(null)) {
            if (notificationObj.has("notification")) {
                holder.tvTitle.setText(notificationObj.optJSONObject("notification").optString("subject"));
                holder.tvDate.setText(notificationObj.optJSONObject("notification").optString("startDate"));
                holder.tvDec.setText(notificationObj.optJSONObject("notification").optString("message"));
            }
//            }
            holder.ivDelete.setOnClickListener(this);
            holder.ivDelete.setTag(notificationObj.toString());
            return view;
        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvDec;
            TextView tvDate;
            ImageView ivDelete;
        }

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.ivNotificationElementDelete:
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                    alertbox.setCancelable(true);
                    alertbox.setMessage("Are you sure you want to delete ?");
                    alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            JSONObject objReminder = null;
                            try {
                                objReminder = new JSONObject((String) view.getTag());
                                deleteNotification(objReminder.optString("id"));
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
            }
        }

    }

}
