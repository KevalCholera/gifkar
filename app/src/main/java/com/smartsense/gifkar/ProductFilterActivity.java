package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btBack;
    CheckBox cbName,cbPrice;
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
        setContentView(R.layout.activity_product_filter);
        cbName=(CheckBox) findViewById(R.id.cbProdFilterName);
        cbPrice=(CheckBox) findViewById(R.id.cbProdFilterPrice);
        btApplyFilter=(Button) findViewById(R.id.btnProdApplyFilter);
        btApplyFilter.setOnClickListener(this);
        cbName.setChecked(SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_PROD_NAME, false));
        cbPrice.setChecked(SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_PROD_PRICE, false));
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.btnProdApplyFilter:
                List<String> myList = new ArrayList<String>();
                if (cbName.isChecked())
                    myList.add(DataBaseHelper.COLUMN_PROD_NAME);
                if (cbPrice.isChecked())
                    myList.add(DataBaseHelper.COLUMN_PROD_PRICE);
                String s1 = TextUtils.join(",", myList);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.PROD_FILTER, s1);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_PROD_NAME, cbName.isChecked());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_PROD_PRICE, cbPrice.isChecked());
                SharedPreferenceUtil.save();
                intent.putExtra("orderBy", s1);
                setResult(1, intent);
                finish();
                break;
            case R.id.btActionBarBack:
                setResult(1, intent);
                finish();
                break;
            default:
        }
    }
}
