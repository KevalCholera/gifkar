package com.smartsense.gifkar.adapter;

/**
 * Created by Ronak on 02-12-2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckoutAdapter extends BaseAdapter {
    JSONArray dataArray;
    private LayoutInflater inflater;
    static JSONArray productArray;
    JSONObject productObj;
    Context activity;
    Boolean check;

    public CheckoutAdapter(Context activity, JSONArray dataArray, Boolean check) {
        this.activity = activity;
        this.dataArray = dataArray;
        this.check=check;
        inflater = LayoutInflater.from(activity);
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
            view = inflater.inflate(R.layout.element_checkout1, parent, false);
            // TODO Auto-generated method stub
            holder.tvcheckoutElementCate = (TextView) view.findViewById(R.id.tvCheckout1ElementCate);
            holder.tvcheckoutElementDT = (TextView) view.findViewById(R.id.tvCheckout1ElementDeliveryTime);
            holder.tvcheckoutElementPrice = (TextView) view.findViewById(R.id.tvCheckout1ElementRs);
            holder.tvcheckoutElementName = (TextView) view.findViewById(R.id.tvCheckout1ElementName);
            holder.ivProdPhoto = (NetworkImageView) view.findViewById(R.id.ivCheckout1ElementImage);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject addressObj = dataArray.optJSONObject(position);
        holder.tvcheckoutElementCate.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_QUANTITY) + " " + addressObj.optString(DataBaseHelper.COLUMN_PROD_UNIT_NAME) + " " + addressObj.optString(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME));
        holder.tvcheckoutElementDT.setText("\u20B9 "+addressObj.optString(DataBaseHelper.COLUMN_PROD_PRICE)+" x "+addressObj.optString("quantity"));
        holder.tvcheckoutElementPrice.setText("\u20B9 "+(addressObj.optDouble(DataBaseHelper.COLUMN_PROD_PRICE)*addressObj.optDouble("quantity")));
        holder.tvcheckoutElementName.setText(addressObj.optString(DataBaseHelper.COLUMN_PROD_NAME));

        return view;
    }

    class ViewHolder {
        TextView tvcheckoutElementCate;
        TextView tvcheckoutElementDT;
        TextView tvcheckoutElementPrice;
        TextView tvcheckoutElementName;
        NetworkImageView ivProdPhoto;

    }
}
