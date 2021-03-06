package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.MyCartActivity;
import com.smartsense.gifkar.ProductDetailActivity;
import com.smartsense.gifkar.ProductListActivity;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends CursorAdapter {
    Cursor productCursor;
    DataBaseHelper dbHelper;
    CommonUtil commonUtil = new CommonUtil();
    static JSONArray productArray;
    static JSONObject productObj;
    Context context;
    static Boolean checkCart;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    public ProductAdapter(Context context, Cursor productCursor, DataBaseHelper dbHelper, Boolean checkCart) {
        super(context, productCursor, 0);
        this.productCursor = productCursor;
        this.dbHelper = dbHelper;
        this.context = context;
        this.checkCart = checkCart;
        getCartItem();
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        TextView tvProdElementCate = (TextView) view.findViewById(R.id.tvProdElementCate);
        TextView tvProdElementDT = (TextView) view.findViewById(R.id.tvProdElementDeliveryTime);
        TextView tvProdElementPrice = (TextView) view.findViewById(R.id.tvProdElementRs);
        TextView tvProdElementName = (TextView) view.findViewById(R.id.tvProdElementName);
        NetworkImageView ivProdPhoto = (NetworkImageView) view.findViewById(R.id.ivProdElementImage);
        TextView tvProdElementQty = (TextView) view.findViewById(R.id.tvProdElementQty);
        final ImageButton ibProdElementPlus = (ImageButton) view.findViewById(R.id.ibProdElementPlus);
        final ImageButton ibProdElementMinus = (ImageButton) view.findViewById(R.id.ibProdElementMinus);
        ImageView ivProdElementType = (ImageView) view.findViewById(R.id.ivProdElementType);
//        ImageButton ibProdElementNext = (ImageButton) view.findViewById(R.id.ibProdElementNext);

//        if (cursor.getInt(cursor.getColumnIndexOrThrow("day_id")) == day_id) {
////			Log.i("if",day_id+"yes" +cursor.getInt(cursor.getColumnIndexOrThrow("day_id")));
//            view.setBackgroundColor(Color.parseColor("#f4f4f4"));
//        } else {
//            view.setBackground(context.getResources().getDrawable(R.drawable.shape_box));
//        }
        if (cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ITEM_TYPE)).trim().equalsIgnoreCase("veg")) {
            ivProdElementType.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.veg));
            ivProdElementType.setVisibility(View.VISIBLE);
        }else if (cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ITEM_TYPE)).trim().equalsIgnoreCase("non-veg")) {
            ivProdElementType.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.non_veg));
            ivProdElementType.setVisibility(View.VISIBLE);
        }else{
            ivProdElementType.setVisibility(View.GONE);
        }
//        + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME))
        tvProdElementCate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_QUANTITY)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)));
        tvProdElementDT.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_EARLIY_DEL)));
        tvProdElementPrice.setText("₹ " + cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
        tvProdElementName.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
        tvProdElementQty.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID)));
        ibProdElementPlus.setTag(tvProdElementQty);
        ibProdElementMinus.setTag(tvProdElementQty);
