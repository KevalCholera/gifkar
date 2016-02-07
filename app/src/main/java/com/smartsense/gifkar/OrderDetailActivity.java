package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CheckoutAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    private ImageView btBack;
    private NetworkImageView ivShopListImage;
    private TextView tvOrderDetailOrderNo,tvOrderDetailPayable,tvOrderDetailShipping,tvOrderDetailTotal;
    private TextView tvOrderDetailDateTime;
    private TextView tvOrderDetailAddress;
    private TextView tvOrderElementShopName;
    private TextView tvOrderElementOrderStatus;
    private TextView tvOrderElementDetails;
    private TextView tvOrderDetailName;
    private TextView tvOrderDetailNo;
    private TextView btProdDetailEmail;
    private TextView btProdDetailCall;
    private ListView lvOrderDetail;
    private TextView tvOrderProcess;
    private TextView tvOrderAccepted;
    private TextView tvOrderCompleted;
    private Button btCancel;
    private Button btEmail;
    private EditText etEmail;
    private AlertDialog alert;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_order_detail));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_order_detail);
        ivShopListImage = (NetworkImageView) findViewById(R.id.ivShopListImage);
        tvOrderDetailOrderNo = (TextView) findViewById(R.id.tvOrderDetailOrderNo);
        tvOrderDetailTotal = (TextView) findViewById(R.id.tvOrderDetailTotal);
        tvOrderDetailShipping = (TextView) findViewById(R.id.tvOrderDetailShipping);
        tvOrderDetailPayable = (TextView) findViewById(R.id.tvOrderDetailPayable);
        tvOrderDetailDateTime = (TextView) findViewById(R.id.tvOrderDetailDateTime);
        tvOrderDetailAddress = (TextView) findViewById(R.id.tvOrderDetailAddress);
        tvOrderElementShopName = (TextView) findViewById(R.id.tvOrderElementShopName);
        tvOrderElementOrderStatus = (TextView) findViewById(R.id.tvOrderElementOrderStatus);
        tvOrderElementDetails = (TextView) findViewById(R.id.tvOrderElementDetails);
        tvOrderDetailName = (TextView) findViewById(R.id.tvOrderDetailName);
        tvOrderDetailNo = (TextView) findViewById(R.id.tvOrderDetailNo);
        tvOrderCompleted = (TextView) findViewById(R.id.tvOrderCompleted);
        tvOrderAccepted = (TextView) findViewById(R.id.tvOrderAccepted);
        tvOrderProcess = (TextView) findViewById(R.id.tvOrderProcess);
        btProdDetailEmail = (TextView) findViewById(R.id.btProdDetailEmail);
        btProdDetailEmail.setOnClickListener(this);
        btProdDetailCall = (TextView) findViewById(R.id.btProdDetailCall);
        btProdDetailCall.setOnClickListener(this);
        ivShopListImage = (NetworkImageView) findViewById(R.id.ivShopListImage);
        lvOrderDetail = (ListView) findViewById(R.id.lvOrderDetail);
