package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Checkout2Activity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    private ImageView btBack;
    TextView llChackout2DelAddress;
    private TextView tvCheckoutTotal;
    private TextView tvCheckoutPayable;
    private TextView tvCheckoutShipping;
    private TextView tvCheckoutShopName;
    private Button btCheckout;
    private Button btAfterTomorrow;
    private Button btTomorrow;
    private Button btToday;
    private TextView tvChackout2DelAddress;
    private EditText etCheckout2SelectOccasion;
    private EditText etCheckout2WriteOccasion;
    private AlertDialog alert;
    private Boolean checkCountry = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.checkout));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_checkout2);


        tvChackout2DelAddress = (TextView) findViewById(R.id.tvChackout2DelAddress);
        tvChackout2DelAddress.setOnClickListener(this);
        tvCheckoutTotal = (TextView) findViewById(R.id.tvChackout2Total);
        tvCheckoutTotal.setText(getIntent().getStringExtra("total"));
        tvCheckoutPayable = (TextView) findViewById(R.id.tvChackout2Payable);
        tvCheckoutPayable.setText(getIntent().getStringExtra("pay"));
        tvCheckoutShipping = (TextView) findViewById(R.id.tvChackout2Shipping);
        tvCheckoutShipping.setText(getIntent().getStringExtra("ship"));
        tvCheckoutShopName = (TextView) findViewById(R.id.tvChackout2ShopName);
        tvCheckoutShopName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_NAME, ""));
        btCheckout = (Button) findViewById(R.id.btnCheckout2PlaceOrder);
        btCheckout.setOnClickListener(this);
        btToday = (Button) findViewById(R.id.btnCheckout2Today);
        btToday.setOnClickListener(this);
        btTomorrow = (Button) findViewById(R.id.btnCheckout2Tomorrow);
        btTomorrow.setOnClickListener(this);
        btAfterTomorrow = (Button) findViewById(R.id.btnCheckout2Tomorrow);
        btAfterTomorrow.setOnClickListener(this);
        etCheckout2WriteOccasion = (EditText) findViewById(R.id.etCheckout2WriteOccasion);
        etCheckout2SelectOccasion = (EditText) findViewById(R.id.etCheckout2SelectOccasion);
        etCheckout2SelectOccasion.setOnClickListener(this);
        getOccasion();
    }

    public void openPopupTimeSlot(JSONArray timeSlot, int dateSelect) {
        try {


            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_pickup_time_slot, null);
            alertDialogs.setView(dialog);

            TextView tvPickupDate = (TextView) dialog.findViewById(R.id.tvPickupDate);
//            Calendar calendar = Calendar.getInstance();
//            switch (dateSelect) {
//                case 1:
////                    + DateAndTimeUtil.getTimeSlotDate(calendar)
//                    tvPickupDate.setText("Today - " +DateAndTimeUtil.toStringReadableDate(calendar));
//                    break;
//                case 2:
//                    calendar.add(Calendar.DAY_OF_MONTH, 1);
//                    tvPickupDate.setText("Tomorrow - " + DateAndTimeUtil.getTimeSlotDate(calendar));
//                    break;
//                case 3:
//                    calendar.add(Calendar.DAY_OF_MONTH, 2);
//                    tvPickupDate.setText("Day After Tomorrow - " + DateAndTimeUtil.getTimeSlotDate(calendar));
//                    break;
//                default:
//                    tvPickupDate.setText("");
//            }
            final RadioGroup rgPickUp = (RadioGroup) dialog.findViewById(R.id.rgPickUp);
            TextView tvPickupCancel = (TextView) dialog.findViewById(R.id.tvPickupCancel);
            tvPickupCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });
            TextView tvPickupSet = (TextView) dialog.findViewById(R.id.tvPickupSet);
            tvPickupSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton rb = (RadioButton) rgPickUp.findViewById(rgPickUp.getCheckedRadioButtonId());
                    btToday.setText(rb.getText());
                    btToday.setTag(rb.getId());
                    alert.dismiss();
                }
            });

            RadioButton[] radioButton = new RadioButton[timeSlot.length()];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < timeSlot.length(); i++) {
                radioButton[i] = new RadioButton(this);
                radioButton[i].setLayoutParams(params);
                radioButton[i].setText(timeSlot.optJSONObject(i).optString("slot"));
                radioButton[i].setId(timeSlot.optJSONObject(i).optInt("id"));
                radioButton[i].setGravity(Gravity.CENTER);
                radioButton[i].setPadding(15, 15, 15, 15);
                rgPickUp.addView(radioButton[i]);
            }
