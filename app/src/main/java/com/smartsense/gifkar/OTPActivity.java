//package com.smartsense.gifkar;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.mpt.storage.SharedPreferenceUtil;
//import com.smartsense.deliveryjunction.utill.JsonErrorShow;
//import com.smartsense.gifkar.utill.CommonUtil;
//import com.smartsense.gifkar.utill.Constants;
//import com.smartsense.gifkar.utill.DataRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class OTPActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
//
//
//    EditText etotpcode;
//    TextView tvresendcode;
//    Button btndoneotp;
//    TextView tvdesnumber;
//
//    EditText inputeditnumber;
//    Button btnsendnumber;
//    private boolean issignup = true;
//    private LinearLayout llotpview;
//    private LinearLayout lleditnumber;
//
//
//    private static OTPActivity inst;
//
//    public static OTPActivity instance() {
//        return inst;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        inst = this;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
//    }
//
//    public void updateList(final String otp) {
//        etotpcode.setText(otp);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp);
//
//
//        llotpview = (LinearLayout) findViewById(R.id.ll_otp_view);
//        lleditnumber = (LinearLayout) findViewById(R.id.ll_editnumber);
//
//        //true for otp
//        //false for mobile number
//        issignup = getIntent().getBooleanExtra("flag", false);
//
//
//        etotpcode = (EditText) findViewById(R.id.et_code_otp);
//        tvresendcode = (TextView) findViewById(R.id.tv_resend_otp);
//        btndoneotp = (Button) findViewById(R.id.otp_button);
//        tvdesnumber = (TextView) findViewById(R.id.tv_desc_otp);
//
//        tvdesnumber.setText("We've sent an SMS with an activation code to: \n +91" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
//        tvresendcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //call resend code
//                CommonUtil.closeKeyboard(OTPActivity.this);
//                changeview(false);
//                inputeditnumber.requestFocus();
//                btnsendnumber.setEnabled(true);
//                btnsendnumber.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            }
//        });
//
//        etotpcode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 4) {
//                    btndoneotp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    btndoneotp.setEnabled(true);
//                    otpverification(charSequence.toString());
//                } else {
//                    btndoneotp.setBackgroundResource(android.R.drawable.btn_default);
//                    btndoneotp.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        btndoneotp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonUtil.closeKeyboard(OTPActivity.this);
//                String otp = etotpcode.getText().toString();
//                if (otp.length() == 4) {
//                    // call otp api
//
//                } else {
//                    etotpcode.requestFocus();
//                }
//            }
//        });
//
//
//        //this is for edit mobile number
//        inputeditnumber = (EditText) findViewById(R.id.et_number_edit);
//        btnsendnumber = (Button) findViewById(R.id.btn_resend_number);
//
////        inputeditnumber.setText(getIntent().getStringExtra("mobile_no"));
//        inputeditnumber.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
//        if (!inputeditnumber.getText().toString().equals("")) {
//            inputeditnumber.setEnabled(true);
//            inputeditnumber.setSelection(10);
//            btnsendnumber.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        }
//        inputeditnumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (charSequence.length() == 10) {
//                    if (charSequence.toString().equals(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, "1"))) {
//                        btnsendnumber.setEnabled(true);
//                        btnsendnumber.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    } else {
//                        getemailstatus(charSequence.toString());
//                    }
////                    btnsendnumber.setEnabled(true);
////                    btnsendnumber.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                } else {
//                    btnsendnumber.setBackgroundResource(android.R.drawable.btn_default);
//                    inputeditnumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    btnsendnumber.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//        btnsendnumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommonUtil.closeKeyboard(OTPActivity.this);
//                String number = inputeditnumber.getText().toString();
//                if (number.length() == 10) {
//                    //call resend api
//                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, number);
//                    resend(number);
//                } else {
//                    inputeditnumber.requestFocus();
//                }
//            }
//        });
//
//        //change view OTP of MNO Screen
//        changeview(issignup);
//    }
//
//    private void getemailstatus(String emailid) {
//
//        final String tag = "checkuser";
//        String url = Constants.BASE_URL + "checkuser";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("email_id", emailid);
////        CommonUtil.showProgressDialog(this, "Wait...");
//        params.put("event_id", String.valueOf(Constants.Events.EVENT_EMAIL_CHECK));
//        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
//        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        DeliveryJunctionApp.getInstance().addToRequestQueue(loginRequest, tag);
//
//    }
//
//    private void emailresults(String flag) {
//
//        // for email verify
//        if (flag.equals("1")) {
//            //User not available
//            inputeditnumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_approved, 0);
//            btnsendnumber.setEnabled(true);
//            btnsendnumber.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//
//
//        } else {
//            //useravailable
//            btnsendnumber.setBackgroundResource(android.R.drawable.btn_default);
//            inputeditnumber.setError(getString(R.string.user_available));
//            btnsendnumber.setEnabled(false);
//            inputeditnumber.requestFocus();
//        }
//    }
//
//
//    public void changeview(boolean issignup) {
//        if (issignup) {
//            // flag true is from signup page
//            // enable otp view
//            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ISWAITING_SMS, "true");
//            lleditnumber.setVisibility(View.GONE);
//            llotpview.setVisibility(View.VISIBLE);
//
//            etotpcode.requestFocus();
//        } else {
//            // flag false is comes from login page
//
//            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ISWAITING_SMS, "false");
//            llotpview.setVisibility(View.GONE);
//            lleditnumber.setVisibility(View.VISIBLE);
//            inputeditnumber.requestFocus();
//        }
//        SharedPreferenceUtil.save();
//
//    }
//
//    private void otpverification(String otp) {
//        final String tag = "otpverification";
//        String url = Constants.BASE_URL + "otpverification";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("user_id", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
//        params.put("otp", otp);
//        params.put("event_id", String.valueOf(Constants.Events.EVENT_OTPVERIFICATION));
//        Log.d("OTP Activity", params.toString());
//        CommonUtil.showProgressDialog(this, "Wait...");
//        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
//        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        DeliveryJunctionApp.getInstance().addToRequestQueue(loginRequest, tag);
//
//    }
//
//    private void resend(String mno) {
//        final String tag = "resendOTP";
//        String url = Constants.BASE_URL + "resendOTP";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("user_id", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
//        params.put("mobile_no", mno);
//        params.put("event_id", String.valueOf(Constants.Events.EVENT_RESEND_OTP));
//        Log.d("OTP Activity", params.toString());
//        CommonUtil.showProgressDialog(this, "Wait...");
//        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
//        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        DeliveryJunctionApp.getInstance().addToRequestQueue(loginRequest, tag);
//
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError volleyError) {
//        CommonUtil.alertBox(getApplicationContext(), "", getResources().getString(R.string.nointernet_try_again_msg));
//        CommonUtil.cancelProgressDialog();
//    }
//
//    @Override
//    public void onResponse(JSONObject response) {
//        CommonUtil.cancelProgressDialog();
//        if (response != null) {
//            try {
//                if (response.getString("status_code").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
//                    switch (Integer.valueOf(response.getString("event_id"))) {
//                        case Constants.Events.EVENT_OTPVERIFICATION:
//
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ISOTP_VERIFIED, true);
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("id"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, response.getJSONArray("data").getJSONObject(0).getString("email"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.getJSONArray("data").getJSONObject(0).getString("mobile"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, response.getJSONArray("data").getJSONObject(0).getString("name"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_REFER_CODE, response.getJSONArray("data").getJSONObject(0).getString("referCode"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, response.getJSONArray("data").getJSONObject(0).getString("profileimage"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_REFER, response.getJSONArray("data").getJSONObject(0).getString("hasrefered"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_REFER_MSG, response.getJSONArray("data").getJSONObject(0).getString("referMessage"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PASS, response.getJSONArray("data").getJSONObject(0).getString("password"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.getString("token"));
//                            SharedPreferenceUtil.save();
//                            startActivity(new Intent(this, DeliveryJunction.class));
//                            finish();
//                            break;
//                        case Constants.Events.EVENT_RESEND_OTP:
//                            changeview(true);
//                            etotpcode.requestFocus();
//                            tvdesnumber.setText("We've sent an SMS with an activation code to: \n +91" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
//                            break;
//
//                        case Constants.Events.EVENT_EMAIL_CHECK:
//                            String flag = response.getJSONObject("data").getString("flag");
//                            Log.d("flag-----", flag);
//                            //assign global check for signup approved
//                            emailresults(flag);
//                            break;
//                    }
//
//                } else {
//                    JsonErrorShow.jsonErrorShow(response, this);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//}
