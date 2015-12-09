package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.CityAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class CitySelectActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private ListView lvCity;
    EditText etCitySearch;
    CityAdapter cityAdapter = null;
    JSONObject tempCityObj = new JSONObject();
    boolean flagMode = true;
    TextView titleTextView;
    View v;
    private JSONObject cityObj;

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
        etCitySearch = (EditText) findViewById(R.id.etCitySearch);
        lvCity = (ListView) findViewById(R.id.lvCity);
        String temp = "{\n" +
                "    \"eventId\"    : 123,\n" +
                "    \"errorCode\"    : 0,\n" +
                "    \"status\"    : 200,\n" +
                "    \"message\"    : \"city list with areas.\",\n" +
                "    \"data\"        : [{\n" +
                "        \"id\": \"1\", \n" +
                "\"name\": \"Ahmedabad\",\n" +
                "        \"areas\": [{\n" +
                "        \"id\": \"1\",\n" +
                "        \"name\": \"Prahlad nagar\",\n" +
                "        \"pincode\": \"380004\",\n" +
                "        \"city_id\": \"1\"\n" +
                "    }, {\n" +
                "        \"id\": \"4\",\n" +
                "        \"name\": \"Kalupur\",\n" +
                "        \"pincode\": \"380005\",\n" +
                "        \"city_id\": \"1\"\n" +
                "    }, {\n" +
                "        \"id\": \"5\",\n" +
                "        \"name\": \"Memnagar\",\n" +
                "        \"pincode\": \"898434\",\n" +
                "        \"city_id\": \"1\"\n" +
                "    }]\n" +
                "}]\n" +
                "}\n";
        try {
            cityObj = new JSONObject(temp);
            cityFill(cityObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                try {
                    JSONObject getCityObj = (JSONObject) adapterView.getItemAtPosition(position);
                    if (flagMode) {
                        //select city
                        tempCityObj = getCityObj;
                        flagMode = false;
                        titleTextView.setText(getResources().getString(R.string.screen_area)+tempCityObj.optString("name"));
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


                if (s != " ") {
                    cityAdapter.getFilter().filter(s.toString());
                }
//                    cityListAdaptor.getFilter().filter(s);
//                    cityListAdaptor.notifyDataSetInvalidated();
//                    cityListAdaptor.notifyDataSetChanged();
//                    lv_search_city.setAdapter(cityListAdaptor);

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {

                // TODO Auto-generated  method stub
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.etEnterCountryCode:

                break;
            case R.id.btActionBarBack:
                if(titleTextView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.screen_city)))
                        finish();
                else{
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
            cityAdapter = new CityAdapter(this, address.getJSONArray("data"), true);
            lvCity.setAdapter(cityAdapter);
//            setListViewHeightBasedOnChildren(lvCity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
