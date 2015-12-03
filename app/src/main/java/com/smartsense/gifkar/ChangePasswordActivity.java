package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etOldPass, etConPass, etNewPass;
    Button btChangePass;
    ImageView btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.center_action_bar, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_change));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_change_password);
        etOldPass = (EditText) findViewById(R.id.etChangePassoldPassword);
        etConPass = (EditText) findViewById(R.id.etChangePassConfirmPassword);
        etNewPass = (EditText) findViewById(R.id.etChangePassNewPassword);
        btChangePass = (Button) findViewById(R.id.btnChangePass);
        btChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePass:
                startActivity(new Intent(this, GifkarActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
