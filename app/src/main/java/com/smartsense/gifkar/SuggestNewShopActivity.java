package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SuggestNewShopActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    private Button btnAddShop;
    private EditText etEmail;
    private EditText etShopName;
    private EditText etAddress;
    private EditText etCity;
    private EditText etArea;
    private EditText etWeblink;
    private EditText etPincode;
    private ImageView btBack;
    private ImageView btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_suggest));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
//        btInfo.setBackground(getResources().getDrawable(R.drawable.));
        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();//first get parent toolbar of current action bar
//        parent.setContentInsetsAbsolute(0, 0);
        setContentView(R.layout.activity_suggest_new_shop);
        etShopName = (EditText) findViewById(R.id.etNewShopName);
        etAddress = (EditText) findViewById(R.id.etNewShopAddress);
        etArea = (EditText) findViewById(R.id.etNewShopArea);
        etCity = (EditText) findViewById(R.id.etNewShopCity);
        etWeblink = (EditText) findViewById(R.id.etNewShopWeblink);
        etPincode = (EditText) findViewById(R.id.etNewShopPin);
        etEmail = (EditText) findViewById(R.id.etNewShopEmail);
        btnAddShop = (Button) findViewById(R.id.btnNewShopAdd);
        btnAddShop.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewShopAdd:
                if (TextUtils.isEmpty(etShopName.getText().toString())) {
                    etShopName.setError(getString(R.string.wrn_shop_name));
                } else if (TextUtils.isEmpty(etAddress.getText().toString())) {
                    etAddress.setError(getString(R.string.wrn_shop_address));
                } else if (TextUtils.isEmpty(etArea.getText().toString())) {
                    etArea.setError(getString(R.string.wrn_area));
                } else if (TextUtils.isEmpty(etCity.getText().toString())) {
                    etCity.setError(getString(R.string.wrn_shop_city));
                } else if (TextUtils.isEmpty(etWeblink.getText().toString())) {
                    etWeblink.setError(getString(R.string.wrn_web_link));
                } else if (TextUtils.isEmpty(etPincode.getText().toString())) {
                    etPincode.setError(getString(R.string.wrn_pincode));
                } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    etEmail.setError(getString(R.string.wrn_em));
                } else if (!CommonUtil.isValidEmail(etEmail.getText().toString())) {
                    etEmail.setError(getString(R.string.wrn_email));
                } else
                    suggestShop();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            case R.id.btActionBarBack:
                CommonUtil.closeKeyboard(SuggestNewShopActivity.this);
                finish();
                break;
            default:
        }
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void suggestShop() {
        final String tag = "suggestShop";
        String url = Constants.BASE_URL + "/mobile/shop/suggest";
        Map<String, String> params = new HashMap<String, String>();
        params.put("shopName", etShopName.getText().toString());
        params.put("address", etAddress.getText().toString());
        params.put("area", etArea.getText().toString());
        params.put("city", etCity.getText().toString());
        params.put("pincode", etPincode.getText().toString());
        params.put("website", etWeblink.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_ADDRESS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
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
                        case Constants.Events.EVENT_DEL_ADDRESS:
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                            alertbox.setCancelable(false);
                            alertbox.setMessage(response.optString("message"));
                            alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }

                            });
                            alertbox.show();

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
