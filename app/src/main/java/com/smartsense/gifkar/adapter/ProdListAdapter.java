package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smartSense on 06-12-2015.
 */
public class ProdListAdapter extends RecyclerView.Adapter<ProdListAdapter.ViewHolder> {
    CursorAdapter mCursorAdapter;
    Context mContext;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();

    public ProdListAdapter(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.element_product, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                TextView tvProdElementCate = (TextView) view.findViewById(R.id.tvProdElementCate);
                TextView tvProdElementDT = (TextView) view.findViewById(R.id.tvProdElementDeliveryTime);
                TextView tvProdElementPrice = (TextView) view.findViewById(R.id.tvProdElementRs);
                TextView tvProdElementName = (TextView) view.findViewById(R.id.tvProdElementName);

                TextView tvProdElementQty = (TextView) view.findViewById(R.id.tvProdElementQty);
                ImageButton ibProdElementPlus = (ImageButton) view.findViewById(R.id.ibProdElementPlus);
                ImageButton ibProdElementMinus = (ImageButton) view.findViewById(R.id.ibProdElementMinus);
                NetworkImageView ivShopListImage = (NetworkImageView) view.findViewById(R.id.ivProdElementImage);

//              ivShopListImage.setDefaultImageResId(R.drawable.ic_gifkar_logo);
//              ivShopListImage.setImageUrl(Constants.BASE_IMAGE_URL + cursor.getString(cursor.getColumnIndexOrThrow("body")), imageLoader);

                tvProdElementCate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_QUANTITY)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_UNIT_NAME)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME)));
                tvProdElementDT.setText(cursor.getString(cursor
                        .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_EARLIY_DEL)));
                tvProdElementPrice.setText(cursor.getString(cursor
                        .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_PRICE)));
                tvProdElementName.setText(cursor.getString(cursor
                        .getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_NAME)));
                ibProdElementPlus.setTag(tvProdElementQty);
                ibProdElementMinus.setTag(tvProdElementQty);
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

                view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_PROD_ID)));

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


