package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CountryCodeAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileNoActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etCountryCode, etMobileNo;
    Button btSend;
    AlertDialog alert;
    private ImageView btBack;
    Boolean checkCountry = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        etCountryCode = (EditText) findViewById(R.id.etEnterCountryCode);
        etCountryCode.setOnClickListener(this);
        etMobileNo = (EditText) findViewById(R.id.etEnterMobileNo);
        if (getIntent().getIntExtra(Constants.SCREEN, 0) == Constants.ScreenCode.SCREEN_LOGIN)
            titleTextView.setText(getResources().getString(R.string.screen_enter));
        else{
            titleTextView.setText(getResources().getString(R.string.screen_change_mno));
            etMobileNo.setText(getIntent().getStringExtra("no"));
            etCountryCode.setText(getIntent().getStringExtra("code"));
            etCountryCode.setTag(getIntent().getStringExtra("tag"));}
        btSend = (Button) findViewById(R.id.btnSend);
        btSend.setOnClickListener(this);
//        getCountryList(checkCountry);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_COUNTRY_LIST);
        SharedPreferenceUtil.save();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                if (TextUtils.isEmpty(etCountryCode.getText().toString())) {
                    CommonUtil.alertBox(this, "", "Please Select Country Code.");
                } else if (TextUtils.isEmpty(etMobileNo.getText().toString())) {
                    etMobileNo.setError(getString(R.string.wrn_mno));
                } else if (!(etMobileNo.length() >= 8 && etMobileNo.length() <= 13)) {
                    etMobileNo.setError(getString(R.string.wrn_valid_mno));
                } else {
                    doSendOTP();
                }
                break;
            case R.id.etEnterCountryCode:
                if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_COUNTRY_LIST, "").equalsIgnoreCase("")) {
                    checkCountry = true;
                    getCountryList(checkCountry);
                } else {
                    try {
                        openCountryPopup(new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_COUNTRY_LIST, "")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void openCountryPopup(JSONObject response) {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (response.getJSONObject("data").getJSONArray("countries").length() == 0) {
                CommonUtil.alertBox(this, "", "Country Code Not Found Please Try Again.");
            } else {
                etCountryCode.setText("+" + response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("code"));
                etCountryCode.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("id"));
                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(this, response.getJSONObject("data").getJSONArray("countries"), true);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etCountryCode.setText("+" + getCodeObj.optString("code"));
                        etCountryCode.setTag(getCodeObj.optString("id"));
                        alert.dismiss();

                    }
                });
                alertDialogs.setView(dialog);
                alertDialogs.setCancelable(false);
                alert = alertDialogs.create();
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSendOTP() {
        final String tag = "resendOTP";
        String url = Constants.BASE_URL + "/mobile/user/resendOtp";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_RESEND_OTP));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
        params.put("mobile", etMobileNo.getText().toString());
        params.put("countryCode", (String) etCountryCode.getTag());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    public void getCountryList(Boolean check) {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        if (check) {
//            checkCountry = false;
            CommonUtil.showProgressDialog(this, "Wait...");
        }
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
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
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("eventId")) {
                        case Constants.Events.EVENT_RESEND_OTP:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
                            SharedPreferenceUtil.save();
                            startActivity(new Intent(this, OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
                            finish();
                            break;
                        case Constants.Events.EVENT_COUNTRY_LIST:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_COUNTRY_LIST, response.toString());
                            SharedPreferenceUtil.save();
                            if (response.getJSONObject("data").getJSONArray("countries").length() != 0) {
                                etCountryCode.setText("+" + response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("code"));
                                etCountryCode.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("id"));
                            }
                            if (checkCountry)
                                openCountryPopup(response);

                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
