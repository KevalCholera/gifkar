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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_signup, container, false);
        etFirstName = (EditText) view.findViewById(R.id.etSignUpFirstName);
        etLastName = (EditText) view.findViewById(R.id.etSignUpLastName);
        etMobileNo = (EditText) view.findViewById(R.id.etSignUpMobileNo);
        etEmail = (EditText) view.findViewById(R.id.etSignUpEmailId);
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
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
//                startActivity(new Intent(getActivity(), OTPActivity.class));
                doSignUp();
                break;
            case R.id.etSignUpCountryCode:
                getCountryList();
                break;
            case R.id.tvSignupTerms:
                startActivity(new Intent(getActivity(), TermsandCondtionsActivity.class));
                break;
            case R.id.tvSignupSkip:
                startActivity(new Intent(getActivity(), GifkarActivity.class));
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
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);

            CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(), response.getJSONObject("data").getJSONArray("countries"), true);
            list_view.setAdapter(countryCodeAdapter);

            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                    JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                    etCountryCode.setText(getCodeObj.optString("code"));
                    etCountryCode.setTag(getCodeObj.optString("id"));
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

    public void getCountryList() {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get/?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void checkMobileEmail(String device,String value) {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/user/checkAvailability/?defaultToken=" + Constants.DEFAULT_TOKEN + "&value="+value+"&checkType="+device+"&eventId=" + String.valueOf(Constants.Events.EVENT_EMAIL_CHECK);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void doSignUp() {
        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            etFirstName.setError(getString(R.string.wrn_fname));
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            etLastName.setError(getString(R.string.wrn_lname));
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
        } else if (!TextUtils.equals(etPass.getText().toString(), etConPass.getText().toString())) {
            etConPass.setError(getString(R.string.wrn_match));
        } else if (!cbTerms.isChecked()) {
            CommonUtil.alertBox(getActivity(), "", "Please check Terms & Conditions to Continue.");
        } else if (!isEmailVerified) {
            CommonUtil.alertBox(getActivity(), "", "Email already exists.");
        } else if (!isMnoVerified) {
            CommonUtil.alertBox(getActivity(), "", "Mobile already exists.");
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
//            CommonUtil.showProgressDialog(this, "Wait...");
//            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
//            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
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
                            startActivity(new Intent(getActivity(), OTPActivity.class));
                            break;
                        case Constants.Events.EVENT_COUNTRY_LIST:
                            openCountryPopup(response);
                            break;
                        case Constants.Events.EVENT_EMAIL_CHECK:
                            isEmailVerified=true;
                            isMnoVerified=true;
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
