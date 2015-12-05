package com.smartsense.gifkar;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.smartsense.gifkar.adapter.StartPagerAdapter;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);

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
}