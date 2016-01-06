package com.smartsense.gifkar.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Ronak on 02-12-2015.
 */

public class MyOrderAdapter extends BaseAdapter {
    JSONArray dataArray;
    private LayoutInflater inflater;
    Activity activity;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    public MyOrderAdapter(Activity activity, JSONArray dataArray, Boolean check) {
        this.activity = activity;
        this.dataArray = dataArray;
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
            view = inflater.inflate(R.layout.element_orders, parent, false);
            // TODO Auto-generated method stub
            holder.tvOrderNo = (TextView) view.findViewById(R.id.tvOrderElementOrderNo);

            holder.tvDate = (TextView) view.findViewById(R.id.tvOrderElementDateTime);

            holder.tvDetails = (TextView) view.findViewById(R.id.tvOrderElementDetails);

            holder.tvShopName = (TextView) view.findViewById(R.id.tvOrderElementShopName);

            holder.tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderElementOrderStatus);

            holder.ivImg = (NetworkImageView) view.findViewById(R.id.ivShopListImage);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject orderObj = dataArray.optJSONObject(position);
        holder.tvOrderNo.setText("Order ID : " + orderObj.optString("orderNo"));
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                holder.tvDate.setText(DateAndTimeUtil.myDateAndTime(orderObj.optString("placedAt")));
        holder.tvDate.setText(orderObj.optString("placedAt"));
        holder.tvDetails.setText(orderObj.optString("itemCount") + " Items");
        holder.tvShopName.setText(orderObj.optString("shopName"));
        holder.tvOrderStatus.setText("Your order is " + orderObj.optString("orderStatus"));
        holder.ivImg.setImageUrl(Constants.BASE_URL + "/images/shops/thumbs/" + orderObj.optString("shopImage"), imageLoader);
//        view.setTag(orderObj.optString("orderDetailId"));
        return view;
    }

    class ViewHolder {
        TextView tvOrderNo;
        TextView tvDate;
        TextView tvDetails;
        TextView tvShopName;
        TextView tvOrderStatus;
        NetworkImageView ivImg;
    }


}