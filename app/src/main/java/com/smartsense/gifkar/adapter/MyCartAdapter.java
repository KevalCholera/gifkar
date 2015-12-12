package com.smartsense.gifkar.adapter;

/**
 * Created by Ronak on 02-12-2015.
 */

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.MyCartActivity;
import com.smartsense.gifkar.ProductListActivity;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCartAdapter extends BaseAdapter {
    JSONArray dataArray;
    private LayoutInflater inflater;
    static JSONArray productArray;
    JSONObject productObj;
    Context activity;
    Boolean check;

    public MyCartAdapter(Context activity, JSONArray dataArray, Boolean check) {
        this.activity = activity;
        this.dataArray = dataArray;
        this.check=check;
        getCartItem();
        inflater = LayoutInflater.from(activity);
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

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.element_product, parent, false);
            // TODO Auto-generated method stub
            holder.tvProdElementCate = (TextView) view.findViewById(R.id.tvProdElementCate);
            holder.tvProdElementDT = (TextView) view.findViewById(R.id.tvProdElementDeliveryTime);
            holder.tvProdElementPrice = (TextView) view.findViewById(R.id.tvProdElementRs);
            holder.tvProdElementName = (TextView) view.findViewById(R.id.tvProdElementName);
            holder.tvProdElementQty = (TextView) view.findViewById(R.id.tvProdElementQty);

            holder.ivProdPhoto = (NetworkImageView) view.findViewById(R.id.ivProdElementImage);
            holder.ibProdElementPlus = (ImageButton) view.findViewById(R.id.ibProdElementPlus);
            holder.ibProdElementMinus = (ImageButton) view.findViewById(R.id.ibProdElementMinus);
            holder.ibProdElementNext = (ImageButton) view.findViewById(R.id.ibProdElementNext);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject addressObj = dataArray.optJSONObject(position);
        holder.tvProdElementCate.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_QUANTITY) + " " + addressObj.optString(DataBaseHelper.COLUMN_PROD_UNIT_NAME) + " " + addressObj.optString(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME));
        holder.tvProdElementDT.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_EARLIY_DEL));
        holder.tvProdElementPrice.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_PRICE));
        holder.tvProdElementName.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_NAME));
        holder.tvProdElementQty.setText(addressObj.optString("quantity"));
        holder.ibProdElementNext.setVisibility(View.INVISIBLE);
        holder.tvProdElementQty.setTag(addressObj);
        holder.ibProdElementMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(holder.tvProdElementQty.getText().toString()) >= 1) {
                    holder.tvProdElementQty.setText("" + (Integer.valueOf(holder.tvProdElementQty.getText().toString()) - 1));
                    addProduct(false, (JSONObject) holder.tvProdElementQty.getTag(),Integer.valueOf(holder.tvProdElementQty.getText().toString()));
                }
            }
        });
        holder.ibProdElementPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(holder.tvProdElementQty.getText().toString()) < 3) {
                    holder.tvProdElementQty.setText("" + (Integer.valueOf(holder.tvProdElementQty.getText().toString()) + 1));
                    addProduct(true, (JSONObject) holder.tvProdElementQty.getTag(),Integer.valueOf(holder.tvProdElementQty.getText().toString()));
                }
            }
        });

        return view;
    }

    class ViewHolder {
        TextView tvProdElementCate;
        TextView tvProdElementDT;
        TextView tvProdElementPrice;
        TextView tvProdElementName;
        NetworkImageView ivProdPhoto;
        TextView tvProdElementQty;
        ImageButton ibProdElementPlus;
        ImageButton ibProdElementMinus;
        ImageButton ibProdElementNext;
    }

    public void addProduct(Boolean insert, JSONObject jsonData,int qty) {
        try {
                for (int i = 0; i < productArray.length(); i++) {
                    if (jsonData.getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID) == productArray.getJSONObject(i).getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            productArray.remove(i);
                        } else {
                            productArray = ProductAdapter.remove(i, productArray);
                        }
                        break;
                    }
                }
                if (qty != 0) {
                    productObj = new JSONObject();
                    productObj.put(DataBaseHelper.COLUMN_PROD_DETAIL_ID, jsonData.getInt(DataBaseHelper.COLUMN_PROD_DETAIL_ID));
                    productObj.put(DataBaseHelper.COLUMN_PROD_ID, jsonData.getInt(DataBaseHelper.COLUMN_PROD_ID));
                    productObj.put("quantity", qty);
                    productObj.put(DataBaseHelper.COLUMN_PROD_NAME, jsonData.getString(DataBaseHelper.COLUMN_PROD_NAME));
                    productObj.put(DataBaseHelper.COLUMN_PROD_IMAGE, jsonData.getString(DataBaseHelper.COLUMN_PROD_IMAGE));
                    productObj.put(DataBaseHelper.COLUMN_PROD_PRICE, jsonData.getString(DataBaseHelper.COLUMN_PROD_PRICE));
                    productObj.put(DataBaseHelper.COLUMN_PROD_QUANTITY, jsonData.getString(DataBaseHelper.COLUMN_PROD_QUANTITY));
                    productObj.put(DataBaseHelper.COLUMN_PROD_ITEM_TYPE, jsonData.getString(DataBaseHelper.COLUMN_PROD_ITEM_TYPE));
                    productObj.put(DataBaseHelper.COLUMN_PROD_DESC, jsonData.getString(DataBaseHelper.COLUMN_PROD_DESC));
                    productObj.put(DataBaseHelper.COLUMN_PROD_UNIT_NAME, jsonData.getString(DataBaseHelper.COLUMN_PROD_UNIT_NAME));
                    productObj.put(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME, jsonData.getString(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME));
                    productObj.put(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME, jsonData.getString(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME));
                    productObj.put(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME, jsonData.getString(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME));
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
                if (check) {
                    ProductListActivity.checkCart(activity,productArray);
                } else {
                    MyCartActivity.setAdapter(activity, productArray, qty);

                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
