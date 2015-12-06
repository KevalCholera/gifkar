package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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
                return LayoutInflater.from(context).inflate(R.layout.product_element, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                TextView tvShopListShopName;
                TextView tvShopListCutofTime;
                TextView tvShopListMinOrder;
                TextView tvShopListDeliveryTime;
                TextView tvShopListRating;
                NetworkImageView ivShopListImage;

                tvShopListRating = (TextView) view.findViewById(R.id.tvShopListRating);
                tvShopListCutofTime = (TextView) view.findViewById(R.id.tvShopListCutofTime);
                tvShopListMinOrder = (TextView) view.findViewById(R.id.tvShopListMinOrder);
                tvShopListDeliveryTime = (TextView) view.findViewById(R.id.tvShopListDeliveryTime);
                tvShopListShopName = (TextView) view.findViewById(R.id.tvShopListShopName);
                ivShopListImage = (NetworkImageView) view.findViewById(R.id.ivShopListImage);

                tvShopListRating.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_RATING)));
                tvShopListCutofTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_CUT_OF_TIME)));
                tvShopListDeliveryTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DELIVERY_FROM)) +" to "+cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DELIVERY_TO)));
                tvShopListShopName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SHOP_NAME)));
                tvShopListMinOrder.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_MIN_ORDER)));
//              ivShopListImage.setDefaultImageResId(R.drawable.ic_gifkar_logo);
//              ivShopListImage.setImageUrl(Constants.BASE_IMAGE_URL + cursor.getString(cursor.getColumnIndexOrThrow("body")), imageLoader);

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


