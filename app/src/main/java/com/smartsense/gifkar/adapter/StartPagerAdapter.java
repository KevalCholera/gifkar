package com.smartsense.gifkar.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smartsense.gifkar.LoginFragment;
import com.smartsense.gifkar.SignupFragment;

/**
 * Created by Sanchi on 30-Nov-15.
 */
public class StartPagerAdapter extends FragmentPagerAdapter {

    final int TAB_COUNT = 2;
    private String[] tabtitles = new String[]{"SIGN IN", "SIGN UP"};
    private Context context;

    public StartPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignupFragment();
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
        return tabtitles[position];
    }
}
