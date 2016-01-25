package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReportErrorActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    private ImageView btBack,btInfo;
    private RadioGroup rgReportError;
    private CheckBox rbPhone;
    private CheckBox rbAddress;
    private CheckBox rbShopClose;
    private CheckBox rbMenu;
    private Button btReportError;
    private EditText etReportErrorDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.report_error));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_report_error);
        rgReportError = (RadioGroup) findViewById(R.id.rgReportError);
        rbPhone = (CheckBox) findViewById(R.id.rbReportErrorPhone);
        rbAddress = (CheckBox) findViewById(R.id.rbReportErrorAddress);
        rbShopClose = (CheckBox) findViewById(R.id.rbReportErrorShopClose);
        rbMenu = (CheckBox) findViewById(R.id.rbReportErrorMenu);
        btReportError = (Button) findViewById(R.id.btnReportError);
        btReportError.setOnClickListener(this);
        etReportErrorDes = (EditText) findViewById(R.id.etReportErrorDes);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReportError:
                try {
                    if (!rbPhone.isChecked() & !rbAddress.isChecked() & !rbShopClose.isChecked() & !rbMenu.isChecked()) {
                        Toast.makeText(this, "Please select at least one error type...", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(etReportErrorDes.getText().toString())) {
                        etReportErrorDes.setError(getString(R.string.wrn_error_report));
                    } else {
                        JSONObject object = new JSONObject();
                        if (rbPhone.isChecked())
                            object.put("0", rbPhone.getText());
                        if (rbAddress.isChecked())
                            object.put("1", rbAddress.getText());
                        if (rbShopClose.isChecked())
                            object.put("2", rbShopClose.getText());
                        if (rbMenu.isChecked())
                            object.put("3", rbMenu.getText());
                        doErrorReport(object.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            default:
        }
    }

    public void getReview() {
        final String tag = "getReview";
        String url = Constants.BASE_URL + "/mobile/shopReview/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&shopId=" + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_ID, "") + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_REVIEW);
//        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    public void doErrorReport(String type) {
        final String tag = "doErrorReport";
        String url = Constants.BASE_URL + "/mobile/shop/reportError";
        Map<String, String> params = new HashMap<String, String>();
        params.put("errors", type);
        params.put("description", etReportErrorDes.getText().toString());
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_REPORT_ERROR));
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
                        case Constants.Events.EVENT_REPORT_ERROR:
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
}

