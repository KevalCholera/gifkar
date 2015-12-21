package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.NotificationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationActivity extends Fragment implements View.OnClickListener {
    ImageView btBack;
    private LinearLayout llNotification;
    private ListView lvNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_notification, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_notification));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        lvNotification = (ListView) view.findViewById(R.id.lvNotification);
        llNotification = (LinearLayout) view.findViewById(R.id.llNotification);
        String temp = "{ \"eventId\" : 123,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"Address list.\", \"data\" :  { \"deliveryAddresses\" : [ { \"recipientName\" : \"raju bhai\",  \"recipientContact\" : \"98989898\", \"address\" : \"titanium city center\",  \"landmark\" : \"sachin tower\", \"isActive\" : \"1\",   \"area\" : { \"id\" : \"1\",   \"name\" : \"Prahlad nagar\" } },  { \"recipientName\" : \"raju bhai\",   \"recipientContact\" : \"98989898\",  \"address\" : \"titanium city center\",    \"landmark\" : \"sachin tower\",  \"isActive\" : \"1\",  \"area\" : { \"id\" : \"1\" , \"name\" : \"Prahlad nagar\" } } ] } }";
        try {
            JSONObject notification = new JSONObject(temp);
            notificationFill(notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btActionBarBack:
//                finish();
//                break;
            default:
        }
    }

    public void notificationFill(JSONObject notification) {
        NotificationAdapter notificationAdapter = null;
        try {
            notificationAdapter = new NotificationAdapter(getActivity(), notification.getJSONObject("data").getJSONArray("notifications"), true);
            lvNotification.setAdapter(notificationAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
