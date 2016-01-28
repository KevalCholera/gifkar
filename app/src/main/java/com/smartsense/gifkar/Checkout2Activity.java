package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        btAfterTomorrow = (Button) findViewById(R.id.btnCheckout2AfterTomorrow);
        btAfterTomorrow.setOnClickListener(this);
        etCheckout2WriteOccasion = (EditText) findViewById(R.id.etCheckout2WriteOccasion);
        etCheckout2SelectOccasion = (EditText) findViewById(R.id.etCheckout2SelectOccasion);
        etCheckout2SelectOccasion.setOnClickListener(this);
        getOccasion();
    }

    String formattedDate;

    public void openPopupTimeSlot(JSONArray timeSlot, final int dateSelect) {
        try {


            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_pickup_time_slot, null);
            alertDialogs.setView(dialog);

            final TextView tvPickupDate = (TextView) dialog.findViewById(R.id.tvPickupDate);
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");

            switch (dateSelect) {
                case 1:
                    formattedDate = df.format(calendar.getTime());
                    tvPickupDate.setText("Today - " + formattedDate);
                    break;
                case 2:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    formattedDate = df.format(calendar.getTime());
                    tvPickupDate.setText("Tomorrow - " + formattedDate);
                    break;
                case 3:
                    calendar.add(Calendar.DAY_OF_MONTH, 2);
                    formattedDate = df.format(calendar.getTime());
                    tvPickupDate.setText("Day After Tomorrow - " + formattedDate);
                    break;
                default:
                    tvPickupDate.setText("");
            }
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
                    switch (dateSelect) {
                        case 1:
                            btToday.setText(formattedDate + "\n" + rb.getText());
                            btToday.setTag(rb.getId());

                            btToday.setBackgroundResource(R.drawable.borderblue);
                            btToday.setTextColor(getResources().getColor(R.color.textcolorwhite));

                            btTomorrow.setTextColor(getResources().getColor(R.color.mainColor));
                            btAfterTomorrow.setTextColor(getResources().getColor(R.color.mainColor));
                            btTomorrow.setText(getResources().getString(R.string.tommorow_del));
                            btTomorrow.setBackgroundResource(R.drawable.borderwhite);
                            btTomorrow.setTag("");
                            btAfterTomorrow.setTag("");
                            btAfterTomorrow.setText(getResources().getString(R.string.day_after_tommorow_del));
                            btAfterTomorrow.setBackgroundResource(R.drawable.borderwhite);
                            break;
                        case 2:
                            btTomorrow.setText(formattedDate + "\n" + rb.getText());
                            btTomorrow.setTag(rb.getId());
                            btTomorrow.setBackgroundResource(R.drawable.borderblue);
                            btTomorrow.setTextColor(getResources().getColor(R.color.textcolorwhite));

                            btToday.setTextColor(getResources().getColor(R.color.mainColor));
                            btAfterTomorrow.setTextColor(getResources().getColor(R.color.mainColor));
                            btToday.setText(getResources().getString(R.string.today_del));
                            btToday.setBackgroundResource(R.drawable.borderwhite);
                            btToday.setTag("");
                            btAfterTomorrow.setTag("");
                            btAfterTomorrow.setText(getResources().getString(R.string.day_after_tommorow_del));
                            btAfterTomorrow.setBackgroundResource(R.drawable.borderwhite);
                            break;
                        case 3:
                            btAfterTomorrow.setText(formattedDate + "\n" + rb.getText());
                            btAfterTomorrow.setTag(rb.getId());
                            btAfterTomorrow.setBackgroundResource(R.drawable.borderblue);
                            btAfterTomorrow.setTextColor(getResources().getColor(R.color.textcolorwhite));

                            btTomorrow.setTextColor(getResources().getColor(R.color.mainColor));
                            btToday.setTextColor(getResources().getColor(R.color.mainColor));
                            btTomorrow.setText(getResources().getString(R.string.tommorow_del));
                            btTomorrow.setBackgroundResource(R.drawable.borderwhite);
                            btToday.setTag("");
                            btTomorrow.setTag("");
                            btToday.setText(getResources().getString(R.string.today_del));
                            btToday.setBackgroundResource(R.drawable.borderwhite);

                            break;
                        default:

                    }
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
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "").equalsIgnoreCase(""))
                        getTimeSlot();
                    else
                        openPopupTimeSlot(new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "")), 1);
                    break;
                case R.id.btnCheckout2Tomorrow:
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "").equalsIgnoreCase(""))
                        getTimeSlot();
                    else
                        openPopupTimeSlot(new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "")), 2);
                    break;
                case R.id.btnCheckout2AfterTomorrow:
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "").equalsIgnoreCase(""))
                        getTimeSlot();
                    else
                        openPopupTimeSlot(new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.TIMESLOT, "")), 3);
                    break;
                case R.id.btActionBarBack:
                    finish();
                    break;
                case R.id.tvChackout2DelAddress:
                    startActivityForResult(new Intent(this, MyAddressActivity.class).putExtra(Constants.SCREEN, true), 0);
                    break;
                case R.id.etCheckout2SelectOccasion:
                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.OCCASIONS, "").equalsIgnoreCase(""))
                        getOccasion();
                    else
                        openOccasionsPopup(new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.OCCASIONS, "")));
                    break;
                case R.id.btnCheckout2PlaceOrder:
                    if (tvChackout2DelAddress.getText().toString().equalsIgnoreCase("")) {
                        CommonUtil.alertBox(this, "", "Please select delivery address.");
                    } else if (btToday.getBackground().getConstantState()
                            .equals(getResources().getDrawable(R.drawable.borderwhite).getConstantState()) & btTomorrow.getBackground().getConstantState()
                            .equals(getResources().getDrawable(R.drawable.borderwhite).getConstantState()) & btAfterTomorrow.getBackground().getConstantState()
                            .equals(getResources().getDrawable(R.drawable.borderwhite).getConstantState())) {
                        CommonUtil.alertBox(this, "", "Please select time slot for delivery.");
                    } else if (etCheckout2WriteOccasion.getText().toString().equalsIgnoreCase("")) {
                        CommonUtil.alertBox(this, "", "Please enter occasion message.");
                    } else
                        doCheckout();
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.remove(Constants.PrefKeys.OCCASIONS);
        SharedPreferenceUtil.save();
    }

    public void openOccasionsPopup(JSONObject response) {
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
                alertDialogs.setCancelable(true);
                alert = alertDialogs.create();
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doCheckout() {
        final String tag = "doCheckout";
        String url = Constants.BASE_URL + "/mobile/orderDetail/create";
        Map<String, String> params = new HashMap<String, String>();
        params.put("eventId", String.valueOf(Constants.Events.EVENT_CHECK_OUT));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("occasionId", (String) etCheckout2SelectOccasion.getTag());
        params.put("senderName", "Ronak");
        params.put("message", etCheckout2WriteOccasion.getText().toString());
        params.put("deliveryAddressId", (String) tvChackout2DelAddress.getTag());
        params.put("products", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("y-M-d");
        String formattedDate;
        if (btToday.getBackground().getConstantState()
                .equals(getResources().getDrawable(R.drawable.borderblue).getConstantState())) {

            formattedDate = df.format(calendar.getTime());
            params.put("expectedDeliveryDate", formattedDate);
            params.put("deliveryTimeSlotId", "" + (Integer) btToday.getTag());
        }
        if (btTomorrow.getBackground().getConstantState()
                .equals(getResources().getDrawable(R.drawable.borderblue).getConstantState())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = df.format(calendar.getTime());
            params.put("expectedDeliveryDate", formattedDate);
            params.put("deliveryTimeSlotId", "" + (Integer) btTomorrow.getTag());
        }
        if (btAfterTomorrow.getBackground().getConstantState()
                .equals(getResources().getDrawable(R.drawable.borderblue).getConstantState())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            formattedDate = df.format(calendar.getTime());
            params.put("expectedDeliveryDate", formattedDate);
            params.put("deliveryTimeSlotId", "" + (Integer) btAfterTomorrow.getTag());
        }
        Log.i("params", params.toString());
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
                                openOccasionsPopup(response);
                            checkCountry = true;
                            if (response.getJSONObject("data").optJSONArray("occasions").length() != 0) {
                                etCheckout2SelectOccasion.setText(response.getJSONObject("data").optJSONArray("occasions").optJSONObject(0).optString("name"));
                                etCheckout2SelectOccasion.setTag(response.getJSONObject("data").optJSONArray("occasions").optJSONObject(0).optString("id"));
                            }
                            break;
                        case Constants.Events.EVENT_GET_TIMESLOT:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.TIMESLOT, response.optJSONObject("data").optJSONArray("timeSlots").toString());
                            SharedPreferenceUtil.save();
//                            openPopupTimeSlot(response.getJSONObject("data").getJSONArray("timeSlots"), 2);
                            break;
                        case Constants.Events.EVENT_CHECK_OUT:
//                            startActivity(new Intent(Checkout2Activity.this, OrderDetailActivity.class).putExtra("id", response.getJSONObject("data").optString("id")));
                            startActivity(new Intent(Checkout2Activity.this, OrderDetailActivity.class).putExtra("id", response.optJSONObject("data").optString("orderDetailId")));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!data.getStringExtra("address").equalsIgnoreCase("")) {
                tvChackout2DelAddress.setText(data.getStringExtra("address"));
                tvChackout2DelAddress.setTag(data.getStringExtra("addressId"));
                tvChackout2DelAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.profile_edit, 0);
            }
        }
    }
}
