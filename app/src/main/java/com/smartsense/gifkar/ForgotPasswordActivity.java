package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etEmail, etCountryCode, etMobileNo;
    Button btForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.center_action_bar, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_forgot));
        getSupportActionBar().setCustomView(v);


        etEmail=(EditText) findViewById(R.id.etForgotEmailId);
        etCountryCode=(EditText) findViewById(R.id.etForgotCountryCode);
        etMobileNo=(EditText) findViewById(R.id.etForgotMobileNo);
        btForgot=(Button) findViewById(R.id.btnForgot);
    }


}
