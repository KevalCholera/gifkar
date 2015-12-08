package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.MyAddressAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvMyAddress;
    LinearLayout llMyAddress;
    private ImageView btBack, btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_my_address));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
//        btInfo.setBackground(getResources().getDrawable(R.drawable.));
        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent =(Toolbar) v.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);
        llMyAddress = (LinearLayout) findViewById(R.id.llMyAddress);
        llMyAddress.setOnClickListener(this);
        lvMyAddress = (ListView) findViewById(R.id.lvMyAddress);
        String temp = "{ \"eventId\" : 123,   \"errorCode\" : 0,   \"status\" : 200,   \"message\" : \"Address list.\", \"data\" :  { \"deliveryAddresses\" : [ { \"recipientName\" : \"raju bhai\",  \"recipientContact\" : \"98989898\", \"address\" : \"titanium city center\",  \"landmark\" : \"sachin tower\", \"isActive\" : \"1\",   \"area\" : { \"id\" : \"1\",   \"name\" : \"Prahlad nagar\" } },  { \"recipientName\" : \"raju bhai\",   \"recipientContact\" : \"98989898\",  \"address\" : \"titanium city center\",    \"landmark\" : \"sachin tower\",  \"isActive\" : \"1\",  \"area\" : { \"id\" : \"1\" , \"name\" : \"Prahlad nagar\" } } ] } }";
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
            case R.id.llMyAddress:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            default:
        }
    }

    public void myAddressFill(JSONObject address) {
        MyAddressAdapter myAddressAdapter = null;
        try {
            myAddressAdapter = new MyAddressAdapter(this, address.getJSONObject("data").getJSONArray("deliveryAddresses"), true);
            lvMyAddress.setAdapter(myAddressAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
