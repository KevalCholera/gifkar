package com.smartsense.gifkar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.smartsense.gifkar.adapter.StartPagerAdapter;
import com.smartsense.gifkar.utill.CommonUtil;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(
                        R.color.red1));
            }
//            View decorView = getWindow().getDecorView();
//// Hide the status bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//            decorView.setSystemUiVisibility(uiOptions);

//            getSupportActionBar().hide();


        }
        setContentView(R.layout.activity_start);
        final RelativeLayout headImage = (RelativeLayout) findViewById(R.id.startHead);

//        FrameLayout fm = (FrameLayout) rootView.findViewById(R.id.fl_category);
        int height = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headImage.getLayoutParams();
        params.height = (int) (height / 3.5);
        headImage.setLayoutParams(params);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SIGN IN"));
        tabLayout.addTab(tabLayout.newTab().setText("SIGN UP"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Get the ViewPager and set its PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new StartPagerAdapter(getSupportFragmentManager(), this));
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CommonUtil.closeKeyboard(StartActivity.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoginFragment.RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != Activity.RESULT_OK) {
                LoginFragment.mShouldResolve = false;          }

            LoginFragment.mIsResolving = false;
            LoginFragment.mGoogleApiClient.connect();
        } else{
            Log.i("yes", "StartActvity");
            LoginFragment.callbackManager.onActivityResult(requestCode, resultCode, data);}
    }
}