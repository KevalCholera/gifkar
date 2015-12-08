package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.gifkar.utill.CommonUtil;

public class FeedUsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private LinearLayout llRateUs, llEmailUs, llContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_about));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);

        setContentView(R.layout.activity_feed_us);
        llContactUs=(LinearLayout) findViewById(R.id.llContactUs);
        llContactUs.setOnClickListener(this);
        llRateUs=(LinearLayout) findViewById(R.id.llRateUs);
        llRateUs.setOnClickListener(this);
        llEmailUs=(LinearLayout) findViewById(R.id.llEmailUs);
        llEmailUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llEmailUs:
                openInfoPopup();
                break;
            case R.id.llRateUs:
                CommonUtil.openAppRating(this);
                break;
            case R.id.llContactUs:
                openInfoPopup();
                break;

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
