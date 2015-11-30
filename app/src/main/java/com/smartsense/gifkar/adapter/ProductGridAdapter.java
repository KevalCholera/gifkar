package com.smartsense.gifkar.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ViewHolder> {
    JSONArray dataArray;
    private LayoutInflater inflater;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();
    static JSONArray productArray;

    JSONObject productObj;
    Boolean check;
    Activity context;

    public ProductGridAdapter(Activity context, JSONArray dataArray, Boolean check) {
        this.check = check;
        this.context = context;
        this.dataArray = dataArray;
//        getLastItem();
        inflater = LayoutInflater.from(context);
    }


    public static void getLastItem() {
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
    public ProductGridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_element, viewGroup, false);


        ViewHolder viewHolder = new ViewHolder(view);
        // TODO Auto-generated method stub

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        JSONObject testJson = dataArray.optJSONObject(position);
//        holder.tvNameProduct.setText(testJson.optString("productName"));
//        holder.tvQuantityProduct.setText(testJson.optString("quantity") + " " + testJson.optString("unitName"));
//        holder.tvPriceProduct.setText("\u20B9 " + testJson.optString("rate"));
//        holder.ivImgProduct.setDefaultImageResId(R.drawable.ic_gifkar_logo);
//        holder.ivImgProduct.setImageUrl(Constants.BASE_IMAGE_URL + testJson.optString("productImage"), imageLoader);
//        holder.imgbtn_add_product_ele.setTag(testJson.optString("productManageId"));
//        holder.imgbtn_remove_product_ele.setTag(testJson.optString("productManageId"));

//        final int productmanageId = testJson.optInt("productManageId");
//        final String productName = testJson.optString("productName");
//        final String quantity = testJson.optString("quantity");
//        final String unitName = testJson.optString("unitName");
//        final String rate = testJson.optString("rate");
//        final String productImage = testJson.optString("productImage");
//        try {
//            if (productArray.length() > 0) {
//                for (int i = 0; i < productArray.length(); i++) {
//                    if (productArray.getJSONObject(i).getInt("productManageId") == productmanageId) {
//                        holder.tv_total_product_ele.setText("" + productArray.getJSONObject(i).getInt("productpurchaseqty"));
//                        break;
//                    } else {
//                        holder.tv_total_product_ele.setText("" + 0);
//                    }
//                }
//            } else {
//                holder.tv_total_product_ele.setText("" + 0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//            final int qty = Integer.valueOf(holder.tv_total_product_ele.getText().toString());

    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {

//        return dataArray.length();
        return 3;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameProduct;
        TextView tvQuantityProduct;
        TextView tv_total_product_ele;
        TextView tvPriceProduct;
        NetworkImageView ivImgProduct;
        ImageButton imgbtn_add_product_ele;
        ImageButton imgbtn_remove_product_ele;

        public ViewHolder(View view) {
            super(view);


            tvNameProduct = (TextView) view.findViewById(R.id.tv_name_product_ele);

            ivImgProduct = (NetworkImageView) view.findViewById(R.id.img_product_ele);
        }
    }

    public void addProduct(Boolean insert, int qty, int productmanageId, String productName, String productImage, String rate, String quantity, String unitName) {
        try {
            for (int i = 0; i < productArray.length(); i++) {
                if (productmanageId == productArray.getJSONObject(i).getInt("productManageId")) {
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
                productObj.put("productManageId", productmanageId);
                productObj.put("productpurchaseqty", qty);
                productObj.put("productName", productName);
                productObj.put("productImage", productImage);
                productObj.put("rate", rate);
                productObj.put("quantity", quantity);
                productObj.put("unitName", unitName);

                productArray.put(productObj);
            }
            Log.i("productArray", productArray.toString());
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PROD_LIST, productArray.toString());
            SharedPreferenceUtil.save();
//            if (!check) {
//                CartActivity.setAdapter(context, productArray, qty);
//
//            } else {
//                CategoryActivity.checkCart(context);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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