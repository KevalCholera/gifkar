package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.gifkar.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etArea, etNo, etPincode, etStreet, etFlatNo, etName,etLandmark;
    Button btnAddAddress;
    ImageView btBack;
    JSONObject addressObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        try {
            if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYADDRESS) {
                titleTextView.setText(getResources().getString(R.string.screen_edit_address));
                addressObj = new JSONObject(getIntent().getStringExtra("Address"));
            } else
                titleTextView.setText(getResources().getString(R.string.screen_add_address));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_add_address);
        etArea = (EditText) findViewById(R.id.etMyAddressAddArea);
        etNo = (EditText) findViewById(R.id.etMyAddressAddNo);
        etPincode = (EditText) findViewById(R.id.etMyAddressAddPinCode);
        etStreet = (EditText) findViewById(R.id.etMyAddressAddStreet);
        etFlatNo = (EditText) findViewById(R.id.etMyAddressAddFlatNo);
        etName = (EditText) findViewById(R.id.etMyAddressAddName);
        etLandmark = (EditText) findViewById(R.id.etMyAddressAddLandmark);
        btnAddAddress = (Button) findViewById(R.id.btnMyAddressAddAddress);
        btnAddAddress.setOnClickListener(this);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYADDRESS) {
            btnAddAddress.setText(getResources().getString(R.string.update));
            etArea.setText(addressObj.optJSONObject("area").optString("name"));
            etPincode.setText(addressObj.optJSONObject("area").optString("name"));
            etNo.setText(addressObj.optString("recipientContact"));
            etStreet.setText(addressObj.optString(""));
            etFlatNo.setText(addressObj.optString("address"));
            etName.setText(addressObj.optString("recipientName"));
            etLandmark.setText(addressObj.optString("landmark"));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMyAddressAddAddress:

                break;
            default:
        }
    }
}