//            rbReview=(Rating) dialog.findViewById(R.id.rbReview);
//            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnContinueCheckout:
                    startActivity(new Intent(this, Checkout2Activity.class));
                    break;
                case R.id.btnCheckout2Today:
                case R.id.btnCheckout2Tomorrow:
                case R.id.btnCheckout2AfterTomorrow:
                    getTimeSlot();
                    break;
                case R.id.btActionBarBack:
                    finish();
                    break;
                case R.id.tvChackout2DelAddress:
                    startActivityForResult(new Intent(this, MyAddressActivity.class), 0);
                    break;
                case R.id.etCheckout2SelectOccasion:
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.OCCASIONS, "").equalsIgnoreCase(""))
                        getOccasion();
                    else
                        openCountryPopup(new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.OCCASIONS, "")));
                    break;

                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openCountryPopup(JSONObject response) {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.dialog_city_select, null);
            TextView tvCityDialogHead = (TextView) dialog.findViewById(R.id.tvCityDialogHead);
            tvCityDialogHead.setText("Select Occasion");
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            if (response.getJSONObject("data").getJSONArray("occasions").length() == 0) {
                CommonUtil.alertBox(this, "", "Occasions Not Found Please Try Again.");
            } else {
//                etCheckout2SelectOccasion.setText(response.getJSONObject("data").getJSONArray("occasions").getJSONObject(0).optString("name"));
//                etCheckout2SelectOccasion.setTag(response.getJSONObject("data").getJSONArray("occasions").getJSONObject(0).optString("id"));

                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(this, response.getJSONObject("data").getJSONArray("occasions"), false);
                list_view.setAdapter(countryCodeAdapter);

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                        JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                        etCheckout2SelectOccasion.setText(getCodeObj.optString("name"));
                        etCheckout2SelectOccasion.setTag(getCodeObj.optString("id"));
                        alert.dismiss();

                    }
                });
                alertDialogs.setView(dialog);
                alertDialogs.setCancelable(false);
                alert = alertDialogs.create();
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void doCheckout() {
        final String tag = "doCheckout";
        String url = Constants.BASE_URL + "/mobile/order/create";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_CHECK_OUT));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("occasionId", "");
        params.put("senderName", "");
        params.put("message", "");
        params.put("deliveryAddressId", "");
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    public void getOccasion() {
        final String tag = "occasion";
        String url = Constants.BASE_URL + "/mobile/occasion/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_OCCASION);
        if (checkCountry) {
            CommonUtil.showProgressDialog(this, "Wait...");
        }
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getTimeSlot() {
        final String tag = "timeSlot";
        String url = Constants.BASE_URL + "/mobile/timeSlot/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_TIMESLOT);
//        if (checkCountry) {
//            CommonUtil.showProgressDialog(this, "Wait...");
//        }
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
                        case Constants.Events.EVENT_GET_OCCASION:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.OCCASIONS, response.toString());
                            SharedPreferenceUtil.save();
                            if (checkCountry)
                                openCountryPopup(response);
                            checkCountry = true;
                            if (response.getJSONObject("data").getJSONArray("occasions").length() != 0) {
                                etCheckout2SelectOccasion.setText(response.getJSONObject("data").getJSONArray("occasions").getJSONObject(0).optString("name"));
                                etCheckout2SelectOccasion.setTag(response.getJSONObject("data").getJSONArray("occasions").getJSONObject(0).optString("id"));
                            }
                            break;
                        case Constants.Events.EVENT_GET_TIMESLOT:
                            openPopupTimeSlot(response.getJSONObject("data").getJSONArray("timeSlots"), 2);
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
