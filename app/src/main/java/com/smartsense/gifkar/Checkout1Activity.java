package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.CheckoutAdapter;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

public class Checkout1Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private ListView lvCheckout1;
    private TextView tvCheckoutTotal;
    private TextView tvCheckoutPayable;
    private TextView tvCheckoutShipping;
    private TextView tvCheckoutShopName;
    private Button btCheckout;

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
        setContentView(R.layout.activity_checkout1);
        lvCheckout1 = (ListView) findViewById(R.id.lvCheckout1);
        tvCheckoutTotal = (TextView) findViewById(R.id.tvChackout1Total);
        tvCheckoutPayable = (TextView) findViewById(R.id.tvChackout1Payable);
        tvCheckoutShipping = (TextView) findViewById(R.id.tvChackout1Shipping);
        tvCheckoutShopName = (TextView) findViewById(R.id.tvChackout1ShopName);
        tvCheckoutShopName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_NAME, ""));
        btCheckout = (Button) findViewById(R.id.btnContinueCheckout);
        btCheckout.setOnClickListener(this);
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
            double shipping = Double.valueOf(SharedPreferenceUtil.getString(Constants.PrefKeys.DELIVERY_CHARGES,"0"));
            for (int i = 0; i < productCartArray.length(); i++) {
                totalAmount += (productCartArray.optJSONObject(i).optDouble(DataBaseHelper.COLUMN_PROD_PRICE) * productCartArray.optJSONObject(i).optDouble("quantity"));
            }
            tvCheckoutTotal.setText("\u20B9" + totalAmount);
            tvCheckoutShipping.setText("\u20B9" + shipping);
            tvCheckoutPayable.setText("\u20B9" + (shipping + totalAmount));
            checkoutAdapter = new CheckoutAdapter(this, productCartArray, true);
            lvCheckout1.setAdapter(checkoutAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinueCheckout:
                startActivity(new Intent(this, Checkout2Activity.class).putExtra("ship", tvCheckoutShipping.getText().toString()).putExtra("total", tvCheckoutTotal.getText().toString()).putExtra("pay", tvCheckoutPayable.getText().toString()));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
