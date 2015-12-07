package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.CountryCodeAdapter;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail, etCountryCode, etMobileNo;
    Button btForgot;
    ImageView btBack;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        titleTextView.setText(getResources().getString(R.string.screen_forgot));
        getSupportActionBar().setCustomView(v);


        etEmail = (EditText) findViewById(R.id.etForgotEmailId);
        etCountryCode = (EditText) findViewById(R.id.etForgotCountryCode);
        etCountryCode.setOnClickListener(this);
        etMobileNo = (EditText) findViewById(R.id.etForgotMobileNo);
        btForgot = (Button) findViewById(R.id.btnForgot);
        btForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnForgot:
                startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_FORGOT));
                break;
            case R.id.etForgotCountryCode:
                openCountryPopup();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void openCountryPopup() {
        String tempary = "{\n" +
                "\t\"eventId\": 123,\n" +
                "\t\"errorCode\": 0,\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"country list.\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"countries\": [{\n" +
                "\t\t\t\"id\": \"18\",\n" +
                "\t\t\t\"name\": \"India\",\n" +
                "\t\t\t\"code\": \"+92\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"19\",\n" +
                "\t\t\t\"name\": \"Pakistan\",\n" +
                "\t\t\t\"code\": \"+93\"\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "}";
        try {
            JSONObject response = new JSONObject(tempary);
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.list_view, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);

            CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(this, response.getJSONObject("data").getJSONArray("countries"), true);
            list_view.setAdapter(countryCodeAdapter);

            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                    JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                    etCountryCode.setText(getCodeObj.optString("code"));
                    alert.dismiss();

                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
