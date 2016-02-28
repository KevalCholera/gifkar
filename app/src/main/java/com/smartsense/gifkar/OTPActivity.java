package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.parse.ParseInstallation;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    TextView tvOtpNo, tvResend, tvEditNum;
    String countryCode = "", mobileNo = "", verify = "";
    EditText etOne, etTwo, etThree, etFour;
    Button btOTP;
    private ImageView btBack;
    private TextView tvOtpCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_otp));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setVisibility(View.INVISIBLE);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_otp);
        tvOtpNo = (TextView) findViewById(R.id.tvOtpNo);
        tvOtpCountDown = (TextView) findViewById(R.id.tvOtpCountDown);
        tvResend = (TextView) findViewById(R.id.tvOtpResend);
        tvResend.setOnClickListener(this);
        tvEditNum = (TextView) findViewById(R.id.tvOtpEditNumber);
        tvEditNum.setOnClickListener(this);
        etOne = (EditText) findViewById(R.id.etOTPOne);
        etTwo = (EditText) findViewById(R.id.etOTPTwo);
        etThree = (EditText) findViewById(R.id.etOTPThree);
        etFour = (EditText) findViewById(R.id.etOTPFour);
        etOne.addTextChangedListener(new CustomTextWatcher(etOne));
        etTwo.addTextChangedListener(new CustomTextWatcher(etTwo));
        etThree.addTextChangedListener(new CustomTextWatcher(etThree));
        etFour.addTextChangedListener(new CustomTextWatcher(etFour));
        etFour.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.KEYCODE_ENTER) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    otpCheck();
                    return true;
                }
                return false;
            }
        });
        btOTP = (Button) findViewById(R.id.btnOTP);
        btOTP.setOnClickListener(this);
        countryCode = getIntent().getStringExtra("code");
        mobileNo = getIntent().getStringExtra("mobile");
//        otp = getIntent().getStringExtra(Constants.OTP);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_FORGOT) {
            tvEditNum.setVisibility(View.GONE);
        }
        tvOtpNo.setText("Please Enter the SMS code that you have received on " + countryCode + " " + mobileNo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOtpEditNumber:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.tvOtpResend:
                tvResend.requestFocus();
                doResendOTP();
                break;
            case R.id.btnOTP:
                otpCheck();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }


    public void doVerifyOTP(String otp) {
        final String tag = "VerifyOTP";
        String url = Constants.BASE_URL + "/mobile/user/verifyOtp";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_SEND_OTP));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
        params.put("otp", otp);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void doResendOTP() {
        final String tag = "resendOTP";
        String url = Constants.BASE_URL + "/mobile/user/resendOtp";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_RESEND_OTP));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
        params.put("mobile", mobileNo);
        params.put("countryCode", countryCode.substring(1));
        CommonUtil.showProgressDialog(this, "Wait...");
        Log.i("params", params.toString());
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void otpCheck() {
        verify = etOne.getText().toString() + "" + etTwo.getText().toString() + "" + etThree.getText().toString() + "" + etFour.getText().toString();
        if (etOne.getText().toString().equalsIgnoreCase("") || etTwo.getText().toString().equalsIgnoreCase("") || etThree.getText().toString().equalsIgnoreCase("") || etFour.getText().toString().equalsIgnoreCase("")) {
            CommonUtil.alertBox(this, "", "Please Enter OTP.");
        } else
//        if (verify.equalsIgnoreCase(otp))
            doVerifyOTP(verify);

//        else
//            CommonUtil.alertBox(this, "", "OTP does not matched.");
    }

    public void coundDownStart() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResend.setEnabled(false);
                tvOtpCountDown.setVisibility(View.VISIBLE);
                tvOtpCountDown.setText(millisUntilFinished / 1000 + " seconds remaining");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvResend.setEnabled(true);
                tvOtpCountDown.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_COUNTRY_LIST);
        SharedPreferenceUtil.save();
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
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_RESEND_OTP:
                            etTwo.setText("");
                            etThree.setText("");
                            etFour.setText("");
                            etOne.setText("");
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
                            SharedPreferenceUtil.save();
//                            otp=response.optJSONObject("data").optString("userToken");
                            coundDownStart();
                            break;
                        case Constants.Events.EVENT_SEND_OTP:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.getJSONObject("data").getString("userToken"));
                            SharedPreferenceUtil.save();
                            if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_FORGOT) {
                                startActivity(new Intent(this, ChangePasswordActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_OTP));
                            } else {
                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
                                installation.saveInBackground();
                                if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                                    startActivity(new Intent(this, GifkarActivity.class));
                                else
                                    startActivity(new Intent(this, CitySelectActivity.class));
                            }
                            finish();
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

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;

        public CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!(s.toString().equalsIgnoreCase(""))) {
                if (mEditText == etOne) {
                    etOne.clearFocus();
                    etTwo.requestFocus();
                    etTwo.setCursorVisible(true);
                } else if (mEditText == etTwo) {
                    etTwo.clearFocus();
                    etThree.requestFocus();
                    etThree.setCursorVisible(true);
                } else if (mEditText == etThree) {
                    etThree.clearFocus();
                    etFour.requestFocus();
                    etFour.setCursorVisible(true);
                } else if (mEditText == etFour) {

                }
            }
        }

        public void afterTextChanged(Editable s) {

        }
    }
}
