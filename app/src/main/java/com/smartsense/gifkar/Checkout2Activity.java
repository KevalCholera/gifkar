package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Checkout2Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    LinearLayout llChackout2DelAddress;
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
        llChackout2DelAddress= (LinearLayout) v.findViewById(R.id.llChackout2DelAddress);
//        llChackout2DelAddress.setOnClickListener(this);
        tvChackout2DelAddress= (TextView) findViewById(R.id.tvChackout2DelAddress);
        tvCheckoutTotal = (TextView) findViewById(R.id.tvChackout2Total);
        tvCheckoutPayable = (TextView) findViewById(R.id.tvChackout2Payable);
        tvCheckoutShipping = (TextView) findViewById(R.id.tvChackout2Shipping);
        tvCheckoutShopName = (TextView) findViewById(R.id.tvChackout2ShopName);
        btCheckout=(Button) findViewById(R.id.btnCheckout2PlaceOrder);
        btCheckout.setOnClickListener(this);
        btToday=(Button) findViewById(R.id.btnCheckout2Today);
        btToday.setOnClickListener(this);
        btTomorrow=(Button) findViewById(R.id.btnCheckout2Tomorrow);
        btTomorrow.setOnClickListener(this);
        btAfterTomorrow=(Button) findViewById(R.id.btnCheckout2Tomorrow);
        btAfterTomorrow.setOnClickListener(this);
        etCheckout2WriteOccasion=(EditText) findViewById(R.id.etCheckout2WriteOccasion);
        etCheckout2SelectOccasion=(EditText) findViewById(R.id.etCheckout2SelectOccasion);
        etCheckout2SelectOccasion.setOnClickListener(this);
    }

    public void openPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_pickup_time_slot, null);
            alertDialogs.setView(dialog);
//            ivReviewUser=(CircleImageView) dialog.findViewById(R.id.ivReviewUser);
//            tvUserName = (TextView) dialog.findViewById(R.id.tvReviewUserName);
//            etReviewAdd = (EditText) dialog.findViewById(R.id.etReviewAdd);
//            btAddReview = (Button) dialog.findViewById(R.id.btnReviewAdd);
//            rbReview=(Rating) dialog.findViewById(R.id.rbReview);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinueCheckout:
                startActivity(new Intent(this, Checkout2Activity.class));
                break;
            case R.id.btnCheckout2Today:
                openPopup();
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llChackout2DelAddress:
                startActivity(new Intent(this, MyAddressActivity.class));
                break;
            default:
        }
    }
}
