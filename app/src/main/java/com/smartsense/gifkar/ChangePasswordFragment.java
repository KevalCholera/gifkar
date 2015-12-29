package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etOldPass, etConPass, etNewPass;
    Button btChangePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_change_password, container, false);

        etOldPass = (EditText) view.findViewById(R.id.etChangePassoldPassword);
        etConPass = (EditText) view.findViewById(R.id.etChangePassConfirmPassword);
        etNewPass = (EditText) view.findViewById(R.id.etChangePassNewPassword);
        btChangePass = (Button) view.findViewById(R.id.btnChangePass);
        btChangePass.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePass:
                if (TextUtils.isEmpty(etOldPass.getText().toString())) {
                    etOldPass.setError(getString(R.string.wrn_oldpwd));
                } else if (TextUtils.isEmpty(etConPass.getText().toString())) {
                    etConPass.setError(getString(R.string.wrn_repwd));
                } else if (TextUtils.isEmpty(etConPass.getText().toString())) {
                    etConPass.setError(getString(R.string.wrn_repwd));
                } else if (!TextUtils.equals(etNewPass.getText().toString(), etConPass.getText().toString())) {
                    etConPass.setError(getString(R.string.wrn_match));
                } else {
                    doPassword(etOldPass.getText().toString(), etNewPass.getText().toString());
                }
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
        params.put("flag", "password");
        params.put("oldPassword", oldPass);
        params.put("newPassword", newPass);
        Log.d("forgot Params", params.toString());
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
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
                        case Constants.Events.EVENT_FORGOT_PASS:
                            etOldPass.setText("");
                            etConPass.setText("");
                            etNewPass.setText("");
                            CommonUtil.alertBox(getActivity(), "", "Your Password Successfully Changed.");
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
