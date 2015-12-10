package com.smartsense.gifkar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.DataBaseHelper;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private ImageButton ibProdMinus;
    private TextView tvProdPrice;
    private ImageButton ibProdPlus;
    private TextView tvProdDes;
    private TextView tvProdUnitName;
    private TextView tvProdName;
    private NetworkImageView ivProdPhoto;
    private TextView tvProdQty;
    private TextView tvProdDetailCartCount;
    private TextView tvProdDetailCartRs;
    private LinearLayout llProdDetailCart, llProdDetailCheckOut;
    DataBaseHelper dbHelper;
    CommonUtil commonUtil = new CommonUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.prod_name));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_product_detail);

        tvProdDes = (TextView) findViewById(R.id.tvProdDetailDes);
        tvProdUnitName = (TextView) findViewById(R.id.tvProdDetailUnitName);
        tvProdPrice = (TextView) findViewById(R.id.tvProdDetailRs);
        tvProdName = (TextView) findViewById(R.id.tvProdDetailName);
        ivProdPhoto = (NetworkImageView) findViewById(R.id.ivProdDetailImage);
        tvProdQty = (TextView) findViewById(R.id.tvProdDetailQty);
        ibProdPlus = (ImageButton) findViewById(R.id.ibProdDetailPlus);
        ibProdPlus.setOnClickListener(this);
        ibProdMinus = (ImageButton) findViewById(R.id.ibProdDetailMinus);
        ibProdMinus.setOnClickListener(this);
        tvProdDetailCartCount= (TextView) findViewById(R.id.tvProdDetailCartCount);
        tvProdDetailCartRs= (TextView) findViewById(R.id.tvProdDetailCartRs);
        llProdDetailCart =(LinearLayout) findViewById(R.id.llProdDetailCart);
        llProdDetailCart.setOnClickListener(this);
        llProdDetailCheckOut =(LinearLayout) findViewById(R.id.llProdDetailCheckOut);
        llProdDetailCheckOut.setOnClickListener(this);

        Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM "+ DataBaseHelper.TABLE_PRODUCT+"  WHERE "+DataBaseHelper.COLUMN_PROD_ID+" = '"
                + getIntent().getStringExtra("ProdID")+"'");
        if (cursor.getCount() > 0) {
            tvProdDes.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DESC)));
            tvProdUnitName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)));
            tvProdPrice.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
            tvProdName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
//            ivProdPhoto.setImageUrl(cursor.getString(cursor
//                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IMAGE)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibProdDetailPlus:

                break;
            case R.id.ibProdDetailMinus:

                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llProdDetailCart:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.llProdDetailCheckOut:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            default:
        }
    }
}
