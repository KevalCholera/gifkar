package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DateAndTimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Ronak on 02-12-2015.
 */


public class ReviewAdapter extends BaseAdapter {
    JSONArray dataArray;
    private LayoutInflater inflater;
    static JSONArray productArray;
    JSONObject productObj;
    Context activity;
    Boolean check;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    public ReviewAdapter(Context activity, JSONArray dataArray, Boolean check) {
        this.activity = activity;
        this.dataArray = dataArray;
        this.check = check;
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
            view = inflater.inflate(R.layout.element_review, parent, false);
            // TODO Auto-generated method stub
            holder.tvReviewName = (TextView) view.findViewById(R.id.tvReviewName);
            holder.tvReviewDes = (TextView) view.findViewById(R.id.tvReviewDes);
            holder.tvReviewDate = (TextView) view.findViewById(R.id.tvReviewDate);
            holder.rbReview = (RatingBar) view.findViewById(R.id.rbReview);
            holder.ivReviewImage = (NetworkImageView) view.findViewById(R.id.ivReviewImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject addressObj = dataArray.optJSONObject(position);
        holder.tvReviewName.setText(addressObj.optJSONObject("user").optString("first_name") + " " + addressObj.optJSONObject("user").optString("last_name"));
        holder.tvReviewDes.setText(addressObj.optString("review"));
        Calendar mCalendar = Calendar.getInstance();
        holder.tvReviewDate.setText(DateAndTimeUtil.myDateAndTime(addressObj.optString("created_at"),mCalendar));
        holder.ivReviewImage.setDefaultImageResId(R.drawable.ic_user);
        holder.ivReviewImage.setImageUrl(Constants.BASE_URL + "/images/users/" + addressObj.optJSONObject("user").optString("image"), imageLoader);
        holder.rbReview.setRating(Float.valueOf(addressObj.optString("rating")));
        return view;
    }

    class ViewHolder {
        TextView tvReviewName;
        TextView tvReviewDes;
        TextView tvReviewDate;
        RatingBar rbReview;
        NetworkImageView ivReviewImage;
    }
}
