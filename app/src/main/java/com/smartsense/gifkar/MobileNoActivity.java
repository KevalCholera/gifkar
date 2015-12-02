package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MobileNoActivity extends AppCompatActivity implements View.OnClickListener{
    EditText  etCountryCode, etMobileNo;
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.center_action_bar, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_enter));
        getSupportActionBar().setCustomView(v);
        etCountryCode=(EditText) findViewById(R.id.etEnterCountryCode);
        etMobileNo=(EditText) findViewById(R.id.etEnterMobileNo);
        btSend=(Button) findViewById(R.id.btnSend);
        btSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.btnSend:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            default:
        }
    }
}
