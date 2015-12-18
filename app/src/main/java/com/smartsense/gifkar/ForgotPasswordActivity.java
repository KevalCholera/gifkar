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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.gifkar.adapter.CountryCodeAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
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
                if (etMobileNo.length() != 0 ||etEmail.length() != 0) {
                    doForgot("mobile",etMobileNo.getText().toString());
                }else {

                }
                break;
            case R.id.etForgotCountryCode:
                getCountryList();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void openCountryPopup(JSONObject response1) {
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

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
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


    public void doForgot(String device, String email) {
        final String tag = "forgotpassword";
        String url = Constants.BASE_URL + "/user/requestResetPassword";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_LOGIN));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("flag", device);
        params.put("value", email);
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getCountryList() {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get/?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getApplicationContext(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_FORGOT_PASS:
                            if (response.optJSONObject("data").has("otp")) {
                                startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_FORGOT));
                            } else
                                CommonUtil.alertBox(this, "", response.optString("message"));
                            break;
                        case Constants.Events.EVENT_COUNTRY_LIST:
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
