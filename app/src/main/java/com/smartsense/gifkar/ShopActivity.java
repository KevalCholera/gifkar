package com.smartsense.gifkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.ShopPagerAdapter;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private TabLayout tabLayout;
    private Button btReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.shop_name));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_shop);
        btReport=(Button) findViewById(R.id.btReport);
        final View shopTOP = (View) findViewById(R.id.shopTop);
        int height = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) shopTOP.getLayoutParams();
        params.height = (int) (height / 3.5);
        shopTOP.setLayoutParams(params);


        tabLayout = (TabLayout) findViewById(R.id.tabs_shop);
        tabLayout.addTab(tabLayout.newTab().setText("CONTACT"));
        tabLayout.addTab(tabLayout.newTab().setText("REVIEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Get the ViewPager and set its PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_shop);

        viewPager.setAdapter(new ShopPagerAdapter(getSupportFragmentManager(), this));
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
        setupTabIcons();

    }
    private int[] imageResId = { R.drawable.ic_pencil, R.drawable.ic_pencil };
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btReport:
                startActivity(new Intent(this, ReportErrorActivity.class));
                break;
            default:
        }
    }
}
