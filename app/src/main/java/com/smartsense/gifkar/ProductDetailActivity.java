package com.smartsense.gifkar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ProductAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static JSONArray productArray;
    private ImageView btBack;
    private ImageButton ibProdMinus;
    private TextView tvProdPrice;
    private ImageButton ibProdPlus;
    private TextView tvProdDes;
    private TextView tvProdUnitName;
//    private TextView tvProdName;
    private NetworkImageView ivProdPhoto;
    private TextView tvProdQty;
    private static TextView tvProdDetailCartCount;
    private static TextView tvProdDetailCartRs;
    private TextView llProdDetailCheckOut;
    RelativeLayout llProdDetailCart;
    DataBaseHelper dbHelper;
    CommonUtil commonUtil = new CommonUtil();
    static LinearLayout llProdDetailBottom;
    private ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

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
        llProdDetailBottom=(LinearLayout) findViewById(R.id.llProdDetailBottom);
        tvProdDes = (TextView) findViewById(R.id.tvProdDetailDes);
        tvProdUnitName = (TextView) findViewById(R.id.tvProdDetailUnitName);
        tvProdPrice = (TextView) findViewById(R.id.tvProdDetailRs);
//        tvProdName = (TextView) findViewById(R.id.tvProdDetailName);
        ivProdPhoto = (NetworkImageView) findViewById(R.id.ivProdDetailImage);
        tvProdQty = (TextView) findViewById(R.id.tvProdDetailQty);
        ibProdPlus = (ImageButton) findViewById(R.id.ibProdDetailPlus);
        ibProdPlus.setOnClickListener(this);
        ibProdMinus = (ImageButton) findViewById(R.id.ibProdDetailMinus);
        ibProdMinus.setOnClickListener(this);
        tvProdDetailCartCount = (TextView) findViewById(R.id.tvProdDetailCartCount);
        tvProdDetailCartRs = (TextView) findViewById(R.id.tvProdDetailCartRs);
        llProdDetailCart = (RelativeLayout) findViewById(R.id.llProdDetailCart);
        llProdDetailCart.setOnClickListener(this);
        llProdDetailCheckOut = (TextView) findViewById(R.id.llProdDetailCheckOut);
        llProdDetailCheckOut.setOnClickListener(this);

        Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE " + DataBaseHelper.COLUMN_PROD_DETAIL_ID + " = '"
                + getIntent().getIntExtra("ProdDEID", 0) + "'");
        if (cursor.getCount() > 0) {
            try {
                getCartItem();
                if (productArray.length() > 0) {
                    for (int i = 0; i < productArray.length(); i++) {
                        if (productArray.getJSONObject(i).getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID) == cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID))) {
                            tvProdQty.setText("" + productArray.getJSONObject(i).getInt("quantity"));
                            ibProdMinus.setBackgroundResource(R.drawable.ic_product_minius_fill);
                            ibProdPlus.setBackgroundResource(R.drawable.ic_product_plus_fill);
                            break;
                        } else {
                            ibProdMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                            ibProdPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                            tvProdQty.setText("" + 0);
                        }
                    }
                } else {
                    ibProdMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                    ibProdPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                    tvProdQty.setText("" + 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvProdQty.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID)));

            tvProdDes.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DESC)));
            tvProdUnitName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_QUANTITY)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME)));
            tvProdPrice.setText("₹ " +cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
//            tvProdName.setText(cursor.getString(cursor
//                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
            titleTextView.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
            ivProdPhoto.setDefaultImageResId(R.drawable.default_img);
            ivProdPhoto.setImageUrl(Constants.BASE_URL + "/images/products/" + cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IMAGE)), imageLoader);
        }

    }

    public static void getCartItem() {
        try {
            if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, "") == "")
                productArray = new JSONArray();
            else
                productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
            checkCart(productArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void checkCart(JSONArray productArray) {
        double totalAmount = 0;
        for (int i = 0; i < productArray.length(); i++) {
            totalAmount += (productArray.optJSONObject(i).optDouble(DataBaseHelper.COLUMN_PROD_PRICE) * productArray.optJSONObject(i).optDouble("quantity"));
        }

        if (totalAmount == 0) {
            llProdDetailBottom.setVisibility(View.GONE);
        } else {
            llProdDetailBottom.setVisibility(View.VISIBLE);
            tvProdDetailCartRs.setText("₹" + totalAmount);
//            DecimalFormat twodigits = new DecimalFormat("00");
//            tvProdDetailCartCount.setText("" + twodigits.format(productArray.length()));
            tvProdDetailCartCount.setText(" " + productArray.length()+" ");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibProdDetailMinus:
                if (Integer.valueOf(tvProdQty.getText().toString()) >= 1) {
                    tvProdQty.setText("" + (Integer.valueOf(tvProdQty.getText().toString()) - 1));
                    ProductAdapter.addProduct(commonUtil, dbHelper, ProductDetailActivity.this, true, (Integer) tvProdQty.getTag(), Integer.valueOf(tvProdQty.getText().toString()));

                    if (Integer.valueOf(tvProdQty.getText().toString()) == 0) {
                        ibProdMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                        ibProdPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                    }
                }
                break;
            case R.id.ibProdDetailPlus:
                if (Integer.valueOf(tvProdQty.getText().toString()) < 3) {
                    tvProdQty.setText("" + (Integer.valueOf(tvProdQty.getText().toString()) + 1));
                    ProductAdapter.addProduct(commonUtil, dbHelper, ProductDetailActivity.this, true, (Integer) tvProdQty.getTag(), Integer.valueOf(tvProdQty.getText().toString()));

                    if (Integer.valueOf(tvProdQty.getText().toString()) > 0) {
                        ibProdMinus.setBackgroundResource(R.drawable.ic_product_minius_fill);
                        ibProdPlus.setBackgroundResource(R.drawable.ic_product_plus_fill);
                    }
                }else{
                    Toast.makeText(this,"Sorry, Maximum item limit is Three",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btActionBarBack:
                ProductFragment.reloadList = true;
                finish();
                break;
            case R.id.llProdDetailCart:
                startActivity(new Intent(this, MyCartActivity.class));
//                finish();
                break;
            case R.id.llProdDetailCheckOut:
                if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_ACCESS_TOKEN))
                    startActivity(new Intent(this, Checkout1Activity.class));
                else
                    startActivity(new Intent(this, GifkarActivity.class));
                break;
            default:
        }
    }
}
