package com.smartsense.gifkar;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.DateAndTimeUtil;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etLastName, etFirstName, etDob, etEmail;
    TextView tVProfileVerified;
    Button btnProfileUpdate;
    private RadioButton rbMale, rbFemale;
    private RadioGroup rbGroup;
    private Calendar mCalendar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_profile, container, false);
        mCalendar = Calendar.getInstance();
        etLastName = (EditText) view.findViewById(R.id.etProfileLastName);
        etFirstName = (EditText) view.findViewById(R.id.etProfileFirstName);
        etDob = (EditText) view.findViewById(R.id.etProfileDob);
        etDob.setOnClickListener(this);
        etEmail = (EditText) view.findViewById(R.id.etProfileEmail);
        tVProfileVerified = (TextView) view.findViewById(R.id.tVProfileVerified);
        tVProfileVerified.setOnClickListener(this);
        rbGroup = (RadioGroup) view.findViewById(R.id.rbProfileGroup);
        rbMale = (RadioButton) view.findViewById(R.id.rbProfileMale);
        rbMale.setTag("m");
        rbFemale = (RadioButton) view.findViewById(R.id.rbProfileFemale);
        rbFemale.setTag("f");
        btnProfileUpdate = (Button) view.findViewById(R.id.btnProfileUpdate);
        btnProfileUpdate.setOnClickListener(this);
        try {
            JSONObject userInfo = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_INFO, ""));
            userInfo = userInfo.optJSONObject("userDetails");
            etEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));
            if (userInfo.optString("isEmailVerified").equalsIgnoreCase("1")) {
                tVProfileVerified.setText("Verified");
                tVProfileVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verified, 0);
            } else {
                tVProfileVerified.setText("Unverified");
                tVProfileVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unverified, 0);
            }
            etFirstName.setText(userInfo.optString("firstName"));
            etLastName.setText(userInfo.optString("lastName"));
            etDob.setText(userInfo.optString("dob"));
            if (!userInfo.optString("dob").equalsIgnoreCase("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM", Locale.ENGLISH);
                mCalendar.setTime(sdf.parse(userInfo.optString("dob")));
            }
            if (userInfo.optString("gender").equalsIgnoreCase("m")) {
                rbMale.setChecked(true);
                rbFemale.setChecked(false);
            } else {
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfileUpdate:
                doUpdate();
                break;
            case R.id.etProfileDob:
                datePicker();
                break;
            case R.id.tVProfileVerified:
                break;
            default:
        }
    }

    public void doUpdate() {

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            etFirstName.setError(getString(R.string.wrn_fname));
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            etLastName.setError(getString(R.string.wrn_lname));
        } else if (TextUtils.isEmpty(etDob.getText().toString())) {
            etDob.setError(getString(R.string.wrn_dob));
        } else {
            RadioButton rb = (RadioButton) rbGroup.findViewById(rbGroup.getCheckedRadioButtonId());
            final String tag = "doUpdate";
            String url = Constants.BASE_URL + "/mobile/user/update";
            Map<String, String> params = new HashMap<String, String>();
            params.put("eventId", String.valueOf(Constants.Events.PROFILE_UPDATE));
            params.put("defaultToken", Constants.DEFAULT_TOKEN);
            params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
            params.put("flag", "profile");
            params.put("firstName", etFirstName.getText().toString());
            params.put("lastName", etFirstName.getText().toString());
            params.put("dob", mCalendar == null ? etDob.getText().toString() : DateAndTimeUtil.toStringDateMy(mCalendar));
            params.put("gender", (String) rb.getTag());
            Log.d("forgot Params", params.toString());
            CommonUtil.showProgressDialog(getActivity(), "Wait...");
            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
        }
    }

    public void datePicker() {
        DatePickerDialog DatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etDob.setText(DateAndTimeUtil.toStringReadableDate(mCalendar));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker.show();
    }


    public void getUserDetail() {
        final String tag = "userDetail";
        String url = Constants.BASE_URL + "/mobile/user/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_USER_DETAIL) + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
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
                        case Constants.Events.PROFILE_UPDATE:
                            CommonUtil.alertBox(getActivity(), "", "Profile Updated Successfully");
                            getUserDetail();
                            break;
                        case Constants.Events.EVENT_USER_DETAIL:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_COUNTRY_LIST, response.optJSONObject("data").toString());
                            SharedPreferenceUtil.save();

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
