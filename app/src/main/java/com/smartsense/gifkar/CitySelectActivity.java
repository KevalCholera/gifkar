package com.smartsense.gifkar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
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
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CityAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;
import com.smartsense.gifkar.utill.LocationFinderService;
import com.smartsense.gifkar.utill.LocationSettingsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_NAME, getCityObj.optString("name"));
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_ID, getCityObj.optString("id"));
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_PIN_CODE, getCityObj.optString("pincode"));
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CITY_ID, tempCityObj.optString("id"));
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CITY_NAME, tempCityObj.optString("name"));
                        SharedPreferenceUtil.save();
                        flagMode = true;
//                        Log.i("getCity", getCityObj.toString());
                        startActivity(new Intent(CitySelectActivity.this, GifkarActivity.class));
                        finish();
                    }
                    lvCity.setAdapter(cityAdapter);
                    etCitySearch.setText("");
                    etCitySearch.setHint(getResources().getString(R.string.delivery_area));
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            getPinCode();
                        }
                    } else {
                        getPinCode();
                    }
                } else {
                    mSettingsHelper = new LocationSettingsHelper(this);
                    mSettingsHelper.checkSettings();
                }
                break;
            case R.id.btActionBarBack:
                if (titleTextView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.screen_city))|getIntent().getBooleanExtra("area", false))
                    finish();
                else {
                    titleTextView.setText(getResources().getString(R.string.screen_city));
                    etCitySearch.setHint(getResources().getString(R.string.delivery_hint));
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
            if (getIntent().getBooleanExtra("area", false)) {
                for (int i = 0; i < address.getJSONObject("data").getJSONArray("cities").length(); i++) {
                    Log.i(address.getJSONObject("data").getJSONArray("cities").optJSONObject(i).optString("id"), SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CITY_ID, ""));
                    if (address.getJSONObject("data").getJSONArray("cities").optJSONObject(i).optString("id").equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CITY_ID, "")))
                        lvCity.performItemClick(
                                lvCity.getAdapter().getView(i, null, null),
                                i,
                                lvCity.getAdapter().getItemId(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPinCode() {
        CommonUtil.showProgressDialog(this, "Wait...");
        LocationFinderService.LocationResult locationResult = new LocationFinderService.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                if (location != null) {
                    String url = "http://ws.geonames.org/findNearbyPostalCodesJSON?formatted=true&lat=" + location.getLatitude()
                            + "&lng=" + location.getLongitude() + "&username=comeback4you";
                    new GetLocationAsync(location.getLatitude(), location.getLongitude()).execute();

                } else {
//            getPinCode();
                    CommonUtil.alertBox(CitySelectActivity.this, "", "Location not found.");
                }
            }
        };
        LocationFinderService myLocation = new LocationFinderService();
        myLocation.getLocation(this, locationResult);

    }

    public void getCityList() {
        final String tag = "cityList";
        String url = Constants.BASE_URL + "/mobile/city/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_CITY);
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    //auto detect location
    public void getArea() {
        final String tag = "area";
        String url = Constants.BASE_URL + "/mobile/area/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_PINCODE) + "&pincode=" + pin_code;
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Network Problem!");
        alert.setMessage(getResources().getString(R.string.internet_error));
        alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getCityList();
            }
        });
        alert.show();
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
                        case Constants.Events.EVENT_PINCODE:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_NAME, response.getJSONObject("data").getJSONObject("area").optString("name"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_ID, response.getJSONObject("data").getJSONObject("area").optString("id"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_AREA_PIN_CODE, response.getJSONObject("data").getJSONObject("area").optString("pincode"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CITY_ID, tempCityObj.optString("id"));
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CITY_NAME, tempCityObj.optString("name"));
                            SharedPreferenceUtil.save();
                            startActivity(new Intent(CitySelectActivity.this, GifkarActivity.class));
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!checkPermission()) {
                                requestPermission();
                            } else {
                                getPinCode();
                            }
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


    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        double x, y;
        StringBuilder str;
        private List<Address> addresses;

        public GetLocationAsync(double latitude, double longitude) {
            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Geocoder geocoder = new Geocoder(CitySelectActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");
                    // Log.e("addresses", addresses.toString());
                } else {
                }
            } catch (IOException e) {
                // Log.e("tag", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (addresses.get(0).getPostalCode() == null) {
                    String url = "http://ws.geonames.org/findNearbyPostalCodesJSON?formatted=true&lat=" + x
                            + "&lng=" + y + "&username=comeback4you";
                    Log.i("url", url);
                    DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    pin_code = response.optJSONArray("postalCodes").optJSONObject(0)
                                            .optString("postalCode");
                                    getArea();
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
                    Log.i("Location", "" + addresses.get(0).getPostalCode());
                    pin_code = addresses.get(0).getPostalCode();
                    getArea();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CommonUtil.cancelProgressDialog();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
