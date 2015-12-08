package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btBack;
    private LinearLayout llGifkar, llHelp, llFAQs;

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
        setContentView(R.layout.activity_about_us);
        llGifkar=(LinearLayout) findViewById(R.id.llGifkar);
        llGifkar.setOnClickListener(this);
        llHelp=(LinearLayout) findViewById(R.id.llHelp);
        llHelp.setOnClickListener(this);
        llFAQs=(LinearLayout) findViewById(R.id.llFAQS);
        llFAQs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llGifkar:
                finish();
                break;
            case R.id.llHelp:
                finish();
                break;
            case R.id.llFAQS:
                finish();
                break;

        }
    }
}
