package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ShopFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack, btInfo;
    private RadioButton cbRatting;
    private RadioButton cbName;
    private RadioButton cbMinOrder;
    private Button btApplyFilter;
    private Button btnShopCancelFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.filter));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);

        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_shop_filter);
        cbName = (RadioButton) findViewById(R.id.cbShopFilterName);
        cbMinOrder = (RadioButton) findViewById(R.id.cbShopFilterMinOrder);
        cbRatting = (RadioButton) findViewById(R.id.cbShopFilterRatting);
        btApplyFilter = (Button) findViewById(R.id.btnShopApplyFilter);
        btApplyFilter.setOnClickListener(this);
        btnShopCancelFilter = (Button) findViewById(R.id.btnShopCancelFilter);
        btnShopCancelFilter.setOnClickListener(this);
        cbName.setChecked(SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_NAME, false));
        cbMinOrder.setChecked(SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_MIN, false));
        cbRatting.setChecked(SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_RATTING, false));
        cbName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible1();
            }
        });
        cbRatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible3();
            }
        });
        cbMinOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible2();
            }
        });
        setVisible();
    }


    public void setVisible3() {
        if (cbRatting.isChecked() | cbName.isChecked() | cbMinOrder.isChecked()) {
            cbName.setChecked(false);
            cbMinOrder.setChecked(false);
            btnShopCancelFilter.setVisibility(View.VISIBLE);
        } else {
            btnShopCancelFilter.setVisibility(View.GONE);
        }
    }


    public void setVisible1() {
        if (cbRatting.isChecked() | cbName.isChecked() | cbMinOrder.isChecked()) {
            cbRatting.setChecked(false);
            cbMinOrder.setChecked(false);
            btnShopCancelFilter.setVisibility(View.VISIBLE);
        } else {
            btnShopCancelFilter.setVisibility(View.GONE);
        }
    }


    public void setVisible2() {
        if (cbRatting.isChecked() | cbName.isChecked() | cbMinOrder.isChecked()) {
            cbRatting.setChecked(false);
            cbName.setChecked(false);
            btnShopCancelFilter.setVisibility(View.VISIBLE);
        } else {
            btnShopCancelFilter.setVisibility(View.GONE);
        }
    }

    public void setVisible() {
        if (cbRatting.isChecked() | cbName.isChecked()| cbMinOrder.isChecked())
            btnShopCancelFilter.setVisibility(View.VISIBLE);
        else
            btnShopCancelFilter.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btnShopApplyFilter:
                List<String> myList = new ArrayList<String>();
                if (cbName.isChecked())
                    myList.add(DataBaseHelper.COLUMN_SHOP_NAME);
                if (cbMinOrder.isChecked())
                    myList.add(DataBaseHelper.COLUMN_MIN_ORDER);
                if (cbRatting.isChecked())
                    myList.add(DataBaseHelper.COLUMN_RATING);
                String s1 = TextUtils.join(",", myList);
                Log.i("save", s1);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_FILTER, s1);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_NAME, cbName.isChecked());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_MIN, cbMinOrder.isChecked());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_RATTING, cbRatting.isChecked());
                SharedPreferenceUtil.save();
                intent.putExtra("orderBy", s1);
                setResult(2, intent);
                finish();
                break;
            case R.id.btActionBarBack:
                setResult(2, intent);
                finish();
                break;
            case R.id.btnShopCancelFilter:
                cbName.setChecked(false);
                cbMinOrder.setChecked(false);
                cbRatting.setChecked(false);
                setVisible();
                SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_FILTER, "");
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_NAME, false);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_MIN, false);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FILTER_SHOP_RATTING, false);
                SharedPreferenceUtil.save();
                setResult(2, intent);
                CommonUtil.alertBox(this,"","Filter Cleared");
                finish();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            default:
        }
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