//        getOrderDetail("1");
        getOrderDetail(getIntent().getStringExtra("id"));
    }

    public void orderDetailFill(JSONObject response) {
        try {
            tvOrderDetailOrderNo.setText("Order ID : " + response.optString("orderNo"));
            tvOrderDetailDateTime.setText(response.optString("createdAt"));
            tvOrderDetailShipping.setText("\u20B9 "+response.optString("shippingCharges"));
            tvOrderDetailTotal.setText("\u20B9 "+response.optString("totalAmount"));
            tvOrderDetailPayable.setText("\u20B9 "+response.optString("grossAmount"));
            tvOrderDetailAddress.setText("Name : " + response.optJSONObject("deliveryAddress").optString("recipientName") + "\nMobile No. : " + response.optJSONObject("deliveryAddress").optString("recipientContact") + "\nAddress : " + response.optJSONObject("deliveryAddress").optString("address") + ", " + response.optJSONObject("deliveryAddress").optString("landmark") + ", " + response.optJSONObject("deliveryAddress").optString("area") + ", " + response.optJSONObject("deliveryAddress").optString("city") + " - " + response.optJSONObject("deliveryAddress").optString("pincode"));
            tvOrderElementShopName.setText(response.optString("shopName"));
            ivShopListImage.setDefaultImageResId(R.drawable.default_img);
            ivShopListImage.setImageUrl(Constants.BASE_URL + "/images/shops/" + response.optString("shopImage"), imageLoader);
            tvOrderElementOrderStatus.setText("Your Order is " + response.optString("orderStatus"));
            tvOrderElementDetails.setText(response.optJSONArray("products").length() + " Items");
            tvOrderDetailName.setText("Name : " + response.optJSONObject("sender").optString("firstName") + " " + response.optJSONObject("sender").optString("lastName")+"\nMobile No. : " + response.optJSONObject("sender").optString("mobile"));
//            tvOrderDetailNo.setText();
            if (response.optString("orderStatus").trim().equalsIgnoreCase("accepted")) {
                tvOrderAccepted.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.accepted_fill, 0, 0);
//                tvOrderAccepted.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted_fill));
//                tvOrderProcess.setBackgroundDrawable(getResources().getDrawable(R.drawable.in_progress_unfill));
//                tvOrderCompleted.setBackgroundDrawable(getResources().getDrawable(R.drawable.deliverd_unfill));
            } else if (response.optString("orderStatus").trim().equalsIgnoreCase("prepared") | response.optString("orderStatus").trim().equalsIgnoreCase("In process")) {
                tvOrderProcess.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.in_progress_fill, 0, 0);
//                tvOrderAccepted.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted_unfill));
//                tvOrderProcess.setBackgroundDrawable(getResources().getDrawable(R.drawable.in_progress_fill));
//                tvOrderCompleted.setBackgroundDrawable(getResources().getDrawable(R.drawable.deliverd_unfill));
            } else if (response.optString("orderStatus").trim().equalsIgnoreCase("delivered")) {
                tvOrderCompleted.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deliverd_fill, 0, 0);
//                tvOrderProcess.setBackgroundDrawable(getResources().getDrawable(R.drawable.in_progress_unfill));
//                tvOrderAccepted.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted_unfill));
//                tvOrderCompleted.setBackgroundDrawable(getResources().getDrawable(R.drawable.deliverd_fill));
            }
            CheckoutAdapter checkoutAdapter = new CheckoutAdapter(this, response.optJSONArray("products"), false);
            lvOrderDetail.setAdapter(checkoutAdapter);
            GifkarActivity.setListViewHeightBasedOnChildren(lvOrderDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                if (getIntent().getBooleanExtra("flag", false))
                    startActivity(new Intent(getBaseContext(), GifkarActivity.class));
                else
                    finish();
                break;
            case R.id.btEmailDialogSend:
                if (etEmail.length() <= 0) {
                    CommonUtil.alertBox(OrderDetailActivity.this, "", "Please enter email address");
                } else {
                    sendMail();
                }

//                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//                alertbox.setCancelable(true);
//                alertbox.setMessage("Mail Sent Successfully.");
//                alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
////                        JSONObject objAddress = (JSONObject) view.getTag();
////                        String deleteId=objAddress.optString("");
//                    }
//
//                });
//                alertbox.show();
                break;
            case R.id.btEmailDialogCancel:
                alert.dismiss();
                break;
            case R.id.btProdDetailCall:

                break;
            case R.id.btProdDetailEmail:
                openInfoPopup();
                break;
            default:
        }
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_email_details, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            etEmail = (EditText) dialog.findViewById(R.id.etEmailDialogEmail);
            etEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL,""));
            btEmail = (Button) dialog.findViewById(R.id.btEmailDialogSend);
            btEmail.setOnClickListener(this);
            btCancel = (Button) dialog.findViewById(R.id.btEmailDialogCancel);
            btCancel.setOnClickListener(this);
            alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMail() {
        CommonUtil.closeKeyboard(OrderDetailActivity.this);
        final String tag = "sendMail";
        String url = Constants.BASE_URL + "/mobile/orderDetail/sendMail";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", etEmail.getText().toString());
        params.put("orderDetailId", getIntent().getStringExtra("id"));
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_ADDRESS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    public void getOrderDetail(String orderId) {
        final String tag = "getOrder";
        String url = Constants.BASE_URL + "/mobile/orderDetail/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&orderDetailId=" + orderId + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_ORDER_DETAIL);
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
                        case Constants.Events.EVENT_ORDER_DETAIL:
                            orderDetailFill(response.getJSONObject("data").optJSONObject("orderDetails"));
                            break;
                        case Constants.Events.EVENT_DEL_ADDRESS:
                            alert.dismiss();
                            CommonUtil.alertBox(OrderDetailActivity.this, "", response.optString("message"));
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
