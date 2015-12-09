package com.smartsense.gifkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.gifkar.utill.Constants;

public class ProductFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.filter));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_product_filter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOtpEditNumber:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.btnOTP:
                if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_FORGOT)
                    startActivity(new Intent(this, ChangePasswordActivity.class).putExtra(Constants.SCREEN,Constants.ScreenCode.SCREEN_OTP));
                else
                    startActivity(new Intent(this, CitySelectActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
