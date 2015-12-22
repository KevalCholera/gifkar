package com.smartsense.gifkar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.gifkar.adapter.CityAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;
import com.smartsense.gifkar.utill.LocationFinderService;
import com.smartsense.gifkar.utill.LocationSettingsHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class CitySelectActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    private ImageView btBack;
    private ListView lvCity;
    EditText etCitySearch;
    CityAdapter cityAdapter = null;
    JSONObject tempCityObj = new JSONObject();
    boolean flagMode = true;
    TextView titleTextView;
    LinearLayout llAutoSeletCity;
    View v;
    private JSONObject cityObj;
    private LocationSettingsHelper mSettingsHelper;
    String pin_code = "";
    LocationFinderService locationFinderService;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        v = inflater.inflate(R.layout.action_bar_center, null);
        titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_city));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_city_select);
        llAutoSeletCity = (LinearLayout) findViewById(R.id.llAutoSeletCity);
        llAutoSeletCity.setOnClickListener(this);
        etCitySearch = (EditText) findViewById(R.id.etCitySearch);
        lvCity = (ListView) findViewById(R.id.lvCity);
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                try {
                    JSONObject getCityObj = (JSONObject) adapterView.getItemAtPosition(position);
                    if (flagMode) {
                        //select city
                        tempCityObj = getCityObj;
                        flagMode = false;
                        titleTextView.setText(getResources().getString(R.string.screen_area) + tempCityObj.optString("name"));
                        cityAdapter = new CityAdapter(CitySelectActivity.this, getCityObj.getJSONArray("areas"), false);
//                        setListViewHeightBasedOnChildren(lvCity);
                    } else {
                        //select area
//                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_NAME, getCityObj.optString("area_name"));
//                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_ID, getCityObj.optString("area_id"));
//                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CITY_NAME, tempCityObj.optString("city_name"));
                        flagMode = true;
                        Log.i("getCity", getCityObj.toString());
                        startActivity(new Intent(CitySelectActivity.this, GifkarActivity.class));
                    }
//                    SharedPreferenceUtil.save();
                    lvCity.setAdapter(cityAdapter);
                    etCitySearch.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        etCitySearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) { // TODO Auto-generated method stub
                if (s != "") {
                    cityAdapter.getFilter().filter(s.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        getCityList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etEnterCountryCode:

                break;
            case R.id.llAutoSeletCity:
                if (CommonUtil.isGPS(getApplicationContext())) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        getPinCode();
                    }
                } else {
                    mSettingsHelper = new LocationSettingsHelper(this);
                    mSettingsHelper.checkSettings();
                }
                break;
            case R.id.btActionBarBack:
                if (titleTextView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.screen_city)))
                    finish();
                else {
                    titleTextView.setText(getResources().getString(R.string.screen_city));
                    flagMode = true;
                    cityFill(cityObj);
                }
                break;
            default:
        }
    }

    public void cityFill(JSONObject address) {
        try {
            cityAdapter = new CityAdapter(this, address.getJSONObject("data").getJSONArray("cities"), true);
            lvCity.setAdapter(cityAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPinCode() {
        locationFinderService = new LocationFinderService(this);
        Location location = locationFinderService.getLastKnownLocation();
        if (location != null) {
            String url = "http://ws.geonames.org/findNearbyPostalCodesJSON?formatted=true&lat=" + location.getLatitude()
                    + "&lng=" + location.getLongitude() + "&username=comeback4you";
            Log.i("url", url);
            DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pin_code = response.optJSONArray("postalCodes").optJSONObject(0)
                                    .optString("postalCode");
                            Log.e("Volley Request", pin_code);
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonUtil.cancelProgressDialog();
                    Log.e("Volley Request Error", "Error");
                }

            });
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GifkarApp.getInstance().addToRequestQueue(loginRequest, "volley");
        } else {
//            getPinCode();
            CommonUtil.alertBox(this, "", "Location not found.");
        }
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
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_CITY:
                            cityObj = response;
                            cityFill(cityObj);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationSettingsHelper.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            getPinCode();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        CommonUtil.alertBox(this, "", "Please turn on your devices gps to get your current location.");
                        break;
                    default:
                        break;
                }
                break;

        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            showMessageOKCancel("You need to allow access to Location",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_CODE);
                            }
                        }
                    });

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPinCode();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
