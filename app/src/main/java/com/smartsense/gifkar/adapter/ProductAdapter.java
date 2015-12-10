package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.ProductDetailActivity;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends CursorAdapter {
    Cursor productCursor;
    int day_id;


    public ProductAdapter(Context context, Cursor productCursor) {
        super(context, productCursor, 0);
        this.productCursor = productCursor;
        this.day_id = day_id;
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
        ImageButton ibProdElementPlus = (ImageButton) view.findViewById(R.id.ibProdElementPlus);
        ImageButton ibProdElementMinus = (ImageButton) view.findViewById(R.id.ibProdElementMinus);
        ImageButton ibProdElementNext = (ImageButton) view.findViewById(R.id.ibProdElementNext);

//        if (cursor.getInt(cursor.getColumnIndexOrThrow("day_id")) == day_id) {
////			Log.i("if",day_id+"yes" +cursor.getInt(cursor.getColumnIndexOrThrow("day_id")));
//            view.setBackgroundColor(Color.parseColor("#f4f4f4"));
//        } else {
//            view.setBackground(context.getResources().getDrawable(R.drawable.shape_box));
//        }

        tvProdElementCate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_QUANTITY)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME)));
        tvProdElementDT.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_EARLIY_DEL)));
        tvProdElementPrice.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
        tvProdElementName.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
        ibProdElementPlus.setTag(tvProdElementQty);
        ibProdElementMinus.setTag(tvProdElementQty);
        ibProdElementMinus.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ID)));
//         ivProdPhoto.setImageBitmap(CommonUtil.decodeFromBitmap(cursor.getString(cursor
//                 .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_IMAGE))));

        ibProdElementMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvProdElementQty = (TextView) v.getTag();
                if (Integer.valueOf(tvProdElementQty.getText().toString()) >= 1) {
                    tvProdElementQty.setText("" + (Integer.valueOf(tvProdElementQty.getText().toString()) - 1));
                }
            }
        });

        ibProdElementPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvProdElementQty = (TextView) v.getTag();
                if (Integer.valueOf(tvProdElementQty.getText().toString()) < 3) {
                    tvProdElementQty.setText("" + (Integer.valueOf(tvProdElementQty.getText().toString()) + 1));
                }
            }
        });

        ibProdElementNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                (String) v.getTag()
                context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("ProdID", (Integer) v.getTag()));

            }
        });


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        return LayoutInflater.from(context).inflate(R.layout.element_product, parent, false);
    }


//    public void addProduct(Boolean insert, int qty, int productmanageId, String productName, String productImage, String rate, String originalRate, String quantity, String unitName) {
//        try {
//            for (int i = 0; i < productArray.length(); i++) {
//                if (productmanageId == productArray.getJSONObject(i).getInt("productManageId")) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        productArray.remove(i);
//                    } else {
//                        productArray = remove(i, productArray);
//                    }
//                    break;
//                }
//            }
//            if (qty != 0) {
//                productObj = new JSONObject();
//                productObj.put("productManageId", productmanageId);
//                productObj.put("productpurchaseqty", qty);
//                productObj.put("productName", productName);
//                productObj.put("productImage", productImage);
//                productObj.put("rate", rate);
//                productObj.put("original_rate", originalRate);
//                productObj.put("quantity", quantity);
//                productObj.put("unitName", unitName);
//
//                productArray.put(productObj);
//            }
//            Log.i("productArray", productArray.toString());
//            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PROD_LIST, productArray.toString());
//            SharedPreferenceUtil.save();
//            if (!check) {
//                CartActivity.setAdapter(context, productArray, qty);
//
//            } else {
//                CategoryActivity.checkCart(context);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        Log.i("objs", objs.toString());
        objs.remove(idx);
        Log.i("objs", objs.toString());
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