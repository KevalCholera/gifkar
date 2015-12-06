package com.smartsense.gifkar;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fragment extends Fragment implements ViewPager.OnPageChangeListener {


    ViewPager viewPager;
    TabLayout tabLayout;
    private Handler handler;
    View newFeaturesView;


    public Main_Fragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        handler = new Handler();

        newFeaturesView = (View) rootView.findViewById(R.id.newfeaturesView);

//        FrameLayout fm = (FrameLayout) rootView.findViewById(R.id.fl_category);
        int height = getResources().getDisplayMetrics().heightPixels;
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) newFeaturesView.getLayoutParams();
//        params.height = (int) (height / 3.5);
//        newFeaturesView.setLayoutParams(params);
        setReference(newFeaturesView);

        // tablayout and viewpager of category
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_main);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_main);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_idicator));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal_text), getResources().getColor(R.color.tab_idicator));
        setupViewPager(viewPager);


        return rootView;
    }

    DataBaseHelper dbHelper;

    private void setupViewPager(ViewPager viewPager) {
        dbHelper = new DataBaseHelper(getActivity());
        Adapter adapter = new Adapter(getChildFragmentManager());
        String tempary = "{ \"eventId\" : 123, \n" +
                "\"errorCode\":0,\n" +
                " \"status\":200,\n" +
                " \"message\":\"Category List.\", \n" +
                "\"data\" : { \"categories\" : [ { \"id\":\"1\", \"name\":\"Cakes\",\"shops\" : [ { \"id\":\"15\",\"name\":\"Shop 7\",\"cutOffTime\":\"0\",\"minOrder\":\"21\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"2\",\"name\":\"Sweets\",\"shops\":[{\"id\":\"15\",\"name\":\"Shop 7\",\"cutOffTime\":\"0\",\"minOrder\":\"21\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"3\",\"name\":\"Flowers\"},{\"id\":\"4\",\"name\":\"Backery\",\"shops\":[{\"id\":\"15\",\"name\":\"Shop 7\",\"cutOffTime\":\"0\",\"minOrder\":\"21\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"5\",\"name\":\"Indian\"},{\"id\":\"6\",\"name\":\"Italian\"} ] } }\n";
        try {
            JSONObject response = new JSONObject(tempary);
            JSONArray category = response.optJSONObject("data").optJSONArray("categories");
            insertItemInCart(adapter, category);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    CommonUtil commonUtil = new CommonUtil();

    public void insertItemInCart(Adapter adapter, JSONArray product) {
        try {
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();

            for (int i = 0; i < product.length(); i++) {
            JSONObject catJson = product.optJSONObject(i);
            if (catJson.has("shops")) {
                for (int j = 0; j < catJson.optJSONArray("shops").length(); j++) {
                    JSONObject prodJson = catJson.optJSONArray("shops").optJSONObject(j);
                    ContentValues values = new ContentValues();
                    values.put(DataBaseHelper.COLUMN_SHOP_ID, prodJson.optString("id"));
                    values.put(DataBaseHelper.COLUMN_SHOP_NAME, prodJson.optString("name"));
                    values.put(DataBaseHelper.COLUMN_CUT_OF_TIME, prodJson.optString("cutOffTime"));
                    values.put(DataBaseHelper.COLUMN_MIN_ORDER, prodJson.optString("minOrder"));
                    values.put(DataBaseHelper.COLUMN_MID_NIGHT_DEL, prodJson.optString("midnightDeliveryStatus"));
                    values.put(DataBaseHelper.COLUMN_SAME_DAY_DELIVERY, prodJson.optString("sameDayDeliveryStatus"));
                    values.put(DataBaseHelper.COLUMN_RATING, prodJson.optString("rating"));
                    values.put(DataBaseHelper.COLUMN_OPEN_AT, prodJson.optString("opensAt"));
                    values.put(DataBaseHelper.COLUMN_CLOSE_AT, prodJson.optString("closesAt"));
                    values.put(DataBaseHelper.COLUMN_DELIVERY_FROM, prodJson.optString("deliveryFrom"));
                    values.put(DataBaseHelper.COLUMN_DELIVERY_TO, prodJson.optString("deliveryTo"));
                    values.put(DataBaseHelper.COLUMN_REMOTE_DELIVERY, prodJson.optString("remoteArea"));
                    values.put(DataBaseHelper.COLUMN_CATEGORY_ID, catJson.optString("id"));
                    values.put(DataBaseHelper.COLUMN_CATEGORY_NAME, catJson.optString("name"));
//                    commonUtil.insert(dbHelper, DataBaseHelper.TABLE_SHOP, values);
                    db.insert(DataBaseHelper.TABLE_SHOP, null, values);
                    db.close();
                }
                adapter.addFragment(catJson.optString("id"), catJson.optString("name"));
            }
        }} catch (Exception e) {
            e.printStackTrace();
        }
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
        private final List<String> mFragmentID = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String id, String title) {
            mFragmentID.add(id);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductFragment.newInstance(mFragmentID.get(position));
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
