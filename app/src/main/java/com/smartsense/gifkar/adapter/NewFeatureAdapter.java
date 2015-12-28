package com.smartsense.gifkar.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smartsense.gifkar.GifkarApp;
import com.smartsense.gifkar.R;

import org.json.JSONArray;


public class NewFeatureAdapter extends PagerAdapter {

    private Activity mContext;
    private JSONArray mResources;
    ImageLoader imgLoader = GifkarApp.getInstance().getDiskImageLoader();

    public NewFeatureAdapter(Activity mContext, JSONArray mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.element_new_features, container, false);

        NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.img_pager_item);
//        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
//        imageView.setDefaultImageResId(R.drawable.normal_hori_bg);
//        imageView.setImageResource(mResources[position]);
//        Constants.BASE_URL+"/images/bannerImages/"+
        imageView.setImageUrl(mResources.optJSONObject(position).optString("image"), imgLoader);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
