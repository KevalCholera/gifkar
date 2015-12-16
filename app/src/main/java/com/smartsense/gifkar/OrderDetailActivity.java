package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CheckoutAdapter;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private NetworkImageView ivShopListImage;
    private TextView tvOrderDetailOrderNo;
    private TextView tvOrderDetailDateTime;
    private TextView tvOrderDetailAddress;
    private TextView tvOrderElementShopName;
    private TextView tvOrderElementOrderStatus;
    private TextView tvOrderElementDetails;
    private TextView tvOrderDetailName;
    private TextView tvOrderDetailNo;
    private Button btProdDetailEmail;
    private Button btProdDetailCall;
    private ListView lvOrderDetail;
    private TextView tvOrderProcess;
    private TextView tvOrderAccepted;
    private TextView tvOrderCompleted;
    private Button btCancel;
    private Button btEmail;
    private EditText etEmail;
    private AlertDialog alert;

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
        tvOrderDetailOrderNo = (TextView) findViewById(R.id.tvOrderDetailOrderNo);
        tvOrderDetailDateTime = (TextView) findViewById(R.id.tvOrderDetailDateTime);
        tvOrderDetailAddress = (TextView) findViewById(R.id.tvOrderDetailAddress);
        tvOrderElementShopName = (TextView) findViewById(R.id.tvOrderElementShopName);
        tvOrderElementOrderStatus = (TextView) findViewById(R.id.tvOrderElementOrderStatus);
        tvOrderElementDetails = (TextView) findViewById(R.id.tvOrderElementDetails);
        tvOrderDetailName = (TextView) findViewById(R.id.tvOrderDetailName);
        tvOrderDetailNo = (TextView) findViewById(R.id.tvOrderDetailNo);
        tvOrderCompleted= (TextView) findViewById(R.id.tvOrderCompleted);
        tvOrderAccepted= (TextView) findViewById(R.id.tvOrderAccepted);
        tvOrderProcess= (TextView) findViewById(R.id.tvOrderProcess);
        btProdDetailEmail = (Button) findViewById(R.id.btProdDetailEmail);
        btProdDetailEmail.setOnClickListener(this);
        btProdDetailCall = (Button) findViewById(R.id.btProdDetailCall);
        ivShopListImage = (NetworkImageView) findViewById(R.id.ivShopListImage);
        lvOrderDetail = (ListView) findViewById(R.id.lvOrderDetail);
        try {
            JSONArray productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
            checkoutFill(productArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkoutFill(JSONArray productCartArray) {
        CheckoutAdapter checkoutAdapter = null;
        try {
            double totalAmount = 0;
            for (int i = 0; i < productCartArray.length(); i++) {
                totalAmount += (productCartArray.optJSONObject(i).optDouble(DataBaseHelper.COLUMN_PROD_PRICE) * productCartArray.optJSONObject(i).optDouble("quantity"));
            }
//            tvCheckoutTotal.setText("" + totalAmount);
            checkoutAdapter = new CheckoutAdapter(this, productCartArray, true);
            lvOrderDetail.setAdapter(checkoutAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btEmailDialogSend:
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setCancelable(true);
                alertbox.setMessage("Mail Sent Successfully.");
                alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        JSONObject objAddress = (JSONObject) view.getTag();
//                        String deleteId=objAddress.optString("");
                    }

                });
                alertbox.show();
                break;
            case R.id.btEmailDialogCancel:
                alert.dismiss();
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
            etEmail=(EditText) dialog.findViewById(R.id.etEmailDialogEmail);
            btEmail=(Button) dialog.findViewById(R.id.btEmailDialogSend);
            btEmail.setOnClickListener(this);
            btCancel=(Button) dialog.findViewById(R.id.btEmailDialogCancel);
            btCancel.setOnClickListener(this);
            alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
