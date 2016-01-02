package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.DataBaseHelper;

/**
 * Created by smartSense on 06-12-2015.
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {
    CursorAdapter mCursorAdapter;
    Context mContext;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    public ShopListAdapter(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.element_shop, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                TextView tvShopListShopName;
                TextView tvShopListCutofTime;
                TextView tvShopListMinOrder;
                TextView tvShopListDeliveryTime;
                TextView tvShopListRating;
                TextView tvShopListTag;
                ImageView ivShopListMidNight;
                NetworkImageView ivShopListImage;

                tvShopListRating = (TextView) view.findViewById(R.id.tvShopListRating);
                tvShopListCutofTime = (TextView) view.findViewById(R.id.tvShopListCutofTime);
                tvShopListMinOrder = (TextView) view.findViewById(R.id.tvShopListMinOrder);
                tvShopListDeliveryTime = (TextView) view.findViewById(R.id.tvShopListDeliveryTime);
                tvShopListShopName = (TextView) view.findViewById(R.id.tvShopListShopName);
                tvShopListTag = (TextView) view.findViewById(R.id.tvShopListTag);
                ivShopListMidNight = (ImageView) view.findViewById(R.id.ivShopListMidNight);
                ivShopListImage = (NetworkImageView) view.findViewById(R.id.ivShopListImage);

                if (cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_MID_NIGHT_DEL)).equalsIgnoreCase("1")) {
                    ivShopListMidNight.setVisibility(View.VISIBLE);
                } else {
                    ivShopListMidNight.setVisibility(View.GONE);
                }
//                tvShopListRating.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_RATING))==null ? "0" : cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_RATING)));
                tvShopListCutofTime.setText("Cut of Time: " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_CUT_OF_TIME)) + "hours");
                tvShopListDeliveryTime.setText("Delivery Time: " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DELIVERY_FROM)) + " to " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DELIVERY_TO)));
                tvShopListShopName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_NAME)));
                tvShopListMinOrder.setText("Minimum Order: \u20B9 " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_MIN_ORDER)));
                tvShopListTag.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TAGS)));
                ivShopListImage.setDefaultImageResId(R.drawable.default_img);
//                Constants.BASE_URL + "/images/shops/thumbs/" +
                ivShopListImage.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_IMAGE_THUMB)), imageLoader);
                view.setTag(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_ID)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_CATEGORY_ID))+" "+cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_NAME))+" "+cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_IMAGE_THUMB)));
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View product;

        public ViewHolder(View view) {
            super(view);
            product = itemView.findViewById(R.id.one_product_element);

        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }
}


