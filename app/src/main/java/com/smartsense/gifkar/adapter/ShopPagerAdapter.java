package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.smartsense.gifkar.R;
import com.smartsense.gifkar.ReviewsFragment;
import com.smartsense.gifkar.ShopDetailFragment;

/**
 * Created by Sanchi on 30-Nov-15.
 */
public class ShopPagerAdapter extends FragmentPagerAdapter {

    final int TAB_COUNT = 2;
    private String[] tabtitles = new String[]{"CONTACT", "REVIEWS"};
    private int[] imageResId = { R.drawable.ic_pencil, R.drawable.ic_pencil };
    private Context context;

    public ShopPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ShopDetailFragment();
            case 1:
                return new ReviewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
//        return tabtitles[position];
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabtitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
