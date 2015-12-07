package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;


public class NewFeatureAdapter extends PagerAdapter {

    private Context mContext;
    private Integer[] mResources;
    ImageLoader imgLoader = GifkarApp.getInstance().getDiskImageLoader();

    public NewFeatureAdapter(Context mContext, Integer[] mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.element_new_features, container, false);

//        NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.img_pager_item);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
//        imageView.setDefaultImageResId(R.drawable.normal_hori_bg);

        imageView.setImageResource(mResources[position]);
//        imageView.setImageUrl(mResources[position], imgLoader);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
