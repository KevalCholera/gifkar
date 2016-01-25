package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etEmail, etCountryCode, etMobileNo;
    Button btForgot;
    ImageView btBack;
    AlertDialog alert;
    Boolean checkCountry = false;

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
        etMobileNo = (EditText) findViewById(R.id.etForgotMobileNo);
//        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    // TODO: the editText has just been left
//
//            }
//        });
        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (etEmail.length() != 0)
                    etMobileNo.setText("");

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        etCountryCode = (EditText) findViewById(R.id.etForgotCountryCode);
        etCountryCode.setOnClickListener(this);
//        etMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    // TODO: the editText has just been left
//                    if (etMobileNo.length() != 0)
//                        etEmail.setText("");
//            }
//        });
        etMobileNo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (etMobileNo.length() != 0)
                    etEmail.setText("");

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btForgot = (Button) findViewById(R.id.btnForgot);
        btForgot.setOnClickListener(this);
        getCountryList(checkCountry);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnForgot:
                if (etMobileNo.length() != 0 || etEmail.length() != 0) {
                    if (etMobileNo.length() != 0) {
                        if (!(etMobileNo.length() >= 8 && etMobileNo.length() <= 13))
                            etMobileNo.setError(getString(R.string.wrn_valid_mno));
                        else
                            doForgot("mobile", etMobileNo.getText().toString());
                    } else {
                        if (!CommonUtil.isValidEmail(etEmail.getText().toString()))
                            etEmail.setError(getString(R.string.wrn_email));
                        else
                            doForgot("email", etEmail.getText().toString());
                    }
                } else {
                    CommonUtil.alertBox(this, "", "Please Enter Email or Mobile No.");
//                    startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_FORGOT));
                }
                break;
            case R.id.etForgotCountryCode:
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


    public void doForgot(String device, String value) {
        final String tag = "forgotpassword";
        String url = Constants.BASE_URL + "/mobile/user/requestResetPassword";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_FORGOT_PASS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("flag", device);
        params.put("value", value);
        Log.d("forgot Params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getCountryList(Boolean check) {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        if (check) {
            checkCountry = false;
            CommonUtil.showProgressDialog(this, "Wait...");
        }
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(ForgotPasswordActivity.this, "", getResources().getString(R.string.nointernet_try_again_msg));
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
                            if (response.optJSONObject("data").has("userToken")) {
//                                startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_FORGOT).putExtra(Constants.OTP, response.optJSONObject("data").optString("otp")).putExtra("mobile_no", response.optJSONObject("data").optString("otp")).putExtra("country_code", response.optJSONObject("data").optString("otp")));
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").optString("userId"));
//                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").optString("userToken"));
                                SharedPreferenceUtil.save();
                                startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_FORGOT).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
                            } else
                                CommonUtil.alertBox(this, "", response.optString("message"));
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
