package com.smartsense.gifkar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etArea, etNo, etPincode, etStreet, etFlatNo, etName, etLandmark;
    Button btnAddAddress;
    ImageView btBack;
    JSONObject addressObj;
    Boolean check = true;

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
        etNo = (EditText) findViewById(R.id.etMyAddressAddNo);

        etArea = (EditText) findViewById(R.id.etMyAddressAddArea);
        etArea.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_NAME, ""));

        etPincode = (EditText) findViewById(R.id.etMyAddressAddPinCode);
        etPincode.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_PIN_CODE, ""));
        etStreet = (EditText) findViewById(R.id.etMyAddressAddStreet);
        etFlatNo = (EditText) findViewById(R.id.etMyAddressAddFlatNo);
        etName = (EditText) findViewById(R.id.etMyAddressAddName);
        etLandmark = (EditText) findViewById(R.id.etMyAddressAddLandmark);
        btnAddAddress = (Button) findViewById(R.id.btnMyAddressAddAddress);
        btnAddAddress.setOnClickListener(this);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYADDRESS) {
            check = false;
            btnAddAddress.setText(getResources().getString(R.string.update));
            etArea.setText(addressObj.optJSONObject("area").optString("name"));
            etPincode.setText(addressObj.optJSONObject("area").optString("name"));
            etNo.setText(addressObj.optString("recipientContact"));
            etStreet.setText(addressObj.optString("companyName"));
            etFlatNo.setText(addressObj.optString("address"));
            etName.setText(addressObj.optString("recipientName"));
            etLandmark.setText(addressObj.optString("landmark"));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMyAddressAddAddress:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError(getString(R.string.wrn_reception_name));
                } else if (TextUtils.isEmpty(etNo.getText().toString())) {
                    etNo.setError(getString(R.string.wrn_reception_no));
                } else if (etNo.length() != 10) {
                    etNo.setError(getString(R.string.wrn_valid_mno));
                } else if (TextUtils.isEmpty(etFlatNo.getText().toString())) {
                    etFlatNo.setError(getString(R.string.wrn_flat));
                } else if (TextUtils.isEmpty(etStreet.getText().toString())) {
                    etStreet.setError(getString(R.string.wrn_street));
                } else if (TextUtils.isEmpty(etLandmark.getText().toString())) {
                    etLandmark.setError(getString(R.string.wrn_land));
                } else
                    addAddress(check);
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void addAddress(Boolean check) {
        final String tag = "address";
        String url;
        Map<String, String> params = new HashMap<String, String>();
        if (check) {
            url = Constants.BASE_URL + "/mobile/deliveryAddress/create";
            params.put("eventId", String.valueOf(Constants.Events.EVENT_ADD_ADDRESS));
        } else {
            url = Constants.BASE_URL + "/mobile/deliveryAddress/update";
            params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE));
            params.put("addressId", addressObj.optString("id"));
        }
        params.put("recipientName", etName.getText().toString());
        params.put("recipientContact", etNo.getText().toString());
        params.put("address", etFlatNo.getText().toString());
        params.put("companyName", etStreet.getText().toString());
        params.put("landmark", etLandmark.getText().toString());
        params.put("areaId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_ID, ""));
        params.put("cityId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CITY_ID, ""));
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_ADD_ADDRESS:
                            alert.setTitle("Success!");
                            alert.setMessage("Address Successfully Added.");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            });
                            alert.show();
                            break;
                        case Constants.Events.EVENT_UPDATE:
                            alert.setTitle("Success!");
                            alert.setMessage("Address Successfully Updated.");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            });
                            alert.show();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, AddAddressActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
