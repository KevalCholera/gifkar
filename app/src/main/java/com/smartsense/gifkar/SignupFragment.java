package com.smartsense.gifkar;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SignupFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etFirstName, etLastName, etMobileNo, etEmail, etPass, etConPass, etCountryCode;
    Button btSignUp;
    TextView tvSkip, tvTerms;
    CheckBox cbTerms;
    AlertDialog alert;
    Boolean isEmailVerified = false;
    Boolean isMnoVerified = false;
    Boolean checkCountry = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_signup, container, false);
        etFirstName = (EditText) view.findViewById(R.id.etSignUpFirstName);
        etLastName = (EditText) view.findViewById(R.id.etSignUpLastName);
        etMobileNo = (EditText) view.findViewById(R.id.etSignUpMobileNo);
        etMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (!hasFocus) {
                    isMnoVerified = false;
                    if (etMobileNo.length() != 0) {
                        if ((etMobileNo.length() >= 8 && etMobileNo.length() <= 13)) {
                            checkMobileEmail("mobile", etMobileNo.getText().toString(), Constants.Events.EVENT_MOBILE_CHECK);
                        }
                    }
                }

            }
        });
        etEmail = (EditText) view.findViewById(R.id.etSignUpEmailId);
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                             @Override
                                             public void onFocusChange(View v, boolean hasFocus) {
                                                 etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                 if (!hasFocus) {
                                                     // TODO: the editText has just been left
                                                     isEmailVerified = false;
                                                     if (etEmail.length() != 0) {
                                                         if (CommonUtil.isValidEmail(etEmail.getText().toString())) {
                                                             checkMobileEmail("email", etEmail.getText().toString(), Constants.Events.EVENT_EMAIL_CHECK);
                                                         }
                                                     }
                                                 }
                                             }
                                         }

        );

        etPass = (EditText) view.findViewById(R.id.etSignUpPassword);
        etConPass = (EditText) view.findViewById(R.id.etSignUpConfirmPassword);
        etCountryCode = (EditText) view.findViewById(R.id.etSignUpCountryCode);
        etCountryCode.setOnClickListener(this);
        btSignUp = (Button) view.findViewById(R.id.btnSignup);
        btSignUp.setOnClickListener(this);
        tvSkip = (TextView) view.findViewById(R.id.tvSignupSkip);
        tvSkip.setOnClickListener(this);
        tvTerms = (TextView) view.findViewById(R.id.tvSignupTerms);
        tvTerms.setOnClickListener(this);
        cbTerms = (CheckBox) view.findViewById(R.id.rbSignupTerms);
        getCountryList(checkCountry);

        return view;
    }

    @Override
    public void onClick(View view) {
        CommonUtil.closeKeyboard(getActivity());
        etMobileNo.clearFocus();
        etEmail.clearFocus();
        switch (view.getId()) {
            case R.id.btnSignup:
//                startActivity(new Intent(getActivity(), OTPActivity.class));
                doSignUp();
                break;
            case R.id.etSignUpCountryCode:
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
            case R.id.tvSignupTerms:
                startActivity(new Intent(getActivity(), TermsandCondtionsActivity.class));
                break;
            case R.id.tvSignupSkip:
                if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                    startActivity(new Intent(getActivity(), GifkarActivity.class));
                else
                    startActivity(new Intent(getActivity(), CitySelectActivity.class));
                break;
            default:
        }
    }

    public void openCountryPopup(JSONObject response) {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (response.getJSONObject("data").getJSONArray("countries").length() == 0) {
                CommonUtil.alertBox(getActivity(), "", "Country Code Not Found Please Try Again.");
            } else {
                etCountryCode.setText("+" + response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("code"));
                etCountryCode.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("id"));

                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(), response.getJSONObject("data").getJSONArray("countries"), true);
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

    public void getCountryList(Boolean check) {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        if (check) {
            checkCountry = false;
            CommonUtil.showProgressDialog(getActivity(), "Wait...");
        }
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void checkMobileEmail(String device, String value, int eventId) {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/user/checkAvailability?defaultToken=" + Constants.DEFAULT_TOKEN + "&value=" + value + "&checkType=" + device + "&eventId=" + String.valueOf(eventId);
//        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void doSignUp() {
        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            etFirstName.setError(getString(R.string.wrn_fname));
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            etLastName.setError(getString(R.string.wrn_lname));
        } else if (TextUtils.isEmpty(etCountryCode.getText().toString())) {
            CommonUtil.alertBox(getActivity(), "", "Please Select Country Code.");
        } else if (TextUtils.isEmpty(etMobileNo.getText().toString())) {
            etMobileNo.setError(getString(R.string.wrn_mno));
        } else if (!(etMobileNo.length() >= 8 && etMobileNo.length() <= 13)) {
            etMobileNo.setError(getString(R.string.wrn_valid_mno));
        } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.wrn_em));
        } else if (!CommonUtil.isValidEmail(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.wrn_email));
        } else if (TextUtils.isEmpty(etPass.getText().toString())) {
            etPass.setError(getString(R.string.wrn_pwd));
        } else if (TextUtils.isEmpty(etConPass.getText().toString())) {
            etConPass.setError(getString(R.string.wrn_repwd));
        } else if (etPass.length() < 5){
            etPass.setError(getString(R.string.wrn_pwd_len));
        } else if (!TextUtils.equals(etPass.getText().toString(), etConPass.getText().toString())) {
            etConPass.setError(getString(R.string.wrn_match));
        } else if (!cbTerms.isChecked()) {
            CommonUtil.alertBox(getActivity(), "", "Please check Terms & Conditions to Continue.");
        } else if (!isMnoVerified) {
            CommonUtil.alertBox(getActivity(), "", "Mobile already exists.");
        } else if (!isEmailVerified) {
            CommonUtil.alertBox(getActivity(), "", "Email already exists.");
        } else {
            final String tag = "Signup";
            String url = Constants.BASE_URL + "/mobile/user/signup";
            Map<String, String> params = new HashMap<String, String>();
            params.put("firstName", etFirstName.getText().toString());
            params.put("lastName", etLastName.getText().toString());
            params.put("email", etEmail.getText().toString());
            params.put("mobile", etMobileNo.getText().toString());
            params.put("password", etPass.getText().toString());
            params.put("countryId", (String) etCountryCode.getTag());
            params.put("eventId", String.valueOf(Constants.Events.EVENT_SIGNUP));
            params.put("defaultToken", Constants.DEFAULT_TOKEN);
            Log.i("params", params.toString());
            CommonUtil.showProgressDialog(getActivity(), "Wait...");
            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }


    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_SIGNUP:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").optString("userId"));
                            SharedPreferenceUtil.save();
                            startActivity(new Intent(getActivity(), OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
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
                        case Constants.Events.EVENT_EMAIL_CHECK:
                            etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verified, 0);
                            isEmailVerified = true;
                            break;
                        case Constants.Events.EVENT_MOBILE_CHECK:
                            etMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verified, 0);
                            isMnoVerified = true;
                            break;
                    }
                } else {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_EMAIL_CHECK:
                            etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unverified, 0);
                            isEmailVerified = false;
                            break;
                        case Constants.Events.EVENT_MOBILE_CHECK:
                            etMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unverified, 0);
                            isMnoVerified = false;
                            break;
                        default:
                            JsonErrorShow.jsonErrorShow(response, getActivity());
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_COUNTRY_LIST);
        SharedPreferenceUtil.save();
    }
}
