package com.smartsense.gifkar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CircleImageView;
import com.smartsense.gifkar.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btBack;
    TextView tvName, tvMobile, tvVerified;
    CircleImageView ivProfileImage;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_profile));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        tvMobile = (TextView) findViewById(R.id.tVProfileMobileNo);
        tvName = (TextView) findViewById(R.id.tvProfileName);
        tvVerified = (TextView) findViewById(R.id.tVProfileVerified);
        ivProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);
        imageLoader = GifkarApp.getInstance().getDiskImageLoader();
        try {
            JSONObject userInfo = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_INFO, ""));
            userInfo=userInfo.optJSONObject("userDetails");
//            Constants.BASE_URL + "/images/users/" +
            ivProfileImage.setImageUrl(Constants.BASE_URL + "/images/users/" + userInfo.optString("image"), imageLoader);
            tvName.setText(userInfo.optString("firstName") + " " + userInfo.optString("lastName"));
            tvMobile.setText(userInfo.optString("mobile"));
            if (userInfo.optString("mobile").equalsIgnoreCase("")){
                tvVerified.setVisibility(View.GONE);
                tvMobile.setVisibility(View.GONE);}
            else
                tvMobile.setText(userInfo.optString("mobile"));

            if (userInfo.optString("isMobileVerified").equalsIgnoreCase("1")) {
                tvVerified.setText("Verified");
                tvVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verified, 0);
            } else {
                tvVerified.setText("Unverified");
                tvVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unverified, 0);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
        tabLayout.addTab(tabLayout.newTab().setText("ADDRESS"));
        tabLayout.addTab(tabLayout.newTab().setText("CHANGE PASSWORD"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            default:
        }
    }

    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        final int TAB_COUNT = 3;
        private String[] tabtitles = new String[]{"PROFILE", "ADDRESS", "CHANGE PASSWORD"};
        private Context context;

        public ProfilePagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new AddressFragment();
                case 2:
                    return new ChangePasswordFragment();
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

}
