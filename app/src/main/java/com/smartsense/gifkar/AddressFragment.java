package com.smartsense.gifkar;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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

public class AddressFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText etArea, etCity, etPincode, etStreet, etFlatNo, etCountry;
    Button btnProfileAddAddress;
    ImageView btBack;
    AlertDialog alert;
    JSONArray cityArr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_profile_address, container, false);
        etArea = (EditText) view.findViewById(R.id.etProfileArea);
        etCity = (EditText) view.findViewById(R.id.etProfileCity);
        etCity.setOnClickListener(this);
        etPincode = (EditText) view.findViewById(R.id.etProfilePinCode);
        etStreet = (EditText) view.findViewById(R.id.etProfileStreet);
        etFlatNo = (EditText) view.findViewById(R.id.etProfileFlatNo);
        etCountry = (EditText) view.findViewById(R.id.etProfileCountry);
        etCountry.setOnClickListener(this);
        btnProfileAddAddress = (Button) view.findViewById(R.id.btnProfileAddAddress);
        btnProfileAddAddress.setOnClickListener(this);
        try {
            JSONObject userInfo = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_INFO, ""));
            userInfo = userInfo.optJSONObject("userDetails");
            etFlatNo.setText(userInfo.optString("landmark"));
            etStreet.setText(userInfo.optString("address"));
            etArea.setText(userInfo.optString("area"));
            etCity.setText(userInfo.optString("city"));
            etCity.setTag(userInfo.optString("city_id"));
            etCountry.setText(userInfo.optString("country"));
            etCountry.setTag(userInfo.optString("country_id"));
            etPincode.setText(userInfo.optString("pincode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getCountryList();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfileAddAddress:
                doUpdate();
                break;
            case R.id.etProfileCity:
                openCityPopup();
                break;
            case R.id.etProfileCountry:
                openCountryPopup();
                break;
            default:
        }
    }

    public void doUpdate() {
        if (TextUtils.isEmpty(etFlatNo.getText().toString())) {
            etFlatNo.setError(getString(R.string.wrn_fname));
        } else if (TextUtils.isEmpty(etStreet.getText().toString())) {
            etStreet.setError(getString(R.string.wrn_lname));
        } else if (TextUtils.isEmpty(etArea.getText().toString())) {
            etArea.setError(getString(R.string.wrn_area));
        } else if (TextUtils.isEmpty(etCountry.getText().toString())) {
            etCountry.setError(getString(R.string.wrn_country));
        } else if (TextUtils.isEmpty(etPincode.getText().toString())) {
            etPincode.setError(getString(R.string.wrn_pincode));
        } else {
            final String tag = "doUpdate";
            String url = Constants.BASE_URL + "/mobile/user/update";
            Map<String, String> params = new HashMap<String, String>();
            params.put("eventId", String.valueOf(Constants.Events.PROFILE_UPDATE));
            params.put("defaultToken", Constants.DEFAULT_TOKEN);
            params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
            params.put("flag", "address");
            params.put("address", etStreet.getText().toString());
            params.put("landmark", etFlatNo.getText().toString());
            params.put("area", etArea.getText().toString());
            params.put("pincode", etPincode.getText().toString());
            params.put("cityId", (String) etCity.getTag());
            params.put("countryId", (String) etCountry.getTag());
            Log.d("Params", params.toString());
            CommonUtil.showProgressDialog(getActivity(), "Wait...");
            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
        }
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
                            JsonErrorShow.diloagMsgShow(getActivity(),"Address updated successfully.");
                            getUserDetail();
                            break;
                        case Constants.Events.EVENT_COUNTRY_LIST:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_COUNTRY_LIST, response.toString());
                            SharedPreferenceUtil.save();
                            if (response.getJSONObject("data").getJSONArray("countries").length() != 0) {
                                etCountry.setText(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("name"));
                                etCountry.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("id"));
                                if (response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optJSONArray("cities").length() != 0) {
                                    cityArr = response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optJSONArray("cities");
                                    etCity.setText(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optJSONArray("cities").getJSONObject(0).optString("name"));
                                    etCity.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optJSONArray("cities").getJSONObject(0).optString("id"));
                                }
                            }
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

    public void getCountryList() {
        final String tag = "countryList";
        String url = Constants.BASE_URL + "/mobile/country/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&country=withCities&eventId=" + String.valueOf(Constants.Events.EVENT_COUNTRY_LIST);
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void openCountryPopup() {
        try {
            JSONObject response = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_COUNTRY_LIST, ""));
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (response.getJSONObject("data").getJSONArray("countries").length() == 0) {
                CommonUtil.alertBox(getActivity(), "", "Country Not Found Please Try Again.");
            } else {
                etCountry.setText(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("name"));
                etCountry.setTag(response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optString("id"));
                cityArr = response.getJSONObject("data").getJSONArray("countries").getJSONObject(0).optJSONArray("cities");
                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(), response.getJSONObject("data").getJSONArray("countries"), false);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etCountry.setText(getCodeObj.optString("name"));
                        etCountry.setTag(getCodeObj.optString("id"));
                        if (getCodeObj.optJSONArray("cities").length() != 0) {
                            try {
                                cityArr = getCodeObj.optJSONArray("cities");
                                etCity.setText(getCodeObj.optJSONArray("cities").getJSONObject(0).optString("name"));
                                etCity.setTag(getCodeObj.optJSONArray("cities").getJSONObject(0).optString("id"));
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

    public void openCityPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (cityArr.length() == 0) {
                CommonUtil.alertBox(getActivity(), "", "City Not Found Please Try Again.");
            } else {
//                etCity.setText(cityArr.getJSONObject(0).optString("name"));
//                etCity.setTag(cityArr.getJSONObject(0).optString("id"));
                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(), cityArr, false);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etCity.setText(getCodeObj.optString("name"));
                        etCity.setTag(getCodeObj.optString("id"));
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
}
