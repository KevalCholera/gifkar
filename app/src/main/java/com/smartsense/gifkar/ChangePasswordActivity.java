package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etOldPass, etConPass, etNewPass;
    Button btChangePass;
    ImageView btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_new_pass));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_change_password);


        etOldPass = (EditText) findViewById(R.id.etChangePassoldPassword);
        if (getIntent().getIntExtra(Constants.SCREEN, 0) == Constants.ScreenCode.SCREEN_OTP)
            etOldPass.setVisibility(View.GONE);
        etConPass = (EditText) findViewById(R.id.etChangePassConfirmPassword);
        etNewPass = (EditText) findViewById(R.id.etChangePassNewPassword);
        btChangePass = (Button) findViewById(R.id.btnChangePass);
        btChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePass:
//                startActivity(new Intent(this, GifkarActivity.class));
                if (TextUtils.isEmpty(etNewPass.getText().toString())) {
                    etNewPass.setError(getString(R.string.wrn_pwd));
                } else if (TextUtils.isEmpty(etConPass.getText().toString())) {
                    etConPass.setError(getString(R.string.wrn_repwd));
                } else if (!TextUtils.equals(etNewPass.getText().toString(), etConPass.getText().toString())) {
                    etConPass.setError(getString(R.string.wrn_match));
                } else {
                    doPassword(etOldPass.getText().toString(), etNewPass.getText().toString());
                }
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public void doPassword(String oldPass, String newPass) {
        final String tag = "doPasswordSet";
        String url = Constants.BASE_URL + "/mobile/user/update";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_FORGOT_PASS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        if (getIntent().getIntExtra(Constants.SCREEN, 0) == Constants.ScreenCode.SCREEN_OTP) {
            params.put("flag", "forgottenPassword");
            params.put("password", newPass);
        } else {
            params.put("flag", "password");
            params.put("oldPassword", oldPass);
            params.put("newPassword", newPass);
        }
        Log.d("forgot Params", params.toString());
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
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_FORGOT_PASS:
                            CommonUtil.alertBox(this, "", "Your Password Successfully Changed.");
                            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                                startActivity(new Intent(this, GifkarActivity.class));
                            else
                                startActivity(new Intent(this, CitySelectActivity.class));
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
}
