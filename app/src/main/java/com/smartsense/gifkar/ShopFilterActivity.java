package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btBack;
    private CheckBox cbRatting;
    private CheckBox cbName;
    private CheckBox cbMinOrder;
    private Button btApplyFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.filter));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_shop_filter);
        cbName=(CheckBox) findViewById(R.id.cbShopFilterName);
        cbMinOrder=(CheckBox) findViewById(R.id.cbShopFilterMinOrder);
        cbRatting=(CheckBox) findViewById(R.id.cbShopFilterRatting);
        btApplyFilter=(Button) findViewById(R.id.btnShopApplyFilter);
        btApplyFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShopApplyFilter:

                break;
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }
}
