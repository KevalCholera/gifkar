package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvOtpNo, tvResend, tvEditNum;
    String countryCode = "", mobileNo = "";
    EditText etOne, etTwo, etThree, etFour;
    Button btOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.center_action_bar, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_otp));
        getSupportActionBar().setCustomView(v);

        tvOtpNo = (TextView) findViewById(R.id.tvOtpNo);
        tvResend = (TextView) findViewById(R.id.tvOtpResend);
        tvEditNum = (TextView) findViewById(R.id.tvOtpEditNumber);
        tvEditNum.setOnClickListener(this);
        etOne = (EditText) findViewById(R.id.etOTPOne);
        etTwo = (EditText) findViewById(R.id.etOTPTwo);
        etThree = (EditText) findViewById(R.id.etOTPThree);
        etFour = (EditText) findViewById(R.id.etOTPFour);
        btOTP =(Button) findViewById(R.id.btnOTP);
        btOTP.setOnClickListener(this);
        countryCode = "+91";
        mobileNo = "9999999999";
        tvOtpNo.setText("Please Enter the SMS code that you have received on " + countryCode + " " + mobileNo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOtpEditNumber:
                startActivity(new Intent(this, MobileNoActivity.class));
            case  R.id.btnOTP:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            default:
        }
    }
}
