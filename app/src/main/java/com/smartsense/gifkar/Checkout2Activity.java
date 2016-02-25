package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.mpt.storage.SharedPreferenceUtil;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.PaymentPostParams;
import com.payu.payuui.PaymentsActivity;
import com.smartsense.gifkar.adapter.CountryCodeAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
    private Boolean checkTimeSlot = false;

    int merchantIndex = 0;
    int env = PayuConstants.PRODUCTION_ENV;
    String merchantTestKeys[] = {"gtKFFx", "gtKFFx"};
    String merchantProductionKeys[] = {"0MQaQP", "smsplus"};
    String merchantKey = env == PayuConstants.PRODUCTION_ENV ? merchantProductionKeys[merchantIndex] : merchantTestKeys[merchantIndex];

    private PaymentParams mPaymentParams;
    private PayuConfig payuConfig;
    private String salt = "eCwWELxi";
    RequestQueue queue;
    RequestFuture<JSONObject> future;

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
            CommonUtil.closeKeyboard(this);
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
                    checkTimeSlot = true;
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
                if (i == 0)
                    radioButton[i].setChecked(true);
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
                    CommonUtil.closeKeyboard(Checkout2Activity.this);
                    finish();
                    break;
                case R.id.tvChackout2DelAddress:
                    CommonUtil.closeKeyboard(this);
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
                    } else if (!checkTimeSlot) {
                        CommonUtil.alertBox(this, "", "Please select time slot for delivery.");
                    } else if (etCheckout2WriteOccasion.getText().toString().equalsIgnoreCase("")) {
                        CommonUtil.alertBox(this, "", "Please enter occasion message.");
                    } else//doPayment();
                        doCheckout();
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //
//    & btTomorrow.getBackground().getConstantState()
//    .equals(getResources().getDrawable(R.drawable.borderwhite).getConstantState()) & btAfterTomorrow.getBackground().getConstantState()
//    .equals(getResources().getDrawable(R.drawable.borderwhite).getConstantState())
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.remove(Constants.PrefKeys.OCCASIONS);
        SharedPreferenceUtil.save();
    }

    public void openOccasionsPopup(JSONObject response) {
        try {
            CommonUtil.closeKeyboard(this);
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

    public void doPayment() {
        CommonUtil.closeKeyboard(this);
        mPaymentParams = new PaymentParams();
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(""+getIntent().getDoubleExtra("rs",0));
//        mPaymentParams.setAmount(ge);
        mPaymentParams.setProductInfo(SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_NAME, ""));
        mPaymentParams.setFirstName(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
        mPaymentParams.setEmail(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));
        mPaymentParams.setTxnId("" + System.currentTimeMillis());
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setUdf1("");
        mPaymentParams.setUdf2("");
        mPaymentParams.setUdf3("");
        mPaymentParams.setUdf4("");
        mPaymentParams.setUdf5("");
        generateHashFromServer(mPaymentParams);
    }

    public void generateHashFromServer(PaymentParams mPaymentParams) {
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));
        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }


    public void doCheckout() {
        CommonUtil.closeKeyboard(this);
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
        Log.i("params", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
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
//                            if (response.getJSONObject("data").optJSONArray("occasions").length() != 0) {
//                                etCheckout2SelectOccasion.setText(response.getJSONObject("data").optJSONArray("occasions").optJSONObject(0).optString("name"));
//                                etCheckout2SelectOccasion.setTag(response.getJSONObject("data").optJSONArray("occasions").optJSONObject(0).optString("id"));
//                            }
                            break;
                        case Constants.Events.EVENT_GET_TIMESLOT:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.TIMESLOT, response.optJSONObject("data").optJSONArray("timeSlots").toString());
                            SharedPreferenceUtil.save();
//                            openPopupTimeSlot(response.getJSONObject("data").getJSONArray("timeSlots"), 2);
                            break;
                        case Constants.Events.EVENT_CHECK_OUT:
//                            startActivity(new Intent(Checkout2Activity.this, OrderDetailActivity.class).putExtra("id", response.getJSONObject("data").optString("id")));
                            CommonUtil.closeKeyboard(this);
                            SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_PROD_LIST);
                            startActivity(new Intent(Checkout2Activity.this, OrderDetailActivity.class).putExtra("id", response.optJSONObject("data").optString("orderDetailId")).putExtra("flag", true));
                            ActivityCompat.finishAffinity(this);
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
            if (data != null) {
                if (!data.getStringExtra("address").equalsIgnoreCase("")) {
                    tvChackout2DelAddress.setText(data.getStringExtra("address"));
                    tvChackout2DelAddress.setTag(data.getStringExtra("addressId"));
                    tvChackout2DelAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.profile_edit, 0);
                }
            }
        } else if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                if (resultCode == Checkout2Activity.RESULT_OK) {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage(data.getStringExtra("result"))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    doCheckout();
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage("Payment Failed Please Re-try")//data.getStringExtra("result"))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                   /* getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, new GridViewFragment()).commit();*/
                                }
                            }).show();
                }

            } else {
                Toast.makeText(this, "Could not receive data", Toast.LENGTH_LONG).show();
            }
        }
    }


    class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {
//                URL url = new URL(PayuConstants.MOBILE_TEST_FETCH_DATA_URL);
//                        URL url = new URL("http://10.100.81.49:80/merchant/postservice?form=2");;

                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        case "get_merchant_ibibo_codes_hash": //
                            payuHashes.setMerchantIbiboCodesHash(response.getString(key));
                            break;
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        case "check_isDomestic_hash":
                            payuHashes.setCheckIsDomesticHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);
            callPayuMoney(payuHashes);
        }
    }

    public void callPayuMoney(PayuHashes payuHashes) {
        PostData postData;
        payuConfig = new PayuConfig();
        mPaymentParams.setHash(payuHashes.getPaymentHash());
        postData = new PaymentPostParams(mPaymentParams, PayuConstants.PAYU_MONEY).getPaymentPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            // launch webview
            payuConfig.setData(postData.getResult());
            payuConfig.setEnvironment(env == PayuConstants.PRODUCTION_ENV ? PayuConstants.PRODUCTION_ENV : PayuConstants.MOBILE_STAGING_ENV);
            Intent intent = new Intent(this, PaymentsActivity.class);
            intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
            startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
        } else {
            Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
        }
    }
}
