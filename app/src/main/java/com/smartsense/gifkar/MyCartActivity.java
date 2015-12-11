package com.smartsense.gifkar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.MyCartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCartActivity extends AppCompatActivity implements View.OnClickListener{
    ListView lvMyCart;
    LinearLayout llMyCart,llMyCartEmpty;
    private ImageView btBack, btCart;
    private TextView tvCartTotalRs;
    private TextView tvCartShopName;
    private LinearLayout llChackout;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_my_cart));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btCart = (ImageView) v.findViewById(R.id.btActionBarInfo);
        btCart.setBackgroundResource(R.drawable.ic_cart_del);
        btCart.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);

        setContentView(R.layout.activity_my_cart);

        llMyCart=(LinearLayout) findViewById(R.id.ll_cart);
        llMyCartEmpty=(LinearLayout) findViewById(R.id.ll_cart_empty);
        llChackout=(LinearLayout) findViewById(R.id.llChackout);
        llChackout.setOnClickListener(this);
        lvMyCart=(ListView) findViewById(R.id.lvCart);
        tvCartShopName=(TextView) findViewById(R.id.tvCartShopName);
        tvCartTotalRs=(TextView) findViewById(R.id.tvCartTotalRs);
    }


    public void myCartFill(JSONObject address) {
        MyCartAdapter myCartAdapter = null;
        try {
            myCartAdapter = new MyCartAdapter(this, address.getJSONObject("data").getJSONArray("deliveryAddresses"), true);
            lvMyCart.setAdapter(myCartAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAdapter(Activity a, JSONArray productArray, int qty) {
        totalAmount = 0;
        for (int i = 0; i < productArray.length(); i++) {
            totalAmount += (productArray.optJSONObject(i).optDouble("rate") * productArray.optJSONObject(i).optDouble("productpurchaseqty"));
        }

        if (totalAmount == 0) {
            llMyCartEmpty.setVisibility(View.VISIBLE);
            llMyCart.setVisibility(View.GONE);
        } else {
            llMyCartEmpty.setVisibility(View.GONE);
            llMyCart.setVisibility(View.VISIBLE);
            tvCartTotalRs.setText("₹" + totalAmount);
        }

        //if product length 0 then refresh list
        if (qty == 0) {
            lvMyCart.setAdapter(new MyCartAdapter(a, productArray, false));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llChackout:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setCancelable(true);
                alertbox.setMessage("Are you sure you want to delete ?");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        JSONObject objAddress = (JSONObject) view.getTag();
//                        String deleteId=objAddress.optString("");
                    }

                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alertbox.show();
                break;
            default:
        }
    }
}
