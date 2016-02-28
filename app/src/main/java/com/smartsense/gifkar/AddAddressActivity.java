package com.smartsense.gifkar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etArea, etNo, etPincode, etStreet, etFlatNo, etName, etLandmark;
    Button btnAddAddress;
    ImageView btBack;
    JSONObject addressObj;
    Boolean check = true;
    android.app.AlertDialog alert;
    JSONArray cityArr;
    private ImageView btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        try {
            if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYADDRESS) {
                titleTextView.setText(getResources().getString(R.string.screen_edit_address));
                addressObj = new JSONObject(getIntent().getStringExtra("Address"));
            } else
                titleTextView.setText(getResources().getString(R.string.screen_add_address));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
//        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
//        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_add_address);
        etNo = (EditText) findViewById(R.id.etMyAddressAddNo);
        etArea = (EditText) findViewById(R.id.etMyAddressAddArea);
        etPincode = (EditText) findViewById(R.id.etMyAddressAddPinCode);
        etStreet = (EditText) findViewById(R.id.etMyAddressAddStreet);
        etFlatNo = (EditText) findViewById(R.id.etMyAddressAddFlatNo);
        etName = (EditText) findViewById(R.id.etMyAddressAddName);
        etLandmark = (EditText) findViewById(R.id.etMyAddressAddLandmark);
        btnAddAddress = (Button) findViewById(R.id.btnMyAddressAddAddress);
        btnAddAddress.setOnClickListener(this);
        if (getIntent().getIntExtra(Constants.SCREEN, 1) == Constants.ScreenCode.SCREEN_MYADDRESS) {
            check = false;
            btnAddAddress.setText(getResources().getString(R.string.update));
            etArea.setText(addressObj.optJSONObject("city").optString("name"));
            etPincode.setText(addressObj.optJSONObject("area").optString("name") + " " + addressObj.optJSONObject("area").optString("pincode"));
            etNo.setText(addressObj.optString("recipientContact"));
            etStreet.setText(addressObj.optString("companyName"));
            etFlatNo.setText(addressObj.optString("address"));
            etName.setText(addressObj.optString("recipientName"));
            etLandmark.setText(addressObj.optString("landmark"));
            etPincode.setTag(addressObj.optJSONObject("city").optString("id"));
            etArea.setTag(addressObj.optJSONObject("area").optString("id"));
        }

        if (getIntent().getBooleanExtra("area", false)) {
            etPincode.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_NAME, "") + " " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_PIN_CODE, ""));
            etPincode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etPincode.setTag(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_ID, ""));
            etArea.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CITY_NAME, ""));
            etArea.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etArea.setTag(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CITY_ID, ""));
        } else {
            getCityList();
            etPincode.setOnClickListener(this);
            etArea.setOnClickListener(this);
            etPincode.setEnabled(true);
            etArea.setEnabled(true);
            etArea.setFocusable(false);
            etPincode.setFocusable(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMyAddressAddAddress:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError(getString(R.string.wrn_reception_name));
                } else if (TextUtils.isEmpty(etNo.getText().toString())) {
                    etNo.setError(getString(R.string.wrn_reception_no));
                } else if (etNo.length() != 10) {
                    etNo.setError(getString(R.string.wrn_valid_mno));
                } else if (TextUtils.isEmpty(etFlatNo.getText().toString())) {
                    etFlatNo.setError(getString(R.string.wrn_flat));
                } else if (TextUtils.isEmpty(etStreet.getText().toString())) {
                    etStreet.setError(getString(R.string.wrn_street));
                } else if (TextUtils.isEmpty(etLandmark.getText().toString())) {
                    etLandmark.setError(getString(R.string.wrn_land));
                } else
                    addAddress(check);
                break;
            case R.id.btActionBarBack:
                CommonUtil.closeKeyboard(AddAddressActivity.this);
                finish();
                break;
            case R.id.etMyAddressAddPinCode:
                openAreaPopup();
                break;
            case R.id.etMyAddressAddArea:
                openCityPopup();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            default:
        }
    }

    public void addAddress(Boolean check) {
        final String tag = "address";
        String url;
        Map<String, String> params = new HashMap<String, String>();
        if (check) {
            url = Constants.BASE_URL + "/mobile/deliveryAddress/create";
            params.put("eventId", String.valueOf(Constants.Events.EVENT_ADD_ADDRESS));
        } else {
            url = Constants.BASE_URL + "/mobile/deliveryAddress/update";
            params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE));
            params.put("addressId", addressObj.optString("id"));
        }
        params.put("recipientName", etName.getText().toString());
        params.put("recipientContact", etNo.getText().toString());
        params.put("address", etFlatNo.getText().toString());
        params.put("companyName", etStreet.getText().toString());
        params.put("landmark", etLandmark.getText().toString());
        params.put("areaId", (String) etPincode.getTag());
        params.put("cityId", (String) etArea.getTag());
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getCityList() {
        final String tag = "cityList";
        String url = Constants.BASE_URL + "/mobile/city/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_CITY);
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_ADD_ADDRESS:
                            CommonUtil.closeKeyboard(AddAddressActivity.this);
                            CommonUtil.alertBox(AddAddressActivity.this,"","Address Successfully Added.");
//                            alert.setTitle("Success!");
//                            alert.setMessage("Address Successfully Added.");
//                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
//                                }
//                            });
//                            alert.show();
                            break;
                        case Constants.Events.EVENT_UPDATE:
                            CommonUtil.closeKeyboard(AddAddressActivity.this);
                            CommonUtil.alertBox(AddAddressActivity.this,"","Address Successfully Updated.");
//                            alert.setTitle("Success!");
//                            alert.setMessage("Address Successfully Updated.");
//                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
//                                }
//                            });
//                            alert.show();
                            break;
                        case Constants.Events.EVENT_CITY:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_COUNTRY_LIST, response.toString());
                            SharedPreferenceUtil.save();
                            if (getIntent().getIntExtra(Constants.SCREEN, 1) != Constants.ScreenCode.SCREEN_MYADDRESS) {
                                if (response.getJSONObject("data").getJSONArray("cities").length() != 0) {
                                    etArea.setText(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optString("name"));
                                    etArea.setTag(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optString("id"));
                                    if (response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas").length() != 0) {
                                        cityArr = response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas");
                                        etPincode.setText(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas").getJSONObject(0).optString("name") + " " + response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas").getJSONObject(0).optString("pincode"));
                                        etPincode.setTag(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas").getJSONObject(0).optString("id"));
                                    }
                                }
                            }
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, AddAddressActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void openCityPopup() {
        try {
            JSONObject response = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_COUNTRY_LIST, ""));
            final android.app.AlertDialog.Builder alertDialogs = new android.app.AlertDialog.Builder(AddAddressActivity.this);
            LayoutInflater inflater = (LayoutInflater) AddAddressActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (response.getJSONObject("data").getJSONArray("cities").length() == 0) {
                CommonUtil.alertBox(AddAddressActivity.this, "", "City Not Found Please Try Again.");
            } else {
                etArea.setText(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optString("name"));
                etArea.setTag(response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optString("id"));
                cityArr = response.getJSONObject("data").getJSONArray("cities").getJSONObject(0).optJSONArray("areas");
                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(AddAddressActivity.this, response.getJSONObject("data").getJSONArray("cities"), false);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etArea.setText(getCodeObj.optString("name"));
                        etArea.setTag(getCodeObj.optString("id"));
                        if (getCodeObj.optJSONArray("areas").length() != 0) {
                            try {
                                cityArr = getCodeObj.optJSONArray("areas");
                                etPincode.setText(getCodeObj.optJSONArray("areas").getJSONObject(0).optString("name") + " " + getCodeObj.optJSONArray("areas").getJSONObject(0).optString("pincode"));
                                etPincode.setTag(getCodeObj.optJSONArray("areas").getJSONObject(0).optString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        alert.dismiss();

                    }
                });
                alertDialogs.setView(dialog);
                alertDialogs.setCancelable(true);
                alert = alertDialogs.create();
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAreaPopup() {
        try {
            final android.app.AlertDialog.Builder alertDialogs = new android.app.AlertDialog.Builder(AddAddressActivity.this);
            LayoutInflater inflater = (LayoutInflater) AddAddressActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (cityArr.length() == 0) {
                CommonUtil.alertBox(AddAddressActivity.this, "", "Area Not Found Please Try Again.");
            } else {
//                etCity.setText(cityArr.getJSONObject(0).optString("name"));
//                etCity.setTag(cityArr.getJSONObject(0).optString("id"));
                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(AddAddressActivity.this, cityArr, false);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etPincode.setText(getCodeObj.optString("name") + " " + getCodeObj.optString("pincode"));
                        etPincode.setTag(getCodeObj.optString("id"));
                        alert.dismiss();

                    }
                });
                alertDialogs.setView(dialog);
                alertDialogs.setCancelable(true);
                alert = alertDialogs.create();
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openInfoPopup() {
        try {
            final android.app.AlertDialog.Builder alertDialogs = new android.app.AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            TextView tvDialog=(TextView) dialog.findViewById(R.id.textInfoDialog);
            tvDialog.setText(getResources().getString(R.string.del_des));
            android.app.AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
