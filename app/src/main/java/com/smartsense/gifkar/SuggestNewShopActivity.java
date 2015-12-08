package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SuggestNewShopActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAddShop;
    private EditText etEmail;
    private EditText etShopName;
    private EditText etAddress;
    private EditText etCity;
    private EditText etArea;
    private EditText etWeblink;
    private EditText etPincode;
    private ImageView btBack;
    private ImageView btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_suggest));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
//        btInfo.setBackground(getResources().getDrawable(R.drawable.));
        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);
        setContentView(R.layout.activity_suggest_new_shop);
        etShopName = (EditText) findViewById(R.id.etNewShopName);
        etAddress = (EditText) findViewById(R.id.etNewShopAddress);
        etArea = (EditText) findViewById(R.id.etNewShopArea);
        etCity = (EditText) findViewById(R.id.etNewShopCity);
        etWeblink = (EditText) findViewById(R.id.etNewShopWeblink);
        etPincode = (EditText) findViewById(R.id.etNewShopPin);
        etEmail = (EditText) findViewById(R.id.etNewShopEmail);
        btnAddShop = (Button) findViewById(R.id.btnNewShopAdd);
        btnAddShop.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewShopAdd:

                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
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