//        ibProdElementNext.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID)));
        view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID)));
        ivProdPhoto.setDefaultImageResId(R.drawable.default_img);
        ivProdPhoto.setImageUrl(Constants.BASE_URL_PHOTO + cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IMAGE)), imageLoader);


        try {
            if (productArray.length() > 0) {
                for (int i = 0; i < productArray.length(); i++) {
                    if (productArray.getJSONObject(i).getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID) == cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID))) {
                        tvProdElementQty.setText("" + productArray.getJSONObject(i).getInt("quantity"));
                        ibProdElementMinus.setBackgroundResource(R.drawable.ic_product_minius_fill);
                        ibProdElementPlus.setBackgroundResource(R.drawable.ic_product_plus_fill);
                        break;
                    } else {
                        ibProdElementMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                        ibProdElementPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                        tvProdElementQty.setText("" + 0);
                    }
                }
            } else {
                ibProdElementMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                ibProdElementPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                tvProdElementQty.setText("" + 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ibProdElementMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvProdElementQty = (TextView) v.getTag();
                if (Integer.valueOf(tvProdElementQty.getText().toString()) >= 1) {
                    tvProdElementQty.setText("" + (Integer.valueOf(tvProdElementQty.getText().toString()) - 1));
                    addProduct(commonUtil, dbHelper, context, false, (Integer) tvProdElementQty.getTag(), Integer.valueOf(tvProdElementQty.getText().toString()));
                    if (Integer.valueOf(tvProdElementQty.getText().toString()) == 0) {
                        ibProdElementMinus.setBackgroundResource(R.drawable.ic_product_minus_unfill);
                        ibProdElementPlus.setBackgroundResource(R.drawable.ic_product_plus_unfill);
                    }
                }
            }
        });

        ibProdElementPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvProdElementQty = (TextView) v.getTag();
                if (Integer.valueOf(tvProdElementQty.getText().toString()) < 3) {
                    tvProdElementQty.setText("" + (Integer.valueOf(tvProdElementQty.getText().toString()) + 1));
                    addProduct(commonUtil, dbHelper, context, false, (Integer) tvProdElementQty.getTag(), Integer.valueOf(tvProdElementQty.getText().toString()));
                    if (Integer.valueOf(tvProdElementQty.getText().toString()) > 0) {
                        ibProdElementMinus.setBackgroundResource(R.drawable.ic_product_minius_fill);
                        ibProdElementPlus.setBackgroundResource(R.drawable.ic_product_plus_fill);
                    }
                }else{
                    Toast.makeText(context, "Sorry, you can't add more of these items", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("ProdDEID", (Integer) v.getTag()));

            }
        });


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        return LayoutInflater.from(context).inflate(R.layout.element_product, parent, false);
    }


    public static void getCartItem() {
        try {
            if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, "") == "")
                productArray = new JSONArray();
            else
                productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addProduct(CommonUtil commonUtil, DataBaseHelper dbHelper, Context context, Boolean insert, int prodDetailId, int qty) {
        try {
            Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE " + DataBaseHelper.COLUMN_PROD_DETAIL_ID + " = '"
                    + prodDetailId + "'");
            if (cursor.getCount() > 0) {
                for (int i = 0; i < productArray.length(); i++) {
                    if (prodDetailId == productArray.getJSONObject(i).getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            productArray.remove(i);
                        } else {
                            productArray = remove(i, productArray);
                        }
                        break;
                    }
                }
                if (qty != 0) {
                    productObj = new JSONObject();
                    productObj.put(DataBaseHelper.COLUMN_PROD_DETAIL_ID, cursor.getInt(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DETAIL_ID)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_ID, cursor.getInt(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ID)));
                    productObj.put("quantity", qty);
                    productObj.put(DataBaseHelper.COLUMN_PROD_NAME, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_IMAGE, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IMAGE)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_PRICE, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_QUANTITY, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_QUANTITY)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_ITEM_TYPE, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ITEM_TYPE)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_DESC, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_DESC)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_UNIT_NAME, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME)));
                    productObj.put(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME, cursor.getString(cursor
                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME)));
//                    productObj.put(DataBaseHelper.COLUMN_PROD_EARLIY_DEL, cursor.getString(cursor
//                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_EARLIY_DEL)));
//                    productObj.put(DataBaseHelper.COLUMN_PROD_IS_AVAIL, cursor.getString(cursor
//                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IS_AVAIL)));

//                    productObj.put(DataBaseHelper.COLUMN_PROD_UNIT_ID, cursor.getString(cursor
//                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_ID)));

//                    productObj.put(DataBaseHelper.COLUMN_PROD_PACKAGE_ID, cursor.getString(cursor
//                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_ID)));
//                    productObj.put(DataBaseHelper.COLUMN_PROD_CATEGORY_ID, cursor.getString(cursor
//                            .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_CATEGORY_ID)));
                    productArray.put(productObj);
                }
                Log.i("productArray", productArray.toString());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PROD_LIST, productArray.toString());
                SharedPreferenceUtil.save();
                if (checkCart) {
                    ProductListActivity.checkCart(context, productArray);
                } else {
                    MyCartActivity.setAdapter(context, productArray, qty);

                }
                if (insert)
                    ProductDetailActivity.checkCart(productArray);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
//        Log.i("objs", objs.toString());
        objs.remove(idx);
//        Log.i("objs", objs.toString());
        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }


}