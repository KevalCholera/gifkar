package com.smartsense.gifkar;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartsense.gifkar.adapter.NewFeatureAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fragment extends Fragment implements ViewPager.OnPageChangeListener {


    public Main_Fragment() {
        // Required empty public constructor

    }

    ViewPager viewPager;
    TabLayout tabLayout;
    private Handler handler;
    View newFeaturesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        handler = new Handler();

        newFeaturesView = (View) rootView.findViewById(R.id.newfeaturesView);

//        FrameLayout fm = (FrameLayout) rootView.findViewById(R.id.fl_category);
        int height = getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newFeaturesView.getLayoutParams();
        params.height = (int) (height / 3.5);
        newFeaturesView.setLayoutParams(params);
        setReference(newFeaturesView);

        // tablayout and viewpager of category
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_main);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_main);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_idicator));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal_text), getResources().getColor(R.color.tab_idicator));
        setupViewPager(viewPager);



        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        Fragment p = new ProductFragment();
        String tempary = "    [{\n" +
                "        \"productManageId\":\"50\",\n" +
                "        \"productName\":\"Atta- Ashirvaad whole wheat\",\n" +
                "        \"productImage\":\"/productmanageimage/thumb/atta1.jpg\",\n" +
                "        \"purchaseLimit\":\"0\",\n" +
                "        \"rate\":\"250\",\n" +
                "        \"quantity\":\"10\",\n" +
                "        \"unitName\":\"kg\",\n" +
                "        \"packageName\":\"Packet\"\n" +
                "    }]" +
                "";
        adapter.addFragment(tempary, "Category 1");
        adapter.addFragment(tempary, "Category 2");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class Adapter extends FragmentPagerAdapter {
        //        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private final List<String> mFragmentProduct = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String product, String title) {
            mFragmentProduct.add(product);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductFragment.newInstance(mFragmentProduct.get(position));
        }

        @Override
        public int getCount() {
            return mFragmentTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private NewFeatureAdapter mAdapter;
    Runnable runnable;
    int position = 0;
    Integer[] mImageResources = {R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four};

    public void setReference(View view) {
        intro_images = (ViewPager) view.findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);

        mAdapter = new NewFeatureAdapter(getActivity(), mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);

        intro_images.setOnPageChangeListener(this);
        runnable = new Runnable() {
            public void run() {
                if (position >= mImageResources.length) {
                    position = 0;
                } else {
                    position = position + 1;
                }
                intro_images.setCurrentItem(position, true);
                handler.postDelayed(runnable, 3000);
            }
        };
        setUiPageViewController();
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

}
