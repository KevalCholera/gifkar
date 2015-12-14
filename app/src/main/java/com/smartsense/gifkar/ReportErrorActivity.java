package com.smartsense.gifkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ReportErrorActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private RadioGroup rgReportError;
    private RadioButton rbPhone;
    private RadioButton rbAddress;
    private RadioButton rbShopClose;
    private RadioButton rbMenu;
    private Button btReportError;
    private EditText etReportErrorDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.report_error));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_report_error);
        rgReportError=(RadioGroup) findViewById(R.id.rgReportError);
        rbPhone=(RadioButton) findViewById(R.id.rbReportErrorPhone);
        rbAddress=(RadioButton) findViewById(R.id.rbReportErrorAddress);
        rbShopClose=(RadioButton) findViewById(R.id.rbReportErrorShopClose);
        rbMenu=(RadioButton) findViewById(R.id.rbReportErrorMenu);
        btReportError=(Button) findViewById(R.id.btnReportError);
        btReportError.setOnClickListener(this);
        etReportErrorDes=(EditText) findViewById(R.id.etReportErrorDes);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReportError:
                startActivity(new Intent(this, OTPActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
